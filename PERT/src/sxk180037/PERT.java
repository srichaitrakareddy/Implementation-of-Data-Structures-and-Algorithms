/* Driver code for PERT algorithm (LP4)
 */
package sxk180037;

import rbk.Graph.Vertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
  int criticalPathLength;
  int totalDuration;

  public static class PERTVertex implements Factory {
    int duration;
    int slack;
    int ec;
    int lc;

    public PERTVertex(Vertex u) {
      duration = 0;
      slack = 0;
      ec = 0;
      lc = 0;
    }

    public PERTVertex make(Vertex u) {
      return new PERTVertex(u);
    }
  }

  public PERT(Graph g) {
    super(g, new PERTVertex(null));
    this.criticalPathLength = 0;
    this.totalDuration = 0;
  }

  public void setDuration(Vertex u, int d) {
    get(u).duration = d;
  }

  public boolean pert() {
    // Add source and target edges
    addEdgesForST();
    List<Vertex> tOrder = topologicalOrder();

    if (tOrder == null) {
      return true;
    }
    int maxTime = 0;

    for (Vertex u : tOrder) {
      for (Edge e : this.g.outEdges(u)) {
        Vertex v = e.otherEnd(u);
        if (ec(u) + get(v).duration > ec(v)) {
          setEC(v, ec(u) + get(v).duration);
        }
      }

      if (u.getName() == g.size()) {
        this.totalDuration = ec(u);
        maxTime = totalDuration;
        setLC(u, this.totalDuration);
      }
    }

    for (Vertex u : this.g) {
      setLC(u, maxTime);
    }

    ListIterator<Vertex> itr = tOrder.listIterator(tOrder.size());
    while (itr.hasPrevious()) {
      Vertex u = itr.previous();
      for (Edge e : this.g.outEdges(u)) {
        Vertex v = e.otherEnd(u);
        if (lc(v) - get(v).duration < lc(u)) {
          setLC(u, lc(v) - get(v).duration);
        }
        get(u).slack = lc(u) - ec(u);
      }
    }

    for (Vertex u : this.g) {
      if (ec(u) - lc(u) == 0) {
        this.criticalPathLength++;
      }
    }

    return false;
  }

  public int ec(Vertex u) {
    return get(u).ec;
  }

  public void setEC(Vertex u, int ec) {
    get(u).ec = ec;
  }

  public int lc(Vertex u) {
    return get(u).lc;
  }

  public void setLC(Vertex u, int lc) {
    get(u).lc = lc;
  }

  public int slack(Vertex u) {
    return lc(u) - ec(u);
  }

  public int criticalPath() {
    return this.totalDuration;
  }

  public boolean critical(Vertex u) {
    return slack(u) == 0;
  }

  public int numCritical() {
    return this.criticalPathLength;
  }

  /**
   * Adds source and target as dummy edges
   */
  public void addEdgesForST() {
    Vertex s = g.getVertex(1);
    Vertex t = g.getVertex(g.size());
    int m = g.edgeSize();
    for (int i = 2; i < g.size(); i++) {
      Vertex u = g.getVertex(i);
      // add source edge to edges with indegree 0
      if (u.inDegree() == 0) {
        g.addEdge(s, u, 1, ++m);
      }
      // add target edge to edges with outdegree 0
      if (u.outDegree() == 0) {
        g.addEdge(u, t, 1, ++m);
      }
    }
  }

  // setDuration(u, duration[u.getIndex()]);
  public static PERT pert(Graph g, int[] duration) {
    PERT p = new PERT(g);
    // Add source and target edges
    p.addEdgesForST();

    for (Vertex u : g) {
      p.setDuration(u, duration[u.getIndex()]);
    }

    return p.pert() ? null : p;
  }

  /**
   * Utility function to get topological order using DFS
   *
   * @param v
   * @param g
   * @param visited
   * @param onPath
   * @param finisList
   * @throws Exception
   */
  public void depthFirstSearch(Vertex v, Graph g, boolean[] visited, boolean[] onPath, LinkedList<Vertex> finisList)
      throws Exception {
    if (visited[v.getIndex()]) {
      return;
    }

    // this will make sure, the same node is not visited again
    visited[v.getIndex()] = true;
    // set onPath for this vertex and unset it once lookup is done
    onPath[v.getIndex()] = true;

    // get adj list of current vertex and iterate through all edges if not visited
    for (Edge edge : this.g.outEdges(v)) {
      // If the node is on current path, there is a cycle
      // throw exception with relevant error message
      if (onPath[edge.toVertex().getIndex()]) {
        throw new Exception("Not DAG! Cycle found in the graph at vertex " + edge.toVertex().getName());
      }

      if (!visited[edge.toVertex().getIndex()]) {
        depthFirstSearch(edge.toVertex(), g, visited, onPath, finisList);
      }
    }
    // Adding to the beginning - based on finish times
    finisList.addFirst(v);
    // unset it once its done
    onPath[v.getIndex()] = false;
  }

  /**
   * Member function to find topological order (taken from SP)
   *
   * @return
   */
  public List<Vertex> topologicalOrder() {
    // if not a directed graph, there is no topological order
    if (!this.g.isDirected()) {
      return null;
    }

    LinkedList<Vertex> finishList = new LinkedList<Vertex>();
    boolean[] visited = new boolean[this.g.size()];

    // onPath - helps us in detecting cylces
    boolean[] onPath = new boolean[this.g.size()];

    Iterator<Vertex> gIterator = this.g.iterator();

    try {
      gIterator = this.g.iterator();
      while (gIterator.hasNext()) {
        Vertex v = gIterator.next();
        boolean isVertexVisited = visited[v.getIndex()];
        if (!isVertexVisited) {
          depthFirstSearch(v, g, visited, onPath, finishList);
        }
      }
    } catch (Exception ex) {
      System.out.println("Error: " + ex.getMessage());
      return null;
    }

    return finishList;
  }

  public static void main(String[] args) throws Exception {
    String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
    Scanner in;
    // If there is a command line argument, use it as file from which
    // input is read, otherwise use input from string.
    in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
    Graph g = Graph.readDirectedGraph(in);
    g.printGraph(false);

    int[] duration = new int[g.size()];
    for (int i = 0; i < g.size(); i++) {
      duration[i] = in.nextInt();
    }
    PERT p = PERT.pert(g, duration);

    // Run PERT algorithm. Returns null if g is not a DAG
    if (p == null) {
      System.out.println("Invalid graph: not a DAG");
    } else {
      System.out.println("Number of critical vertices: " + p.numCritical());
      System.out.println("u\tEC\tLC\tSlack\tCritical");
      for (Vertex u : g) {
        System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
      }
    }
  }
}
