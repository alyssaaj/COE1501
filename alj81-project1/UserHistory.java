import java.io.*;
import java.util.*;

public class UserHistory{		// DLB trie for user history words

	Node head;

	public class Node{

		char letter;
		Node next;			// Node next refers to Nodes at the same level
		Node down;			// Node down refers to Nodes at the next level going down (aka building the word)
		int count;			// how often the word was typed in
		
		public Node(char l) {
			letter = l;
			next = null;
			down = null;
			count = 0;
		}
	}

	public void addword(String line, int count_saved){		// addword adds a word to the DLB trie; count_saved is used for when the word is added from the file to load history
		Boolean proceed, cont;
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
					{	  											    // and there are more options to search
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


				if(i+1 == line.length() && curr.down == null){			// At the end of the word and there is no ^ node
					curr.down = new Node('^');							// Add ^ node below
					curr = curr.down;
					if (count_saved != 0){								// Deal with that word's count 
						curr.count = count_saved;
					}
					else{
						curr.count = 1;
					}
			
				}
				else if (i+1 == line.length() && curr.down != null) {				// At the end of the word and there is a linked list below

					curr = curr.down;
					cont = true;
					while (cont){
						if (curr.letter == '^'){												// Node is a  ^ ; means the word was already in the trie so just increment count
							cont = false;
							curr.count++;																	
						}
						else if(curr.letter != '^' && curr.next != null) {						// Node is not ^ but there are more nodes to the right
							curr = curr.next;
						}
						else if (curr.next == null){											// Never found a ^ no more nodes
							curr.next = new Node('^');											// Add ^ node next
							curr = curr.next;
							if (count_saved != 0){
								 curr.count = count_saved;										// Word has never been typed in previously
							}																	// Deal with that word's count
							else{
								curr.count = 1;
							}

							cont = false;

						}

	
					}
				}
				else if (i+1<line.length() && curr.down == null){				// Not at the end of the word, there isnt a linked list below
					curr.down = new Node(line.charAt(i+1));
					curr = curr.down;
				}
				else if (i+1<line.length()){									// Not at end of word, there is a linked list below
					curr = curr.down;
				}
			

		}

	}

	public void savetofile(FileWriter file_writer){								// This is used to save all the words and their counts to the 'user_history.txt' file
		Node curr = head;
		StringBuilder word_to_save = new StringBuilder();

		findwords(curr, word_to_save, file_writer);								// call recursive function to iterate through trie
	}

	public void findwords(Node curr, StringBuilder word_to_save, FileWriter file_writer){	

		if(curr.letter == '^'){													// if the curr node is at ^, write word to file
			try{	
				file_writer.write(word_to_save.toString()+" "+curr.count);		// saves word space count
				file_writer.write("\n");
				file_writer.flush();
			} catch (IOException o){
				System.out.println("Error when writing to file");
			}

			if (curr.next != null){												// sees if there are any node to the right
				findwords(curr.next, word_to_save, file_writer);	
			}

		}
		else if (curr.down != null){											// checks is down is null

			word_to_save.append(curr.letter);									// if not append letter and recursively call down
			findwords(curr.down, word_to_save, file_writer);

			word_to_save.deleteCharAt(word_to_save.length()-1);					// this is where you start to backtrack up the trie; must deleter chars to keep the spellings correct

			if (curr.next != null){
				findwords(curr.next, word_to_save, file_writer);				// check to see if there are any node to the right
			}

		}

	}

	public List<HistWord> makepredictions(StringBuilder word){	// word is what the user has typed in so far and predict is the number of predictions
																// needed from the dictionary based on the number filled by user history
		char curr_char;
		Node curr = head;
		Boolean proceed = true;
		StringBuilder predicted_word = new StringBuilder();
		List<HistWord> predictions = new ArrayList<HistWord>();

		int i = 0;
		int numCh = word.length();

		while(i < numCh){										// Attempts to the whole match the pattern typed in
			curr_char = word.charAt(i);
			if (curr_char != curr.letter && curr.next != null){		// curr node is not a match go right
				curr = curr.next;
			}
			else if (curr_char == curr.letter ){					// curr node is a match go down
				predicted_word.append(curr.letter);
				curr = curr.down;
				i++;
			}
			else if (curr.next == null){					// No words match this pattern in the User History DLB
				return predictions;
			}
		}

		// Once the entered pattern is matched then make predictions using this recursive functions
		predict(curr, predicted_word, predictions);

		return predictions;

	}

	public void predict(Node curr, StringBuilder predicted_word, List<HistWord> predictions){

		if(curr.letter == '^'){															// curr node is ^
			String predict = new String(predicted_word.toString());		
			predictions.add(new HistWord(curr.count, predict));													// add prediction to the predictions list

			if (curr.next != null){														// if there are more nodes, go next
				predict(curr.next, predicted_word, predictions);						// recursive call
			}

		}
		else if (curr.down != null){													//  if there are more nodes below, go down

			predicted_word.append(curr.letter);											// create appending letters as you go down
			predict(curr.down, predicted_word, predictions);

			predicted_word.deleteCharAt(predicted_word.length()-1);						// this is where you start to backtrack up the trie; must deleter chars to keep the spellings correct

			if (curr.next != null){														// if there are more nodes next, go right
				predict(curr.next, predicted_word, predictions);	
			}

		}

	}

}