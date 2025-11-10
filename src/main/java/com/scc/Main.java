package com.scc;

import com.dagsp.Dagsp;
import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.*;
import java.sql.Time;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String jsonText = Files.readString(Path.of("tasks.json"));
        JSONObject input = new JSONObject(jsonText);

        int n = input.getInt("n");
        JSONArray edges = input.getJSONArray("edges");
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            adj.get(e.getInt("u")).add(e.getInt("v"));
        }

        SCC scc = new SCC(n, adj);
        List<List<Integer>> comps = scc.findSCCs();
        JSONObject condensation = SCC.buildCondensation(comps, adj);
        JSONObject dagspResult = Dagsp.compute(input);

        JSONObject out = new JSONObject();
        out.put("SCC_result", condensation);
        out.put("DAG_paths", dagspResult);

        Files.writeString(Path.of("output.json"), out.toString(2));
        System.out.println("Output saved to output.json");
    }
}