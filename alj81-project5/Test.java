// Alyssa Jordan
// Project 5 - Test Main
// 12/7/19

import java.io.ObjectOutputStream;
import java.util.Random;
import java.io.*;
import java.util.*;
import java.math.BigInteger;


// Test Program
public class Test{
	
	public static void main(String args[]) {
		Random rand = new Random();

		System.out.println("---TESTING MULTIPLY---");
		
		System.out.println("----------------------Test 1-------------------");
		byte[] pbyte1 = new byte[1];
		byte[] qbyte1 = new byte[1];
		
		pbyte1[0] = (byte) 20;	// 0001 0100
		qbyte1[0] = (byte) 9; 	// 0000 1001
		LargeInteger p1 = new LargeInteger(pbyte1); 	// 20
		LargeInteger q1 = new LargeInteger(qbyte1);	// 9
		System.out.println("p: "+ Arrays.toString(p1.getVal()));
		System.out.println("q: "+ Arrays.toString(q1.getVal()));


		LargeInteger n1 = p1.multiply(q1);	// 180
		// 10110100
		// [-76]
		System.out.println("\nn: "+ Arrays.toString(n1.getVal()));
		System.out.println("	"+ new BigInteger(n1.getVal()).toString());


		System.out.println("----------------------Test 2-------------------");
		byte[] pbyte2 = new byte[1];
		byte[] qbyte2 = new byte[1];
		
		pbyte2[0] = (byte) 100;	// 0110 0100
		qbyte2[0] = (byte) 30; 	// 0001 1110
		LargeInteger p2 = new LargeInteger(pbyte2); 	// 100
		LargeInteger q2 = new LargeInteger(qbyte2);		// 30
		System.out.println("p: "+ Arrays.toString(p2.getVal()));
		System.out.println("q: "+ Arrays.toString(q2.getVal()));


		LargeInteger n2 = p2.multiply(q2);	// 3000
		// 00001011 10111000
		// [ 11,			-72]
		System.out.println("\nn: "+ Arrays.toString(n2.getVal()));
		System.out.println("	"+ new BigInteger(n2.getVal()).toString());
		
		System.out.println("----------------------Test 3-------------------");
		byte[] pbyte = new byte[2];
		byte[] qbyte = new byte[2];
		
		pbyte[0] = (byte) 5;	// 0000 0101
		pbyte[1] = (byte) 4;	// 0000 0100
		qbyte[0] = (byte) 6; 	// 0000 0110
		qbyte[1] = (byte) 47; 	// 0010 1111
		LargeInteger p = new LargeInteger(pbyte); 	// 1284	
		LargeInteger q = new LargeInteger(qbyte);	// 1583
		System.out.println("p: "+ Arrays.toString(p.getVal()));
		System.out.println("q: "+ Arrays.toString(q.getVal()));


		LargeInteger n = p.multiply(q);	// 2032572
		// 00011111 00000011 10111100
		// [	31,		3,		-68]
		System.out.println("\nn: "+ Arrays.toString(n.getVal()));
		System.out.println("	"+ new BigInteger(n.getVal()).toString());

		System.out.println("-----------testing 256 bit numbers -------------------");
		LargeInteger ppp = new LargeInteger(256, rand);
		LargeInteger qqq = new LargeInteger(256, rand);
		System.out.println("p: "+ Arrays.toString(ppp.getVal()));
		System.out.println("	"+ new BigInteger(ppp.getVal()).toString());
		System.out.println("q: "+ Arrays.toString(qqq.getVal()));
		System.out.println("	"+ new BigInteger(qqq.getVal()).toString());

		LargeInteger nnn = ppp.multiply(qqq);	// 2032572
		// 00011111 00000011 10111100
		// [	31,		3,		-68]
		System.out.println("\nn: "+ Arrays.toString(nnn.getVal()));
		System.out.println("ans:	"+ new BigInteger(nnn.getVal()).toString());

		BigInteger ppp_BI = new BigInteger(ppp.getVal());
		BigInteger qqq_BI = new BigInteger(qqq.getVal());
		BigInteger nnn_actually = ppp_BI.multiply(qqq_BI);
		System.out.println("correct ans:"+nnn_actually.toString());
		
		System.out.println("---TESTING DIVISON AND MOD---");

		
		byte[] pb_d1 = new byte[2];
		byte[] qb_d1 = new byte[2];

		System.out.println(" Test 1:");
		// 3000 / 300 
		pb_d1[0] = (byte) 11;
		pb_d1[1] = (byte) -72;
		qb_d1[0] = (byte) 1;
		qb_d1[1] = (byte) 44;

		LargeInteger p_d1 = new LargeInteger(pb_d1);
		LargeInteger q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		LargeInteger quot_d1 = p_d1.divide(q_d1);
		LargeInteger rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());

		System.out.println("Test 2:");
		//3000 / 10
		pb_d1 = new byte[2];
		qb_d1 = new byte[1];

