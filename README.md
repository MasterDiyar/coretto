# Assignment 4 – Project Milestone 2
**Topic:** Graph Algorithms (SCC, Condensation, Topological Sort, Node-Weighted DAG Paths)

---

## 1. Overview

This project implements several core graph algorithms as part of Assignment 4:

- **Strongly Connected Components (SCC)** using **Tarjan’s algorithm**
- **Condensation Graph (DAG of SCCs)**
- **Topological Sort** (Kahn’s algorithm)
- **Shortest and Longest Paths in a Node-Weighted DAG**

````
src/
├─ main/java/
│ ├─ com/scc/ → SCC (Tarjan)
│ ├─ com/topo/ → Topological Sort
│ ├─ com/dagsp/ → DAG shortest/longest paths
│ └─ com/metrics/ → Metrics interface
└─ test/java/ → Unit tests (JUnit)
````

---

### 2. Dataset Generation (Graph Data)

All graph datasets are stored under `/data/`.

| Category | Nodes (n) | Description | Variants | File |
|-----------|-----------|--------------|-----------|------|
| **Small** | 6–10 | Simple graphs with 1–2 cycles or pure DAG | 3 | `small_graphs.json` |
| **Medium** | 10–20 | Mixed structures with multiple SCCs | 3 | `medium_graphs.json` |
| **Large** | 20–50 | Performance and timing tests | 3 | `large_graphs.json` |

Each dataset file contains three graphs in JSON (GSON) format:
```json
{
  "category": "small",
  "graphs": [
    {
      "id": 1,
      "n": 8,
      "nodes": [{"id": 0, "weight": 3}, ...],
      "edges": [{"u": 0, "v": 1, "w": 5}, ...]
    },
    ...
  ]
}
```
All node weights represent processing duration or cost, which is used for Node-Weighted Path calculations.

### 3. Algorithms Implemented
3.1 SCC (Tarjan’s Algorithm)
Finds all strongly connected components.

Time complexity: O(V + E)

Returns a list of SCCs and their internal nodes.

3.2 Condensation Graph
Builds a DAG from the SCCs.

Each node in DAG represents one SCC.

3.3 Topological Sort (Kahn)
Applied to the condensation graph.

Produces a valid order for DAG traversal.

3.4 Node-Weighted DAG Paths
Computes both shortest and longest paths using node weights.

Used for critical-path analysis.

### 4. Metrics
The project includes a Metrics interface to measure:

Number of visited nodes and edges.

Execution time for each algorithm.

Comparisons between small, medium, and large datasets.

Example:

```java
metrics.start();
List<List<Integer>> scc = sccFinder.findSCCs();
metrics.stop();
System.out.println("Time (ms): " + metrics.getTime());
```
### 5. Example Output
Example for one small dataset:

```json
{
  "scc_count": 2,
  "scc_components": [[0,1,2], [3]],
  "condensation_adj": [[1], []],
  "is_condensation_dag": true,
  "condensation_topo_order": [0,1],
  "dag_shortest_node_weighted": [6, 7],
  "dag_longest_node_weighted": [6, 7]
}
```
### 6. Performance Analysis
Category	Graphs	Avg. Nodes	Avg. Edges	Avg. Time (ms)	Observations
Small	3	8	~10	< 1	All algorithms run instantly
Medium	3	15	~25	2–4	SCC and TopoSort perform well
Large	3	35	~60	10–20	Noticeable growth in time, still linear O(V+E)


### 7.Conclusion
Implemented SCC, Topological Sort, and DAG path algorithms successfully.

Verified correctness using multiple datasets.

Performance scales linearly with graph size.

The project satisfies all functional requirements of Assignment 4 Milestone 2.