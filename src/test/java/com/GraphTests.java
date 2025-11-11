package com;

import com.scc.SCC;
import com.topo.Topological;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTests {
    @Test
    public void testSCCSimple() {
        List<List<Integer>> adj = Arrays.asList(
                List.of(1), List.of(2), List.of(0), List.of(2)
        );
        SCC scc = new SCC(4, adj);
        var comps = scc.findSCCs();
        assertEquals(2, comps.size());
    }

    @Test
    public void testTopoOrder() {
        List<List<Integer>> adj = Arrays.asList(
                List.of(1,2), List.of(3), List.of(3), List.of()
        );
        Topological topo = new Topological(adj, new DummyMetrics());
        var order = topo.sortKahn();
        assertEquals(4, order.size());
    }
}

class DummyMetrics implements com.metrics.Metrics {
    private final Map<String,Integer> m = new HashMap<>();
    private long t;
    public void start(){ t=System.nanoTime(); }
    public void stop(){ t=System.nanoTime()-t; }
    public void inc(String key){ m.put(key,m.getOrDefault(key,0)+1); }
    public long getTime(){ return t; }
    public int get(String key){ return m.getOrDefault(key,0); }
}