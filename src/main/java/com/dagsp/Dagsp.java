package com.dagsp;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class Dagsp {
    private static class Edge {
        int to, w;
        Edge(int t, int w) { to = t; this.w = w; }
    }

    public static JSONObject compute(JSONObject json) {
        int n = json.getInt("n");
        int source = json.getInt("source");
        JSONArray edges = json.getJSONArray("edges");

        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            adj.get(e.getInt("u")).add(new Edge(e.getInt("v"), e.getInt("w")));
        }
        List<Integer> topo = topoSort(n, adj);

        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        for (int u : topo) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (Edge e : adj.get(u)) {
                    if (dist[e.to] > dist[u] + e.w) {
                        dist[e.to] = dist[u] + e.w;
                        parent[e.to] = u;
                    }
                }
            }
        }

        double[] longDist = new double[n];
        Arrays.fill(longDist, Double.NEGATIVE_INFINITY);
        longDist[source] = 0;

        for (int u : topo) {
            if (longDist[u] != Double.NEGATIVE_INFINITY) {
                for (Edge e : adj.get(u)) {
                    if (longDist[e.to] < longDist[u] + e.w)
                        longDist[e.to] = longDist[u] + e.w;
                }
            }
        }

        // Find critical path (max distance)
        double maxLen = Double.NEGATIVE_INFINITY;
        int end = -1;
        for (int i = 0; i < n; i++) {
            if (longDist[i] > maxLen) {
                maxLen = longDist[i];
                end = i;
            }
        }

        List<Integer> path = new ArrayList<>();
        for (int v = end; v != -1; v = parent[v]) path.add(v);
        Collections.reverse(path);

        JSONObject result = new JSONObject();

        JSONArray shortest = new JSONArray();
        for (double d : dist) {
            if (Double.isInfinite(d))
                shortest.put("INF");
            else
                shortest.put(d);
        }

        JSONArray longest = new JSONArray();
        for (double d : longDist) {
            if (Double.isInfinite(d))
                longest.put("-INF");
            else
                longest.put(d);
        }

        result.put("shortest_distances", shortest);
        result.put("longest_distances", longest);

        result.put("critical_path_length", maxLen);
        result.put("critical_path", new JSONArray(path));
        return result;
    }

    private static List<Integer> topoSort(int n, List<List<Edge>> adj) {
        int[] indeg = new int[n];
        for (List<Edge> list : adj)
            for (Edge e : list) indeg[e.to]++;
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (Edge e : adj.get(u)) {
                if (--indeg[e.to] == 0) q.add(e.to);
            }
        }
        return order;
    }
}