		pb_d1[0] = (byte) 11;
		pb_d1[1] = (byte) -72;
		qb_d1[0] = (byte) 10;

		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		quot_d1 = p_d1.divide(q_d1);
		rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());

		System.out.println("Test 3:");
		// 2032572 / 1284 = 1583
		pb_d1 = new byte[3];
		qb_d1 = new byte[2];

		pb_d1[0] = (byte) 31;
		pb_d1[1] = (byte) 3;
		pb_d1[2] = (byte) -68;
		qb_d1[0] = (byte) 5;
		qb_d1[1] = (byte) 4;

		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		quot_d1 = p_d1.divide(q_d1);
		rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());

		System.out.println("Test 4:");
		// 2032572 / 1289 = 1576 R 1108
		pb_d1 = new byte[3];
		qb_d1 = new byte[2];

		pb_d1[0] = (byte) 31;
		pb_d1[1] = (byte) 3;
		pb_d1[2] = (byte) -68;
		qb_d1[0] = (byte) 5;
		qb_d1[1] = (byte) 9;

		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		quot_d1 = p_d1.divide(q_d1);
		rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());

		System.out.println("Test 5:");
		// -3000 / 11 = -272 R -8
		pb_d1 = new byte[2];
		qb_d1 = new byte[1];

		pb_d1[0] = (byte) -12;
		pb_d1[1] = (byte) 72;
		qb_d1[0] = (byte) 11;

		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		quot_d1 = p_d1.divide(q_d1);
		rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());

		System.out.println("Test 6:");
		// 3000 / -11 = -272 R -8
		pb_d1 = new byte[2];
		qb_d1 = new byte[1];

		pb_d1[0] = (byte) 11;
		pb_d1[1] = (byte) -72;
		qb_d1[0] = (byte) -11;

		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		quot_d1 = p_d1.divide(q_d1);
		rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());

		System.out.println("Test 7:");
		// -3000 / -11 = 272 R 8
		pb_d1 = new byte[2];
		qb_d1 = new byte[1];

		pb_d1[0] = (byte) -12;
		pb_d1[1] = (byte) 72;
		qb_d1[0] = (byte) -11;


		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		quot_d1 = p_d1.divide(q_d1);
		rem_d1 = p_d1.mod(q_d1);

		System.out.println("quotient: "+ Arrays.toString(quot_d1.getVal()));
		System.out.println("	"+ new BigInteger(quot_d1.getVal()).toString());
		System.out.println("remainder: "+ Arrays.toString(rem_d1.getVal()));
		System.out.println("	"+ new BigInteger(rem_d1.getVal()).toString());


		System.out.println("---XGCD---");

		System.out.println("Test 1:");
		pb_d1 = new byte[1];
		qb_d1 = new byte[1];

		pb_d1[0] = (byte) 99;
		qb_d1[0] = (byte) 78;


		p_d1 = new LargeInteger(pb_d1);
		q_d1 = new LargeInteger(qb_d1);

		System.out.println("	"+ new BigInteger(p_d1.getVal()).toString());
		System.out.println("	"+ new BigInteger(q_d1.getVal()).toString());

		LargeInteger[] ans1 = p_d1.XGCD(q_d1);

		System.out.println("d: "+ Arrays.toString(ans1[0].getVal()));
		System.out.println("	"+ new BigInteger(ans1[0].getVal()).toString());
		System.out.println("x: "+ Arrays.toString(ans1[1].getVal()));
		System.out.println("	"+ new BigInteger(ans1[1].getVal()).toString());
		System.out.println("y: "+ Arrays.toString(ans1[2].getVal()));
		System.out.println("	"+ new BigInteger(ans1[2].getVal()).toString());


		System.out.println("---TESTING MODULAR EXPONENTATION---");
		byte[] a = new byte[1];
		byte[] b = new byte[1];
		byte[] c = new byte[1];

		/*a[0] = (byte) 2;
		b[0] = (byte) 5;
		c[0] = (byte) 3;*/

		/*a[0] = (byte) 15;
		b[0] = (byte) 8;
		c[0] = (byte) 20;*/

		/*a[0] = (byte) 15;
		b[0] = (byte) 10;
		c[0] = (byte) 20;*/

		a[0] = (byte) 54;
		b[0] = (byte) 23;
		c[0] = (byte) 13;

		/*a[0] = (byte) 5;
		b[0] = (byte) -3;
		c[0] = (byte) 10;*/


		LargeInteger aa = new LargeInteger(a);	// x
		LargeInteger bb = new LargeInteger(b);	// y
		LargeInteger cc = new LargeInteger(c);	// n

		System.out.println("a: "+ new BigInteger(aa.getVal()).toString());
		System.out.println("b: "+ new BigInteger(bb.getVal()).toString());
		System.out.println("c: "+ new BigInteger(cc.getVal()).toString());

		LargeInteger ans = aa.modularExp(bb, cc);

		System.out.println("The answer is: "+ Arrays.toString(ans.getVal()));
		System.out.println("	"+ new BigInteger(ans.getVal()).toString());




		
	}
}