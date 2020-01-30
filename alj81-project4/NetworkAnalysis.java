// Alyssa Jordan
// alj81@pitt.edu
// 11/12/19

import java.util.*;
import java.io.*;


public class NetworkAnalysis
{
	public static void main(String[] args){
		
		// function to read in file and create graph
		NetworkGraph network_graph = inputNetworkData();

		// check to make sure graph got read in properly
		if (network_graph == null){
			System.out.println("Error: Please try again");
			System.exit(1);
		}

		// prints out graph initially so you can see it
		//network_graph.printGraph();

		// Program Options
		int choice; 
		do{
			// prints choices and gets user choice
			choice= userMenu();

			// based on choice runs function
			switch (choice){
				// Find the lowest latency path between any two points and give the bandwidth available along that path
				case 1:
					findLowestLatencyPath(network_graph);
					break;
				// Determine where or not the graph is copper-only connected
				case 2:
					copperOnly(network_graph);
					break;
				// Find the lowest average latency spanning tree
				case 3:
					findLowestAverageLatencyST(network_graph);
					break;
				// Determine whether or not the graph would remain connected if any two vertices in the graph were to fail
				case 4:
					Boolean fail = vertFailure(network_graph);
					if (fail){
						System.out.println("\nThe graph would not remain connected.");
					}
					else{
						System.out.println("\nThe graph would remain connected.");
					}
					break;
				// Quit the program
				case 5:
					quitProgram();
					break;

			}
			System.out.printf("\n\n");

		}while(choice != 5);

	}

	// Reads in file and and builds Graph
	public static NetworkGraph inputNetworkData(){
		System.out.println("Enter the name of the file containing the graph description: ");
		Scanner inputScanner = new Scanner(System.in);
		String filename = inputScanner.nextLine();

		File f = new File(filename);

		// If file is empty & doesn't not exist ask the user to try again.
		while (f.length() == 0 || !f.exists()){			
			if(!f.exists()){
				System.out.println("Error: file not found.");	
			}
			else if(f.length() == 0 ){
				System.out.println("Error: file is empty.");
			}
			System.out.println("Please try again. Enter the name of the file containing the graph description: ");
			filename = inputScanner.nextLine();
			f = new File(filename);
		}

		NetworkGraph network_graph = null;
		// if file is not empty, read in network info
		try{
			Scanner file_scanner = new Scanner(f);

			if(file_scanner.hasNextLine()){		// read in first line (number of vertices)
				int numOfVertices = Integer.parseInt(file_scanner.nextLine());
				// create grapph
				network_graph = createGraph(numOfVertices);
			

				while (file_scanner.hasNextLine()){	// read through remaining lines of file
					String edge = file_scanner.nextLine();
					String[] edge_info = edge.split(" ");

					// add each edge to the graph
					network_graph.addToGraph(edge_info);
				}
			}
		}
		catch (FileNotFoundException e){
			System.out.println("Error: file not found");
			System.exit(1);
		}

		return network_graph;

	}

	// creates graph object
	public static NetworkGraph createGraph(int numOfVertices){
		NetworkGraph network_graph = new NetworkGraph(numOfVertices);
		return network_graph;

	}

	// Prints Options and gets user choice
	public static int userMenu(){
		System.out.println("Network Analysis:");
		System.out.println("  Choose from the following options: ");
		System.out.println("	1. Find the lowest latency path between any two points.");
		System.out.println("	2. Determine where or not the graph is copper-only connected.");
		System.out.println("	3. Find the lowest average latency spanning tree for the graph.");
		System.out.println("	4. Determine whether or not the graph would remain connected if any two vertices in the graph were to fail.");
		System.out.println("	5. Quit program.");
		
		System.out.print("Enter the number corresponding to your choice: ");

			// Scan in user's choice and check for input errors
			Scanner input = new Scanner(System.in);
			int num = input.nextInt();	
			while(num < 1 || num > 5){
				System.out.println("Please enter a valid option number.");
				num = input.nextInt();
			};
			

		return num;
	}

