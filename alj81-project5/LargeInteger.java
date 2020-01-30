import java.util.Random;
import java.math.BigInteger;
import java.util.*;
import java.lang.*;

public class LargeInteger {
	
	private final byte[] ONE = {(byte) 1};
	private final byte[] ZERO = {(byte) 0};

	private byte[] val;

	/**
	 * Construct the LargeInteger from a given byte array
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		val = b;
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
	}
	
	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/** 
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most 
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */
	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);
	
		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	public LargeInteger addOne(){
		return this.add(new LargeInteger(ONE));
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's 
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);
	
		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Implement special case of subtraction 
	 * where a value of 1 is subtracted from this
	 * @return difference of this and 1
	 */
	public LargeInteger subtractOne(){
		return this.subtract(new LargeInteger(ONE));
	}

	/**
	 * Compute the product of this and other
	 * @param other LargeInteger to multiply by this
	 * @return product of this and other
	 */
	public LargeInteger multiply(LargeInteger other) {
		//System.out.println("Multiply");
		//LargeInteger curr_product;
		LargeInteger total_product = new LargeInteger(ZERO);
		
		// Check to see if other LargeInteger is valid
		if (other == null){
			return null;
		}

		Boolean oneIsNeg = false;
		if ((this.isNegative() && !other.isNegative()) || (!this.isNegative() && other.isNegative())){
			oneIsNeg = true;
		}

		LargeInteger thisInt = new LargeInteger(this.getVal());
		if(thisInt.isNegative()){
			thisInt = thisInt.negate();
		}

		if(other.isNegative()){
			other = other.negate();
		}

		byte[] multiplicand, multiplier;
		// If operands are of different sizes, put larger first 
		if (val.length < other.length()) {
			multiplicand = other.getVal();
			multiplier = thisInt.getVal();
		}
		else {
			multiplicand = thisInt.getVal();
			multiplier = other.getVal();
		}


		LargeInteger shifted_multiplicand = new LargeInteger(multiplicand);


		// Starting at the least significant byte of the multiplicand, loop through
		for (int i = multiplier.length - 1; i >= 0; i--){
		  // Check each bit of the byte to see if its 1

			// Check 0th bit
			if(((int) multiplier[i] & 0x01) == 1){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne();	// shift multiplicand left once

			// Check 1st bit
			if(((int) multiplier[i] & 0x02) == 2){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once
			
			// Check 2nd bit
			if(((int) multiplier[i] & 0x04) == 4){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once

			// Check 3rd bit
			if(((int) multiplier[i] & 0x08) == 8){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once

			// Check 4th bit
			if(((int) multiplier[i] & 0x10) == 16){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once

			// Check 5th bit
			if(((int) multiplier[i] & 0x20) == 32){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once

			// Check 6th bit
			if(((int) multiplier[i] & 0x40) == 64){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once

			// Check 7th bit
			if(((int) multiplier[i] & 0x80) == 128){
				total_product = total_product.add(shifted_multiplicand); // add the shifted multiplicand to total product
			}
			shifted_multiplicand = shifted_multiplicand.shiftLeftOne(); // shift multiplicand left once


		}

		if (oneIsNeg){
			total_product = total_product.negate();
		}

		return total_product;
	}

	public byte[] extendByteArray(byte[] val_arr){
		byte[] newv = new byte[val_arr.length + 1];
		newv[0] = (byte) 0;
		for (int i = 0; i < val_arr.length; i++) {
			newv[i + 1] = val_arr[i];
		}
		return newv;

	}

	// Shift multiplicand to the left once
	public LargeInteger shiftLeftOne(){
		byte[] curr_val = val.clone();

		if (((int) val[0] & 0x40) == 64){
			//System.out.println("Extend");
			curr_val = extendByteArray(curr_val);
		}

		Boolean nextOne = false;
		Boolean currOne = false;
		//Boolean firstRound = true;

		//System.out.println("the current val is: " +Arrays.toString(curr_val));
		for (int i = curr_val.length - 1; i >= 0; i--){
	
			if (((int) curr_val[i] & 0x80) == 128){
				nextOne = true;
				//System.out.println("next bit");
			}
			else{
				nextOne = false;
			}

			curr_val[i] <<= 1;

			if (currOne){
				//System.out.println("add one");
				curr_val[i] += 1;
			}

			if (nextOne){
				currOne = true;
			}
			else{
				currOne = false;
			}
		}

		LargeInteger shiftedInt = new LargeInteger(curr_val);
		//System.out.println("shifted: "+Arrays.toString(shiftedInt.getVal()));

		return shiftedInt;
	}

	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another LargeInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	// This one was because off our textbook code Princeton ExtendedEuclid.java
	public LargeInteger[] XGCD(LargeInteger other) {

		// Determine if other is zero
		Boolean z = other.isZero();

		LargeInteger oneInt = new LargeInteger(ONE);
		LargeInteger zeroInt = new LargeInteger(ZERO); 
		LargeInteger[] ans = new LargeInteger[]{this, oneInt, zeroInt};
		
		// Special Case: if other is zero 
		if(z){
			return ans;
		}

		LargeInteger[] results = other.XGCD(this.mod(other));
		LargeInteger d = results[0];
		LargeInteger x = results[2];
		LargeInteger p_div_q = this.divide(other);
		LargeInteger p_div_q_mult_res2 = p_div_q.multiply(results[2]);
		LargeInteger y = results[1].subtract(p_div_q_mult_res2);


		ans = new LargeInteger[]{d, x, y};

		return ans;
	}

	public Boolean isZero(){
		byte[] valueChecking = this.getVal();
		Boolean z = true;
		for (byte b : valueChecking){
			if (b != 0){
				z = false;
			}
		}

		return z;
	}

	public Boolean isOne(){
		byte[] valueChecking = this.getVal();
		Boolean z = true;
		int len = valueChecking.length;
		for (int b=0; b<len; b++){
			if (b == len-1){
				if(valueChecking[b] != 1)
				{
					//System.out.println("in");
					z = false;
				}
			}
			else if (valueChecking[b] != 0){
				z = false;
			}
		}
		return z;
	}

	public Boolean isZeroorOneorTwo(){
		byte[] valueChecking = this.getVal();
		Boolean z = true;
		int len = valueChecking.length;
		for (int b=0; b<len; b++){
			if (b == len-1){
				if(valueChecking[b] != 2 && valueChecking[b] != 1 && valueChecking[b] != 0)
				{
					//System.out.println("in");
					z = false;
				}
			}
			else if (valueChecking[b] != 0){
				z = false;
			}
		}

		return z;
	}


	public LargeInteger mod(LargeInteger other){
		Boolean oneIsNeg = false;
		if ((this.isNegative() && !other.isNegative()) || (!this.isNegative() && other.isNegative())){
			oneIsNeg = true;
		}

		LargeInteger thisInt = new LargeInteger(this.getVal());
		if(thisInt.isNegative()){
			thisInt = thisInt.negate();
		}

		if(other.isNegative()){
			other = other.negate();
		}

		LargeInteger remainder = new LargeInteger(thisInt.getVal());
		LargeInteger prev_rem;

		do{
			prev_rem = remainder;
			remainder = remainder.subtract(other);

		}while (!remainder.isNegative() && !remainder.isZero());
		
		if (remainder.isNegative()){
			remainder = prev_rem;
		}

		if (oneIsNeg){
			remainder = remainder.negate();
		}

		return remainder;
	}

	
	public LargeInteger divide(LargeInteger other){
		//System.out.println("Dividing");

		Boolean oneIsNeg = false;
		if ((this.isNegative() && !other.isNegative()) || (!this.isNegative() && other.isNegative())){
			oneIsNeg = true;
		}

		LargeInteger thisInt = new LargeInteger(this.getVal());
		if(thisInt.isNegative()){
			thisInt = thisInt.negate();
		}

		if(other.isNegative()){
			other = other.negate();
		}

		LargeInteger remainder = new LargeInteger(thisInt.getVal());
		LargeInteger quotient = new LargeInteger(ZERO);
		
		do{
			remainder = remainder.subtract(other);
			quotient = quotient.addOne();
		}while (!remainder.isNegative() && !remainder.isZero());

		if (remainder.isNegative()){
			quotient = quotient.subtractOne();
		}

		if (oneIsNeg){
			quotient = quotient.negate();
		}

		return quotient;
	}
	
	 /**
	  * Compute the result of raising this to the power of y mod n
	  * @param y exponent to raise this to
	  * @param n modulus value to use
	  * @return this^y mod n
	  */

	public LargeInteger modularExp(LargeInteger y, LargeInteger n) {
		// YOUR CODE HERE (replace the return, too...)
		// Check to see if other LargeInteger is valid
		if (y == null || n == null){
			return null;
		}

		LargeInteger ans = new LargeInteger(ONE);

		byte[] y_val = y.getVal();


		for(int cur_byte = 0; cur_byte < y.length(); cur_byte++){

			
			byte cur_byte_arr = y_val[cur_byte];
			
			String cur_byte_arr_str = Integer.toBinaryString(cur_byte_arr);
			

			for(int cur_bit = 8; cur_bit > 0; cur_bit--){
				ans = ans.multiply(ans).mod(n);
				
				if(cur_bit == 1){
					if(((int) y_val[cur_byte] & 0x01) == 1){
						
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 2){
					if(((int) y_val[cur_byte] & 0x02) == 2){
						
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 3){
					if(((int) y_val[cur_byte] & 0x04) == 4){
						
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 4){
					if(((int) y_val[cur_byte] & 0x08) == 8){
						
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 5){
					if(((int) y_val[cur_byte] & 0x10) == 16){
						
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 6){
					if(((int) y_val[cur_byte] & 0x20) == 32){
						
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 7){
					if(((int) y_val[cur_byte] & 0x40) == 64){
					
						ans = ans.multiply(this).mod(n);
					}
				}
				else if(cur_bit == 8){
					if(((int) y_val[cur_byte] & 0x80) == 128){
					
						ans = ans.multiply(this).mod(n);
					}
				}

			}
		}

		return ans;
	 }
}