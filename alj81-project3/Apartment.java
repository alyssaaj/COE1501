// Alyssa Jordan
// alj81@pitt.edu
// 10/29/19

import java.util.*;
import java.io.*;


public class Apartment{
	String street_address;
	String apt_number;
	String city;
	String zip;
	double price;
	double sqft;

	// Apartment Object
	public Apartment (String[] apt_info){
		this.street_address = apt_info[0];
		this.apt_number = apt_info[1];
		this.city = apt_info[2];
		this.zip = apt_info[3];
		this.price = Double.parseDouble(apt_info[4]);
		this.sqft = Double.parseDouble(apt_info[5]);
			
	}

	// gets price
	public Double getPrice(){
		return this.price;
	}

	// sets price
	public void setPrice(Double price){
		this.price = price;
	}

	// gets square footage
	public Double getSqft(){
		return this.sqft;
	}

	// sets square footage
	public void setSqft(Double sqft){
		this.sqft = sqft;
	}

	// prints Apartment info
	public void printApartment(){
		System.out.println("Apartment: "+street_address+" "+apt_number+" "+city+" "+zip+" "+price+" "+sqft);	
	}

}