// Alyssa Jordan
// alj81@pitt.edu
// 9/25/19

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;


public class ac_test
{

	public static void main(String[] args){

		
		String line;
		DLB dict = new DLB();					// create a new DLB trie and add all of the words in 'dictionary.txt' to that trie
		UserHistory hist = new UserHistory();	// creat a new DLB trie and add all of the words in 'user_history.txt' to that trie
		Boolean hasUserHistory = false;

		
		try{
			Scanner dict_scanner = new Scanner(new File("dictionary.txt"));		// read in dictionary file

			while(dict_scanner.hasNextLine()) {			// read in line by line
				line = dict_scanner.nextLine();
				dict.addword(line);						// add word to dictionary
			}

			File file = new File("user_history.txt");		// read in user_history file
			FileWriter file_writer;

			if (!file.createNewFile()){						// if 'user_history.txt' exists
				if (file.length() != 0)						// Makes sure user_history actually has words in it
				{
					hasUserHistory = true;
				}
				Scanner hist_scanner = new Scanner(file);
				while(hist_scanner.hasNextLine()) {
					line = hist_scanner.nextLine();				// each line in the user_history file is the word space the count
					String[] line_divided = line.split(" ");
					hist.addword(line_divided[0], Integer.parseInt(line_divided[1]));	// send in word and count
				}
				
			}

			file_writer = new FileWriter(file, false);

		// prompt the user to start typing a word

				Scanner scanner = new Scanner(System.in);
				StringBuilder word = new StringBuilder();
				long start_time, end_time; 
				double total_time, sum_total_time = 0, average_time;
				int ch_predicted = 0;

				System.out.print("Enter your first character: ");
				
				// accept a single character at a time, each time followed by 'Enter'
				char ch;
				String input = scanner.next();
				if (input.length() == 1){
					ch = input.charAt(0);
				}
				else{
					do {
						System.out.println("\nPlease only type one character in at time followed by an Enter.");
						System.out.print("Enter your first character: ");
						input = scanner.next();
					} while(input.length() != 1);
					ch = input.charAt(0);
				}

				while(ch != '!'){												// while the character each isn't ! continue
					word.append(ch);
					List<String> displayed_words = new ArrayList<String>();		// List of the words to display

					
					start_time = System.nanoTime();			// Start time
					ch_predicted++;

					if (hasUserHistory){												// If the user has a userhistory file with words in it, make predictions from this DLB
						List<HistWord> historywords = hist.makepredictions(word);		// historywords is a list of HistWord objects
																						// HistWord objects are used in order to sort the predictions by count (most frequently used) and remember word associated with count
						historywords.sort(Comparator.comparing(HistWord::getCount).reversed());		// This sorts the HistWord objects in descending count
						

						int user_words;

						if(historywords.size()<5){							// this determines if there are enough words in the user predictions
							user_words = historywords.size();
						}
						else{
							user_words = 5;
						}

						for (int i = 0; i < user_words; i++){
							displayed_words.add(historywords.get(i).getWord());		// adds the (5 or less) most frequent user_history words to the words displayed list
							
						}
						
					}
					

					if (displayed_words.size() < 5){											// if the displayed word list isn't filled with 5 words already, continue to dictionary predictions
						List<StringBuilder> predictedwords = dict.makepredictions(word);

						int words_needed = 5 - displayed_words.size();						// determines how many words are needed	
						int dict_words;

						if (predictedwords.size() < words_needed) {
							dict_words = predictedwords.size();
						}
						else{
							dict_words = words_needed;
						}

						int i = 0, j = 0;
						while (i < dict_words) {
							if (!displayed_words.contains(predictedwords.get(j).toString())) {		// if the dictionary predicted words are already in the user history list dont repeat them

								displayed_words.add(predictedwords.get(j).toString());		// add the dictionary words to the displayed words
								i++;
							}
							j++;
						}
						
						
					}

					end_time = System.nanoTime();		// end prediction time

					if (displayed_words.isEmpty()){				// no predictions from user_history or dictionary
						System.out.println("Unfortunately, no predictions were found. Feel free to continue typing your word (one char at a time) and press $ when done.");
					}
					else {								// display predictions
						// After each character, present the user with the list of predictions (5) of the word that they are trying to type
						System.out.println("Predictions: ");
						for (int i = 0; i < displayed_words.size(); i++){
							System.out.print("("+ (i+1) +") "+ displayed_words.get(i) +"    ");
						}
					System.out.println(" ");
					}

				
					

					total_time = (double)end_time - (double)start_time;
					total_time = total_time/1000000000;
					sum_total_time += total_time;
					System.out.println(" ");
					System.out.printf("( %.6f", (double)total_time); 
					System.out.println(" s)");


					
					// Continue entering characters
					System.out.println(" ");
					System.out.print("Enter the next character: ");
					input = scanner.next();
					if (input.length() == 1){
						ch = input.charAt(0);
					}
					else{
						do {
							System.out.println("\nPlease only type one character in at time followed by an Enter.");
							System.out.print("Enter your next character: ");
							input = scanner.next();
						} while(input.length() != 1);
						ch = input.charAt(0);
					}
					
					// if the user enters 1-5, the word associated with the number is choosen or if $ is entered, the current word is added to the dictionary
					if (ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '$'){
						if (ch == '1') {
							System.out.println(" WORD COMPLETED: " +displayed_words.get(0));
							hist.addword(displayed_words.get(0).toString(), 0);
							hasUserHistory = true;
						}
						else if (ch == '2') {
							System.out.println(" WORD COMPLETED: " +displayed_words.get(1));
							hist.addword(displayed_words.get(1).toString(), 0);
							hasUserHistory = true;
						}
						else if (ch == '3') {
							System.out.println(" WORD COMPLETED: " +displayed_words.get(2));
							hist.addword(displayed_words.get(2).toString(), 0);
							hasUserHistory = true;
						}
						else if (ch == '4') {
							System.out.println(" WORD COMPLETED: " +displayed_words.get(3));
							hist.addword(displayed_words.get(3).toString(), 0);
							hasUserHistory = true;
						}
						else if (ch == '5') {
							System.out.println(" WORD COMPLETED: " +displayed_words.get(4));
							hist.addword(displayed_words.get(4).toString(), 0);
							hasUserHistory = true;
						}
						else if (ch == '$') {
							System.out.println(" WORD COMPLETED: "+ word);
							hist.addword(word.toString(), 0);
							hasUserHistory = true;
						}
						
						

						word = new StringBuilder();		// clears stringbuilder
						System.out.println(" ");
						System.out.print("Enter your first character: ");
						input = scanner.next();
						if (input.length() == 1){
							ch = input.charAt(0);
						}
						else{
							do {
								System.out.println("\nPlease only type one character in at time followed by an Enter.");
								System.out.print("Enter your first character: ");
								input = scanner.next();
							} while(input.length() != 1);
							ch = input.charAt(0);
						}
						
					}



				}

				average_time = sum_total_time/ch_predicted;			// determines the average time to make predicts for each char typed in
				
				System.out.println(" ");
				System.out.printf("Average time: (%.6f", (double)average_time); 
				System.out.println(" s)");
				System.out.println("Bye!");
				hist.savetofile(file_writer);

				file_writer.close();

		} catch (IOException e) {
			System.out.println("Error: Dictionary File does not exist");
		}

	}
}