	// Find the lowest latency path between any two points and give the bandwidth available along that path			
	public static void findLowestLatencyPath(NetworkGraph network_graph){
		// gets two vertices and checks that they are in graph
		Scanner input = new Scanner(System.in);

		System.out.println("Enter the first vertex: ");
		int first_vertex = input.nextInt();
		while(!network_graph.inGraph(first_vertex)){
			System.out.println("The vertex you entered is not in the graph. Please try again. Enter the first vertex: ");
			first_vertex = input.nextInt();
		}

		System.out.println("Enter the second vertex: ");
		int second_vertex = input.nextInt();
		while(!network_graph.inGraph(second_vertex)){
			System.out.println("The vertex you entered is not in the graph. Please try again. Enter the second vertex: ");
			second_vertex = input.nextInt();
		}

		// Create dijkstra object from graph starting at first vertex
		DijkstraSP dijk = new DijkstraSP(network_graph, first_vertex);
		// finds the path to the second vertex 
		Iterable<Edge> path = dijk.pathTo(second_vertex);

		// prints out the path and determines the minimum bandwidth of all edges in path
		boolean first = true;
		int min_bandwidth = 0;
		Edge last_edge = null;
		for(Edge e : path){
			if (first){
				System.out.print("\nPath: " +e.edge_pt1);
				first = false;
				min_bandwidth = e.bandwidth;
			}
			else{
				System.out.print("->"+e.edge_pt1);
			}

			if (e.bandwidth < min_bandwidth){
				min_bandwidth = e.bandwidth;
			}
			last_edge = e;
		}
		System.out.print("->"+last_edge.edge_pt2);

		// finds the time required to travel across path
		double lowestLP = dijk.distTo(second_vertex);
		System.out.printf("\nLowest Latency Path is: %.9f s\n",lowestLP);
		System.out.println("Bandwidth: "+min_bandwidth);
	}

	// Determine whether or not the graph is copper-only connected
	public static void copperOnly(NetworkGraph network_graph){
		Boolean cop = true;
		// bfs object
		BreadthFirstSearch bfs = new BreadthFirstSearch(network_graph, 0);
		
		// if there is not a path to every vertex using copper only return false
		for(int v = 1; v < network_graph.V(); v++){
			if(!bfs.hasPathTo(v)){
				cop = false;
				break;
			}
		}

		if(cop){
			System.out.println("\nThe graph is copper-only connected.");
		}
		else{
			System.out.println("\nThe graph is NOT copper-only connected.");
		}
	}

	// Find the lowest average latency spanning tree 
	public static void findLowestAverageLatencyST(NetworkGraph network_graph){
		// run prim alg
		PrimMST pr = new PrimMST(network_graph);
		Iterable<Edge> edges= pr.edges();

		// print out spanning tree edges
		System.out.println("\nMinimum Spanning Tree Edges:");
		for(Edge e : edges){
			System.out.println(e.toString());
		}

	}

	// Determine whether or not the graph would remain connected if any two vertices in the graph were to fail.
	public static Boolean vertFailure(NetworkGraph network_graph){
		
		for (int i = 0; i<network_graph.V(); i++){
			// creates a new temporary graph excluding one vertex
			NetworkGraph temp_graph = new NetworkGraph(network_graph.V(), true);
			// add the edges
			for (Edge e : network_graph.edges()){
				if (e.edge_pt1 != i && e.edge_pt2 != i){
					temp_graph.addEdges(e.edge_pt1, e.edge_pt2);
				}
			}
			// bioconnected object is created to check for any articulations points
			Biconnected biconnect = new Biconnected(temp_graph);
			for (int vert = 0; vert<temp_graph.V(); vert++){
				if(biconnect.isArticulation(vert)){
					return true;
				}
			}

			// repeat until done or an articulation point in found
		}

		return false;


	}

	public static void quitProgram(){
		System.out.println("Quitting program. Goodbye.");
		System.exit(0);
	}

}