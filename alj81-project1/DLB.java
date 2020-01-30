import java.util.*;

public class DLB{			// Dictionary DLB

	Node head;

	public class Node{

		char letter;
		Node next;
		Node down;
		
		public Node(char l) {
			letter = l;
			next = null;
			down = null;
		}
	}

	public void addword(String line){

		Boolean proceed;
		char curr_char;
		Node curr;

		if(head == null) {								// Check to see if the DLB has a head
			head = new Node(line.charAt(0));
			curr = head;
		}
		else{
			curr = head;								// If there is already a head set current Node (curr) to head
		}

		for (int i=0; i<line.length(); i++){			// iterate through chars of that line
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
					curr.down = new Node('^');
					curr = curr.down;
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

	public List<StringBuilder> makepredictions(StringBuilder word){	// word is what the user has typed in so far and predict is the number of predictions
																// needed from the dictionary based on the number filled by user history
		char curr_char;
		Node curr = head;
		Boolean proceed = true;
		StringBuilder predicted_word = new StringBuilder();
		List<StringBuilder> predictions = new ArrayList<StringBuilder>();

		int i = 0;
		int numCh = word.length();

		while(i < numCh){											// match the entire pattern to the Dictionary DLB
			curr_char = word.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node doesn't match but there are more node to search through
				curr = curr.next;
			}
			else if (curr_char == curr.letter ){					// curr node matches, go on to next char in pattern
				predicted_word.append(curr.letter);
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){ 		// this pattern is not found in the Dictionary DLB
				return predictions;
			}
		}

		// Once the pattern is matched, look for predictions using recursive method predict
		predict(curr, predicted_word, predictions);

		return predictions;

	}

	public void predict(Node curr, StringBuilder predicted_word, List<StringBuilder> predictions){

		if(curr.letter == '^'){															// curr node is ^, end prediction add to predictions list
			StringBuilder predict = new StringBuilder(predicted_word.toString());
			predictions.add(predict);

			if (curr.next != null){
				predict(curr.next, predicted_word, predictions);	
			}

		}
		else if (curr.down != null){													// more nodes are below

			predicted_word.append(curr.letter);											// add current letter to predicted word
			predict(curr.down, predicted_word, predictions);

			predicted_word.deleteCharAt(predicted_word.length()-1);						// delete chars as you backtrack

			if (curr.next != null){
				predict(curr.next, predicted_word, predictions);	
			}

		}

	}



}

