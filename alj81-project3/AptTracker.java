// Alyssa Jordan
// alj81@pitt.edu
// 10/29/19

import java.util.*;
import java.io.*;


public class AptTracker
{
	public static void main(String[] args){

		DLB rent_sqftDLB = new DLB();
		DLB cityDLB = new DLB();
		IndexPQ rentPQ = new IndexPQ(1500, true);	// Rent Priority Queue
		IndexPQ sqftPQ = new IndexPQ(1500, false);	// Square Footage Priority Queue
		
		inputAptData(rent_sqftDLB, rentPQ, sqftPQ, cityDLB);

		String response;

		do{
			// Menu-based User Interface Driver Program
			System.out.println("Apartment Tracker:");
			System.out.println("  Choose from the following options: ");
			System.out.println("	1. Add an apartment");
			System.out.println("	2. Update an apartment");
			System.out.println("	3. Remove a specific apartment from consideration");
			System.out.println("	4. Retrieve the lowest rent apartment");
			System.out.println("	5. Retrieve the highest square footage apartment");
			System.out.println("	6. Retrieve the lowest rent apartment by city");
			System.out.println("	7. Retrieve the highest square footage apartment by city");
			System.out.print("Enter the number corresponding to your choice: ");

			// Scan in user's choice and check for input errors
			Scanner input = new Scanner(System.in);
			int num = input.nextInt();	
			while(num < 1 || num > 7){
				System.out.println("Please enter a valid option number.");
				num = input.nextInt();
			};
			input.nextLine();	// Fixes weirdness of .nextInt

			// Depending on user's choice run on of the following functions
			switch(num)
			{
				case 1:		// Add an apartment
					addApt(input, rent_sqftDLB, rentPQ, sqftPQ, cityDLB);
					break;
				case 2:		// Update an apartment
					updateApt(input, rent_sqftDLB, rentPQ, sqftPQ, cityDLB);
					break;
				case 3:		// Remove a specific apartment
					removeApt(input, rent_sqftDLB, rentPQ, sqftPQ, cityDLB);
					break;
				case 4:		// Retrieve lowest rent apartment
					lowRent(rentPQ);
					break;
				case 5:		// Retrieve highest square footage apartment
					highSqFt(sqftPQ);
					break;
				case 6:		// Retrieve lowest rent apartment by city
					lowRentByCity(input, cityDLB);
					break;
				case 7:		// Retrieve highest square footage apartment by city
					highSqFtByCity(input, cityDLB);
					break;
			}

			System.out.println(" ");
			
			// Checks if user wants to keep using the Apt Tracker
	        System.out.println("Would you like to continue? (Y/N)");
	        response = input.nextLine().toUpperCase();
			while (!response.equals("Y") && !response.equals("N")){
				System.out.println("Incorrect Response. Please enter Y or N. Would you like to continue? (Y/N)");
				response = input.nextLine().toUpperCase();
			}

			// Comment out to display all the apartments in the Min Priority Order for Rent or Max Priority Order for SqFt
			if (response.equals("N")){
				System.out.println(" ");
			
				System.out.println("LOWEST RENT PQ");
				while (!rentPQ.isEmpty()) {
		            int i = rentPQ.delHighestPriority();

		        }
				System.out.println(" ");
				System.out.println("HIGHEST SQFT PQ");
				while (!sqftPQ.isEmpty()) {
		            int i = sqftPQ.delHighestPriority();

		        }
			}
	    } while(response.equals("Y"));


	}

	// Inputs Starter File Data
	public static void inputAptData(DLB rent_sqftDLB, IndexPQ rentPQ, IndexPQ sqftPQ, DLB cityDLB){
		File f = new File("apartments.txt");

		// If file is empty, exit
		if (f.length() == 0){			
			System.out.println("Error: apartments.txt is empty.");
			System.exit(-1);
		}

		// if file is not empty, read in apartments
		try{
			Scanner file_scanner = new Scanner(f);

			if(file_scanner.hasNextLine()){		// skip header line
				String header = file_scanner.nextLine();
			}

			while (file_scanner.hasNextLine()){	// read through remaining lines of file
				String apt = file_scanner.nextLine();
				String[] apt_info = apt.split(":"); 
				String partial_info = apt_info[0]+apt_info[1]+apt_info[3]; // street address, apartment number, and zip

				// Add apartment to regular DLB and then city DLB
 				Boolean already_added = rent_sqftDLB.addApartment(partial_info, apt_info, rentPQ, sqftPQ);
 				if (!already_added){
 					cityDLB.addAptToCity(apt_info[2], apt_info);
 				}
			}

		}
		catch (FileNotFoundException e){
			System.out.println("Error: apartments.txt not found");
		}

	}

	// Adds apartments to tracker after initial loading of apartment data
	public static void addApt(Scanner input, DLB rent_sqftDLB, IndexPQ rentPQ, IndexPQ sqftPQ, DLB cityDLB){
		// Gather all apartment attributes
		String[] apt_info = new String[6];
		System.out.println("ADD AN APARTMENT");

		System.out.println("Enter the Street Address: ");
		String street_address = input.nextLine();
		apt_info[0] = street_address;

		System.out.println("Enter the Apartment Number: ");
		String apt_number = input.nextLine();
		apt_info[1] = apt_number;

		System.out.println("Enter the City: ");
		String city = input.nextLine();
		apt_info[2] = city;

		System.out.println("Enter the zipcode: ");
		String zip = input.nextLine();
		apt_info[3] = zip;

		System.out.println("Enter the rent: ");
		String price = input.nextLine();
		apt_info[4] = price;

		// verifies that the price is a number
		try {
			Double.parseDouble(apt_info[4]);
			System.out.println("Enter the square footage: ");
			String sqft = input.nextLine();
			apt_info[5] = sqft;

			// verifies that the square footage is a number
			try {
				Double.parseDouble(apt_info[5]);
				String partial_info = street_address+apt_number+zip;

				// adds apartment to regular DLB PQs
				Boolean already_added = rent_sqftDLB.addApartment(partial_info, apt_info, rentPQ, sqftPQ);
				// add apartment to city DLB PQs
				if (!already_added){
					cityDLB.addAptToCity(apt_info[2], apt_info);
				}

			} catch (NumberFormatException e){
				System.out.println("That is an invalid square footage.");
			}
		} catch (NumberFormatException e){
			System.out.println("That is an invalid price.");
		}


	}

