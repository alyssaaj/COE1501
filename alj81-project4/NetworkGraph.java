// Alyssa Jordan
// alj81@pitt.edu
// 11/12/19

import java.util.*;
import java.io.*;


public class NetworkGraph{

	private ArrayList<ArrayList<Integer>> copper_vlist; 	// adjacency list for copper only graph
	private ArrayList<Edge>[] adj;							// adjacency list for main graph
	private ArrayList<Integer>[] adjArt;					// adjacency list for temp graphs used in option 4 w articulation points
	private final int V;									// vertices
	private int E;											// edges

	// creates normal graph
	public NetworkGraph(int numOfVertices){
		this.V = numOfVertices;
		this.E = 0;

		copper_vlist = new ArrayList<ArrayList<Integer>>(numOfVertices);
		adj = (ArrayList<Edge>[]) new ArrayList[V];

		for (int i = 0; i<numOfVertices; i++){
			adj[i] = new ArrayList<Edge>();
			ArrayList<Integer> c = new ArrayList<Integer>();
			c.add(i);
			copper_vlist.add(c);
		}

	}

	// creates articulation graphs
	public NetworkGraph(int numOfVertices, boolean artCheck){
		this.V = numOfVertices;
		this.E = 0;

		adjArt = (ArrayList<Integer>[]) new ArrayList[V]; 
		for (int i = 0; i<numOfVertices; i++){
			adjArt[i] = new ArrayList<Integer>();
		}


	}

	// return vertices
	public int V(){
		return V;
	}

	// return edges
	public int E(){
		return E;
	}

	// add edges to graph
	public void addToGraph(String[] edge_info){
		Edge e = new Edge(edge_info);
		adj[e.edge_pt1].add(e);
		String[] edge_info2 = edge_info;
		edge_info2[0]=String.valueOf(e.edge_pt2);
		edge_info2[1]=String.valueOf(e.edge_pt1);
		Edge e2 = new Edge(edge_info2);
		adj[e2.edge_pt1].add(e2);

		this.E = E + 2;

		// if copper link run add the copper graph function
		if(edge_info[2].equals("copper")){
			addToCopperGraph(Integer.parseInt(edge_info[0]), Integer.parseInt(edge_info[1]));
		}

	}

	// add to copper graph
	public void addToCopperGraph(int e_pt1, int e_pt2){

		ArrayList<Integer> c1 = copper_vlist.get(e_pt1);
		
		if(!c1.contains(e_pt2)){
			c1.add(e_pt2);
		}

		ArrayList<Integer> c2 = copper_vlist.get(e_pt2);
		if(!c2.contains(e_pt1)){
			c2.add(e_pt1);
		}

	}

	// gets neighbors of vertices for copper graph
	public Iterable<Integer> adjC(int v){
		return copper_vlist.get(v);
	}

	// prints graph
	public void printGraph(){
        for (int i = 0; i < adj.length; i++) { 
        	System.out.print(i+": ");
            for (Edge e : adj(i)) { 
                System.out.print(e.edge_pt2 + " "); 
            } 
            System.out.println(); 
        }
	}

	// checks to see if vertex is in graph
	public Boolean inGraph(int vert){
		if(vert < V && vert >= 0){
			return true;
		}
		else{
			return false;
		}
	}

	public void validateVertex(int v){
		if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	// gets neighbors of vertices for main graph
	public Iterable<Edge> adj(int v){
		return adj[v];
	}

	// gets all the edges in the graph
	public Iterable<Edge> edges(){
		ArrayList<Edge> list = new ArrayList<Edge>();
		for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
	}

	// add edges to articulation graph
	public void addEdges(int v1, int v2){
		adjArt[v1].add(v2);
		adjArt[v2].add(v1);
		this.E = E + 2;
	}

	// get neighbors for articulation graph
	public Iterable<Integer> adjS(int v){
		return adjArt[v];
	}

}
