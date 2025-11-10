package com.scc;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class SCC {
    private final int n;
    private final List<List<Integer>> adj;
    private int time = 0;
    private int[] disc, low;
    private boolean[] inStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;

    public SCC(int n, List<List<Integer>> adj) {
        this.n = n;
        this.adj = adj;
    }

    public List<List<Integer>> findSCCs() {
        disc = new int[n];
        low = new int[n];
        inStack = new boolean[n];
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        Arrays.fill(disc, -1);

        for (int i = 0; i < n; i++) {
            if (disc[i] == -1)
                dfs(i);
        }
        return components;
    }

    private void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        inStack[u] = true;

        for (int v : adj.get(u)) {
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // Root of SCC
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            int node;
            do {
                node = stack.pop();
                inStack[node] = false;
                comp.add(node);
            } while (node != u);
            components.add(comp);
        }
    }

    public static JSONObject buildCondensation(List<List<Integer>> comps, List<List<Integer>> adj) {
        int c = comps.size();
        Map<Integer, Integer> nodeToComp = new HashMap<>();
        for (int i = 0; i < c; i++) {
            for (int node : comps.get(i)) nodeToComp.put(node, i);
        }

        Set<String> edgeSet = new HashSet<>();
        List<List<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < c; i++) dag.add(new ArrayList<>());

        for (int u = 0; u < adj.size(); u++) {
            for (int v : adj.get(u)) {
                int cu = nodeToComp.get(u);
                int cv = nodeToComp.get(v);
                if (cu != cv && edgeSet.add(cu + "->" + cv)) {
                    dag.get(cu).add(cv);
                }
            }
        }

        JSONObject out = new JSONObject();
        out.put("num_components", c);
        JSONArray compArr = new JSONArray();
        for (List<Integer> comp : comps) compArr.put(new JSONArray(comp));
        out.put("components", compArr);

        JSONArray dagArr = new JSONArray();
        for (int i = 0; i < dag.size(); i++) {
            for (int v : dag.get(i)) {
                JSONObject e = new JSONObject();
                e.put("u", i);
                e.put("v", v);
                dagArr.put(e);
            }
        }
        out.put("condensation_edges", dagArr);
        return out;
    }
}