	// Updates the price of an apartment in the apartment tracker
	public static void updateApt(Scanner input, DLB rent_sqftDLB, IndexPQ rentPQ, IndexPQ sqftPQ, DLB cityDLB){
		// Enter apartment info needed to update an apartment
		System.out.println("UPDATE AN APARTMENT");
		System.out.println("Enter the Street Address: ");
		String street_address = input.nextLine();
		System.out.println("Enter the Apartment Number: ");
		String apt_number = input.nextLine();
		System.out.println("Enter the zipcode: ");
		String zip = input.nextLine();
		System.out.println(" ");
		String partial_info = street_address+apt_number+zip;

		// As the user if they would like to update the rent for the apartment and check for errors
		System.out.println("Would you like to update the rent for this apartment? (Y/N)");
		String response = input.nextLine().toUpperCase();
		while (!response.equals("Y") && !response.equals("N")){
			System.out.println("Incorrect Response. Please enter Y or N. Would you like to update the rent for this apartment? (Y/N)");
			response = input.nextLine().toUpperCase();
		}

		if (response.equals("Y")){
			System.out.println("Please enter the new rent:");
			String new_price = input.nextLine();
			// checks if price is a number
			try {
				Double.parseDouble(new_price);

				// update price in regular DLB PQs
				String[] update_info = rent_sqftDLB.updateApartment(partial_info, new_price, rentPQ, sqftPQ);
				
				// if the apartment entered isn't in the rent_sqftDLB, it doesn't exist and we shouldn't
				// continue, so check if the output of update_info is null which lets us know the apartment 
				// doesn't exist
				if(update_info[0] != null && update_info[1] != null){
					String city = update_info[0];
					int curr_index = Integer.parseInt(update_info[1]);
					// update price in city specific DLB PQs
					cityDLB.updateCity(city, new_price, curr_index);
				}

			} catch (NumberFormatException e){
				System.out.println("That is an invalid price.");
			}

		
		}

	}

	// Removes apartment from apartment tracker
	public static void removeApt(Scanner input, DLB rent_sqftDLB, IndexPQ rentPQ, IndexPQ sqftPQ, DLB cityDLB){
		// Enter apartment infor needed to remove an apartment
		System.out.println("REMOVE AN APARTMENT");
		System.out.println("Enter the Street Address: ");
		String street_address = input.nextLine();
		System.out.println("Enter the Apartment Number: ");
		String apt_number = input.nextLine();
		System.out.println("Enter the zipcode: ");
		String zip = input.nextLine();

		String partial_info = street_address+apt_number+zip;

		// deletes apartment from regular DLB PQs
		String[] delete_info = rent_sqftDLB.removeApartment(partial_info, rentPQ, sqftPQ);

		// if the apartment doesn't exist the delete_info will be null so check if its null.
		if(delete_info[0] != null && delete_info[1] != null){
			String city = delete_info[0];
			int curr_index = Integer.parseInt(delete_info[1]);
			// delete apartment from city DLB PQs
			cityDLB.removeCity(city, curr_index);
		}

	}

	// retrieves the lowest rent apartment
	public static void lowRent(IndexPQ rentPQ){
		Apartment lowestR = rentPQ.highestPriorityKey();	// get highest priority key
		if (lowestR != null){
	    	System.out.println("The lowest rent apartment is: ");
	    	lowestR.printApartment();						// print apartment info
	    }
	}

	// retrieves the highest square footage apartment
	public static void highSqFt(IndexPQ sqftPQ){
		Apartment highestS = sqftPQ.highestPriorityKey();	// get highest priority key
		if (highestS != null){
			System.out.println("The highest square footage apartment is: ");
	    	highestS.printApartment();						// print apartment info
	    }
	}

	// retrieves the lowest rent by city
	public static void lowRentByCity(Scanner input, DLB cityDLB){
		System.out.println("Please enter a city: ");
		String city = input.nextLine();
		System.out.println("You entered: "+city);
		System.out.println(" ");

		// gets the rent and square footage pqs for a city
		IndexPQ[] pqs = cityDLB.getCityPQ(city);
		
		if (pqs[0] != null){		// check that the rent pq isn't null
			Apartment lowestCity = pqs[0].highestPriorityKey();		// gets highest priority
			System.out.println("The lowest rent apartment in "+city+" is: ");
			lowestCity.printApartment();
		}

	}

	// retrieves the highest square footage by city
	public static void highSqFtByCity(Scanner input, DLB cityDLB){
		System.out.println("Please enter a city: ");
		String city = input.nextLine();
		System.out.println("You entered: "+city);
		System.out.println(" ");

		// gets the rent and square footage pqs for a city
		IndexPQ[] pqs = cityDLB.getCityPQ(city);
		
		if (pqs[1] != null){		// check that the square footage pq isn't null
			System.out.println("The highest square footage apartment in "+city+" is: ");
			Apartment highestCity = pqs[1].highestPriorityKey();	// gets highest priority
			highestCity.printApartment();
		}
	}
}