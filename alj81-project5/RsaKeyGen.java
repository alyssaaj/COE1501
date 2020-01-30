// Alyssa Jordan
// Project 5
// 12/7/19

import java.io.ObjectOutputStream;
import java.util.Random;
import java.io.*;
import java.util.*;
import java.io.IOException;
import java.math.BigInteger;



// Key Generation Program
public class RsaKeyGen{
	
	public static void main(String args[]) {
		byte[] ONE = {(byte) 1};
		byte[] ZERO = {(byte) 0};

		Random rand = new Random();

		
	// Pick p and q to be random primes of an appropriate size to generate a 512-bit key
		//LargeInteger p = new LargeInteger(3, rand); 	
		//LargeInteger q = new LargeInteger(2, rand);
		LargeInteger p = new LargeInteger(256, rand); 	
		LargeInteger q = new LargeInteger(256, rand);
		System.out.println("p: "+ Arrays.toString(p.getVal()));
		System.out.println("q: "+ Arrays.toString(q.getVal()));
	// Calculate n as p*q
		LargeInteger n = p.multiply(q);
		System.out.println("n: "+ Arrays.toString(n.getVal()));
		
	// Calculate phi(n) as (p-1)*(q-1)
		LargeInteger p_1 = p.subtractOne();
		LargeInteger q_1 = q.subtractOne();
		System.out.println("p-1: "+ Arrays.toString(p_1.getVal()));
		System.out.println("q-1: "+ Arrays.toString(q_1.getVal()));
		
		LargeInteger phi_n = p_1.multiply(q_1);
		System.out.println("phi(n): "+ Arrays.toString(phi_n.getVal()));

	// Choose an e such that 1 < e < phi(n) and gcd(e, phi(n)) = 1
	// (e must not share a factor with phi(n))
		byte[] e_arr = phi_n.getVal();
		LargeInteger ee = new LargeInteger(e_arr);

		LargeInteger[] shouldBeOne;
		

		do{	
			ee = ee.subtractOne();
			//System.out.println("e: "+ Arrays.toString(ee.getVal()));

			shouldBeOne = ee.XGCD(phi_n);
			//System.out.println("gcd: "+ Arrays.toString(shouldBeOne[0].getVal()));

		} while (!shouldBeOne[0].isOne() && !ee.isNegative() && !ee.isZeroorOneorTwo());

		System.out.println("e: "+ Arrays.toString(ee.getVal()));
		System.out.println("gcd: "+ Arrays.toString(shouldBeOne[0].getVal()));

	// Determine d such that d = e^-1 mod phi(n)
		byte[] NEG_ONE = {(byte) -1};
		LargeInteger negativeOne = new LargeInteger(NEG_ONE);
		LargeInteger d = ee.modularExp(negativeOne, phi_n);
		System.out.println("d: "+ Arrays.toString(d.getVal()));

		List<byte[]> pubkey = new ArrayList<byte[]>();
		pubkey.add(ee.getVal());
		pubkey.add(n.getVal());

	// save e and n to pubkey.rsa
		try {  
			ObjectOutputStream pubOS = new ObjectOutputStream(new FileOutputStream("pubkey.rsa"));
			pubOS.writeObject(pubkey);
			pubOS.close();

		} catch (IOException e) {
			System.out.println("Error: There was a problem creating pubkey.rsa");
		}

		List<byte[]> privkey = new ArrayList<byte[]>();
		privkey.add(d.getVal());
		privkey.add(n.getVal());

	// save d and n to privkey.rsa
		try {  
			ObjectOutputStream privOS = new ObjectOutputStream(new FileOutputStream("privkey.rsa"));
			privOS.writeObject(privkey);
			privOS.close();

		} catch (IOException e) {
			System.out.println("Error: There was a problem creating privkey.rsa");
		}
		
	}
}