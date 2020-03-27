package sxk180037;
/* Starter code for enumerating topological orders of a DAG
 */

import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

import java.util.ArrayList;
import java.util.Scanner;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
  boolean print; // Set to true to print array in visit
  long count; // Number of permutations or combinations visited
  Selector sel;

  public EnumerateTopological(Graph g) {
    super(g, new EnumVertex());
    print = false;
    count = 0;
    sel = new Selector();
  }

  static class EnumVertex implements Factory {
    boolean seen;
    int inDegree;

    EnumVertex() {
      inDegree = 0;
      seen = false;
    }

    public EnumVertex make(Vertex u) {
      return new EnumVertex();
    }
  }

  class Selector extends Enumerate.Approver<Vertex> {
    @Override
    public boolean select(Vertex u) {
      return true;
    }

    @Override
    public void unselect(Vertex u) {
    }

    @Override
    public void visit(Vertex[] arr, int k) {
      count++;
      if (print) {
        for (Vertex u : arr) {
          System.out.print(u + " ");
        }
        System.out.println();
      }
    }
  }

  /**
   * increment/decrement incoming edge count once edge having this vertex is
   * selected
   *
   * @param u
   * @param val
   */
  void updateIndegree(Vertex u, int val) {
    for (Edge e : g.incident(u)) {
      Vertex v = e.otherEnd(u);
      get(v).inDegree += val;
    }
  }

  /**
   * Iterate all top logical orders
   *
   * @param list
   */
  public void enumerate(ArrayList<Vertex> list) {
    // call visit method to increment and print if needed
    if (list.size() == this.g.size()) {
      Vertex[] newList = new Vertex[g.size()];
      sel.visit(list.toArray(newList), 0);
    }

    for (Vertex u : this.g) {
      if (!get(u).seen && get(u).inDegree == 0) {
        list.add(u);
        get(u).seen = true;
        updateIndegree(u, -1);
        enumerate(list);
        get(u).seen = false;
        updateIndegree(u, 1);
        list.remove(list.size() - 1);
      }
    }
  }

  // To do: LP4; return the number of topological orders of g
  public long enumerateTopological(boolean flag) {
    print = flag;

    // initialize inDegree (in EnumVertex) of every vertex
    for (Vertex u : g) { // iterating through the vertices in the graph
      get(u).inDegree = u.inDegree();
    }

    enumerate(new ArrayList<Vertex>());
    return count;
  }

  // -------------------static methods----------------------

  public static long countTopologicalOrders(Graph g) {
    EnumerateTopological et = new EnumerateTopological(g);
    return et.enumerateTopological(false);
  }

  public static long enumerateTopologicalOrders(Graph g) {
    EnumerateTopological et = new EnumerateTopological(g);
    return et.enumerateTopological(true);
  }

  public static void main(String[] args) throws Exception {
    int VERBOSE = 0;
    if (args.length > 0) {
      VERBOSE = Integer.parseInt(args[0]);
    }
    String graph = "";

    Scanner in = args.length > 1 ? new Scanner(new java.io.File(args[1])) : new Scanner(graph);

    Graph g = Graph.readDirectedGraph(in);
    Graph.Timer t = new Graph.Timer();
    long result;
    if (VERBOSE > 0) {
      result = enumerateTopologicalOrders(g);
    } else {
      result = countTopologicalOrders(g);
    }
    System.out.println("\n" + result + "\n" + t.end());
  }

}
