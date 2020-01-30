import java.util.*;

public class DLB{			// Apartment DLB

	Node head;
	int ind=0;

	public class Node{

		char letter;
		Node next;
		Node down;
		Apartment apt;
		int index;
		IndexPQ rent_pqueue;
		IndexPQ sqft_pqueue;
		
		// Node Object
		public Node(char l) {
			letter = l;
			next = null;
			down = null;
			apt = null;
			index = -1;
			rent_pqueue = null;
			sqft_pqueue = null;
		}
		public Node(int ii, Apartment a) {		// used as end nodes for rent_sqft DLB
			letter = 0;
			next = null;
			down = null;
			apt = a;
			index = ii;
			rent_pqueue = null;
			sqft_pqueue = null;
		}
		public Node(IndexPQ rPQ, IndexPQ sPQ) {		// used as end nodes for cityDLB
			letter = 0;
			next = null;
			down = null;
			apt = null;
			index = -1;
			rent_pqueue = rPQ;
			sqft_pqueue = sPQ;
		}
	}

	// Add Apartment to Regular DLB and then the rent and square footage PQs
	public Boolean addApartment(String line, String[] a, IndexPQ rentPQ, IndexPQ sqftPQ){

		Boolean proceed;
		char curr_char;
		Node curr;
		Boolean already_added = false;

		if(head == null) {								// Check to see if the DLB has a head
			head = new Node(line.charAt(0));
			curr = head;
		}
		else{
			curr = head;								// If there is already a head set current Node (curr) to head
		}

		for (int i=0; i<line.length(); i++){			// iterate through each char of a concatenation of the street address, apartment numver, and zipcode 
			proceed = true;		
			curr_char = line.charAt(i);					// set current char (curr_char) to char of line at position i
			
				while(proceed) {
					if (curr_char != curr.letter && curr.next != null) 	// The current char in the line does not match the current Node letter
					{													// and there are more options to search
						curr = curr.next;								// Move on to next Node
						
					}														
					else if(curr_char == curr.letter) {					// The current char in the line matches the current Node letter
						proceed = false;								// Stop searching through next
					}
					else if (curr.next == null) {						// The current char is not found
						proceed = false;								// Stop searching through next
						curr.next = new Node(curr_char);				// Create a node with char 
						curr = curr.next;
					}

							
				}

				// if you found the char or got to the end of the horizontal linked list and add char to node
				// Proceed down the Node to look for next char in line if there are more chars in line
				if(i+1 == line.length() && curr.down == null){
					curr.down = new Node('^');
					curr = curr.down;
					Apartment key = new Apartment(a);
					curr.down = new Node(ind, key);
					rentPQ.insert(ind, key);			// insert apartment at the index for both PQS
					sqftPQ.insert(ind,key);
					ind++;
				}
				else if (i+1 == line.length() && curr.down != null){
					System.out.println("You already have that apartment in the apartment tracker please update the apartment instead");
					already_added = true;
				}
				else if (i+1<line.length() && curr.down == null){
					curr.down = new Node(line.charAt(i+1));
					curr = curr.down;
				}
				else if (i+1<line.length()){
					curr = curr.down;
				}
			

		}

		return already_added;

	}

	// Adds apartment to the City DLB and its specific PQs
	public void addAptToCity(String line, String[] a){

		Boolean proceed;
		char curr_char;
		Node curr;
		IndexPQ rPQ, sPQ;

		if(head == null) {								// Check to see if the DLB has a head
			head = new Node(line.charAt(0));
			curr = head;
		}
		else{
			curr = head;								// If there is already a head set current Node (curr) to head
		}

		for (int i=0; i<line.length(); i++){			// iterate through each char of a concatenation of the street address, apartment numver, and zipcode 
			proceed = true;		
			curr_char = line.charAt(i);					// set current char (curr_char) to char of line at position i
			
				while(proceed) {
					if (curr_char != curr.letter && curr.next != null) 	// The current char in the line does not match the current Node letter
					{													// and there are more options to search
						curr = curr.next;								// Move on to next Node
						
					}														
					else if(curr_char == curr.letter) {					// The current char in the line matches the current Node letter
						proceed = false;								// Stop searching through next
					}
					else if (curr.next == null) {						// The current char is not found
						proceed = false;								// Stop searching through next
						curr.next = new Node(curr_char);				// Create a node with char 
						curr = curr.next;
					}

							
				}

				// if you found the char or got to the end of the horizontal linked list and add char to node
				// Proceed down the Node to look for next char in line if there are more chars in line
				if(i+1 == line.length()){
					if (curr.down == null){
						curr.down = new Node('^');
					}
					curr = curr.down;
					if (curr.down == null){
						rPQ = new IndexPQ(1500, true);
						sPQ = new IndexPQ(1500, false);
						Apartment key = new Apartment(a);
						curr.down = new Node(rPQ, sPQ);
						rPQ.insert(ind, key);
						sPQ.insert(ind, key);
					}
					else{
						curr = curr.down;
						rPQ = curr.rent_pqueue;
						sPQ = curr.sqft_pqueue;
						Apartment key = new Apartment(a);
						rPQ.insert(ind, key);
						sPQ.insert(ind, key);

					}
					ind++;
				}
				else if (i+1<line.length() && curr.down == null){
					curr.down = new Node(line.charAt(i+1));
					curr = curr.down;
				}
				else if (i+1<line.length()){
					curr = curr.down;
				}	
		}
	}

