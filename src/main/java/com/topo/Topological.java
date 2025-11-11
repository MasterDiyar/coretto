package com.topo;

import com.metrics.Metrics;
import java.util.*;

public class Topological {
    private final List<List<Integer>> adj;
    private final Metrics metrics;

    public Topological(List<List<Integer>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }

    public List<Integer> sortKahn() {
        int n = adj.size();
        int[] indeg = new int[n];
        for (List<Integer> edges : adj)
            for (int v : edges) indeg[v]++;

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            metrics.inc("pop");
            for (int v : adj.get(u)) {
                if (--indeg[v] == 0) q.add(v);
                metrics.inc("push");
            }
        }
        return order;
    }
}