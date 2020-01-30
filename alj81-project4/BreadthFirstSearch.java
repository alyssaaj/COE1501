// Alyssa Jordan
// alj81@pitt.edu
// 11/12/19

/******************************************************************************
 *  Compilation:  javac BreadthFirstSearch.java
 *  Dependencies: NetworkGraph.java Queue.java
 ******************************************************************************/


/**
Based off of textbook code
 */
public class BreadthFirstSearch {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s-v path

    /**
     * Computes the shortest path between the source vertex {@code s}
     * and every other vertex in the graph {@code G}.
     */
    public BreadthFirstSearch(NetworkGraph G, int s) {
        marked = new boolean[G.V()];
        validateVertex(s);
        bfs(G, s);
    }

    // breadth-first search from a single source
    private void bfs(NetworkGraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adjC(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex {@code s} (or sources) and vertex {@code v}?
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


}