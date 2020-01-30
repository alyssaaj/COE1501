// Alyssa Jordan
// alj81@pitt.edu
// 11/12/19

import java.util.*;
import java.io.*;

// Edge Class for graph
public class Edge{
	int edge_pt1;
	int edge_pt2;
	String type_of_cable;
	int bandwidth;
	int cable_length;
	Boolean copper;
	double weight;
	double copper_speed = 230000000.0;
	double optic_speed = 200000000.0;

	// Edge Object
	public Edge(String[] edge_info){
		this.edge_pt1 = Integer.parseInt(edge_info[0]);
		this.edge_pt2 = Integer.parseInt(edge_info[1]);
		this.type_of_cable = edge_info[2];
		this.bandwidth = Integer.parseInt(edge_info[3]);
		this.cable_length = Integer.parseInt(edge_info[4]);
		if(type_of_cable.equals("copper")){
			this.copper = true;
			this.weight = (double) cable_length/copper_speed;
		}
		else{
			this.copper = false;
			this.weight = (double) cable_length/optic_speed;
		} 
				
	}

	// gets first pt or from pt
	public int edge_pt1(){
		return edge_pt1;
	}

	// gets second pt or to pt
	public int edge_pt2(){
		return edge_pt2;
	}

	// get weight or time to travel edge
	public double weight(){
		return weight;
	}


	public String toString() {
        return edge_pt1 + "->" + edge_pt2; 
    }

}