	// gets the priority queues for a specifc city
	public IndexPQ[] getCityPQ(String city){

		char curr_char;
		Node curr = head;
		IndexPQ[] pqs = new IndexPQ[2];

		int i = 0;
		int numCh = city.length();

		while(i < numCh){											// match the entire pattern to the DLB
			curr_char = city.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node doesn't match but there are more node to search through
				curr = curr.next;
			}
			else if (curr_char == curr.letter){					// curr node matches, go on to next char in pattern
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){ 		// this pattern is not found in the Dictionary DLB
				System.out.println("You have no apartments listed for the city you entered.");
				break;
			}
		}

		if (curr.letter == '^'){
			curr = curr.down;
			pqs[0] = curr.rent_pqueue;
			pqs[1] = curr.sqft_pqueue;

		}
		return pqs;		// return the two priority queues for that city
	}

	// update apartment in regular DLB and the PQs
	public String[] updateApartment(String apartment, String price, IndexPQ rentPQ, IndexPQ sqftPQ){	
																
		char curr_char;
		Node curr = head;
		String[] update_info = new String[2];

		int i = 0;
		int numCh = apartment.length();

		while(i < numCh){											// match the entire pattern to the DLB
			curr_char = apartment.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node doesn't match but there are more node to search through
				curr = curr.next;
			}
			else if (curr_char == curr.letter){					// curr node matches, go on to next char in pattern
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){ 		// this pattern is not found in the Dictionary DLB
				System.out.println("The apartment you wanted to update does not exist in your apartment tracker.");
				break;
			}
		}

		if (curr.letter == '^'){
			curr = curr.down;
			System.out.println("The apartment's new price is: "+price);
			curr.apt.setPrice(Double.parseDouble(price));	// updates the apartment's price
			int curr_index = curr.index;
			rentPQ.changeKey(curr_index, curr.apt);			// finds the apartment's new position in the PQs
			sqftPQ.changeKey(curr_index, curr.apt);
			update_info[0] = curr.apt.city;
			update_info[1] = Integer.toString(curr_index);
			
		}

		return update_info;	// return the city and index of the apartment for the city specific PQ
	}

	// updates the price in the city specific PQs
	public void updateCity(String city, String price, int curr_index){															
		char curr_char;
		Node curr = head;

		int i = 0;
		int numCh = city.length();

		while(i < numCh){											// match the entire pattern to the DLB
			curr_char = city.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node doesn't match but there are more node to search through
				curr = curr.next;
			}
			else if (curr_char == curr.letter){					// curr node matches, go on to next char in pattern
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){ 		// this pattern is not found in the Dictionary DLB
				break;
			}
		}

		if (curr.letter == '^'){
			curr = curr.down;
			IndexPQ rPQ = curr.rent_pqueue;
			IndexPQ sPQ = curr.sqft_pqueue;
			Apartment curr_apt = rPQ.getAptByIndex(curr_index);	// gets the apartment by the index
			curr_apt.setPrice(Double.parseDouble(price));	// sets the apartment to new price
			rPQ.changeKey(curr_index, curr_apt);		// finds the apartment's new position in the PQs
			sPQ.changeKey(curr_index, curr_apt);
			
		}


	}

	// remove apartment from regular DLB and the PQs
	public String[] removeApartment(String apartment, IndexPQ rentPQ, IndexPQ sqftPQ){	
		char curr_char;
		Node curr = head;
		String[] delete_info = new String[2];

		int i = 0;
		int numCh = apartment.length();

		while(i < numCh){											// match the entire pattern to the DLB
			curr_char = apartment.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node doesn't match but there are more node to search through
				curr = curr.next;
			}
			else if (curr_char == curr.letter){					// curr node matches, go on to next char in pattern
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){ 		// this pattern is not found in the Dictionary DLB
				System.out.println("The apartment you wanted to delete does not exist in your apartment tracker.");
				break;
			}
		}

		if (curr.letter == '^'){
			curr = curr.down;
			int curr_index = curr.index;
			Apartment curr_a = curr.apt;
			String city = curr_a.city;
			rentPQ.delete(curr_index);	// delete apartments from pqs
			sqftPQ.delete(curr_index);
			delete_info[0] = city;
			delete_info[1] = Integer.toString(curr_index);

			
		}
		return delete_info;	// return the city and index of apartment that needs to be deleted

	}

	// delete apartment from city DLB and its specific PQs
	public void removeCity(String city, int curr_index){
		char curr_char;
		Node curr = head;

		int i = 0;
		int numCh = city.length();

		while(i < numCh){											// match the entire pattern to the DLB
			curr_char = city.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node doesn't match but there are more node to search through
				curr = curr.next;
			}
			else if (curr_char == curr.letter){					// curr node matches, go on to next char in pattern
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){ 		// this pattern is not found in the DLB
				break;
			}
		}

		if (curr.letter == '^'){
			curr = curr.down;
			IndexPQ rPQ = curr.rent_pqueue;
			IndexPQ sPQ = curr.sqft_pqueue;
			rPQ.delete(curr_index);		// delete aparments from pqs
			sPQ.delete(curr_index);

		}
	}


}

