/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

import java.util.*;
import java.lang.*;

public class MyLZW {
    private static final int R = 256;        // number of input chars
    public static int L = 512;                    // number of codewords = 2^W
    public static int W = 9;                      // codeword width
    public static char mode = 'n';

    public static void compress() {     // Do Nothing & Reset Mode
        BinaryStdOut.write(mode);

        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
            
            if (code == L && W < 16){       // run out of codewords but have not reached max codeword width of 16
                W++;                        // increase codeword width
                L=(int)Math.pow(2,W);       // increase number of codewords
            }

            // If reset mode...
            if (mode == 'r'){
                // Reset Codebook
                if (code == L && W == 16 ){      // run out of codewords and reached max codeword width
                    st = new TST<Integer>();

                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;  // R is codeword for EOF
                    W = 9;
                    L = 512;
                }
            }
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void compressMonitor() { 
        BinaryStdOut.write('m');
        String input = BinaryStdIn.readString();
        
        // Variables for Compression Ratio
        int bits_processed =0, bits_generated = 0;
        double inital_compression_ratio = 0.0, curr_compression_ratio = 0.0, ratio_of_ratios= 0.0;
        Boolean begin_monitoring = false;

        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.

            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();

            bits_generated = bits_generated + W;        // Increase bits_generated (compressed data) by current codeword length
            bits_processed = bits_processed + (t*8);    // Increase bits_proccessed (uncompressed data) by 8 (length of char) times length of s

            if (t < input.length() && code < L){    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            }

            if (code == L && W < 16){       // run out of codewords but have not reached max codeword width of 16
                W++;                        // increase codeword width
                L=(int)Math.pow(2,W);       // increase number of codewords
            }
            else if (code == L && W == 16){     // if everything is used up
                if (!begin_monitoring){         // has the initial compression ratio been computed?
                    inital_compression_ratio = (double) bits_processed/bits_generated;
                    begin_monitoring = true;
                }
                curr_compression_ratio = (double) bits_processed/bits_generated;
                ratio_of_ratios = inital_compression_ratio/curr_compression_ratio;
                if (ratio_of_ratios > 1.1){     // the Ratio of Ratios is high enough, reset codebook
                    //System.err.println("Reset");
                    st = new TST<Integer>();

                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;  // R is codeword for EOF
                    W = 9;
                    L = 512;
                    begin_monitoring = false;

                }
            }

            input = input.substring(t);
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void expand() {
        mode = BinaryStdIn.readChar();

        if (mode == 'n' || mode == 'r'){        // Do Nothing or Reset Mode
            String[] st = new String[65536];
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(W);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                if (codeword == R) break;

                String s = st[codeword];

                if (i == codeword){
                    s = val + val.charAt(0);   // special case hack
                }

                if (i < L-1){
                    st[i++] = val + s.charAt(0);
                }

                if (i == L-1 && W < 16){        // run out of codewords but have not reached max codeword width of 16
                    W++;                        // Happens one sooner to prevent corner case of LZW
                    L=(int)Math.pow(2,W);
                }

                // if in reset mode
                if (mode == 'r'){
                    if (i == L-1 && W == 16){      // if codewords run out and max codeword width is reached reset codebook
                        
                        W = 9;
                        L = 512;
                        st = new String[65536];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = "";  
                        codeword = BinaryStdIn.readInt(W);

                    }
                }
                val = s;

            }
            BinaryStdOut.close();
        }
        else if (mode == 'm') {     // Monitor mode
            String[] st = new String[65536];
            int i; // next available codeword value
            
            // Compression Ratio Variables
            int bits_processed = 0, bits_generated = 0;
            double inital_compression_ratio = 0.0, curr_compression_ratio = 0.0, ratio_of_ratios = 0.0;
            Boolean begin_monitoring = false;

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(W);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            while (true) {
                bits_generated = bits_generated + (val.length()*8);     // increase bits_generated (uncompressed data) by 8 * length of val
                bits_processed = bits_processed + W;            // increase bits_processed (compressed data) by current codeword width
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                
                if (codeword == R) break;

                String s = st[codeword];

                if (i == codeword){
                    s = val + val.charAt(0);   // special case hack
                }

                if (i < L-1){
                    st[i++] = val + s.charAt(0);
                }

                if (i == L-1 && W < 16){
                    W++;
                    L=(int)Math.pow(2,W);
                }
                else if (i == L-1 && W == 16)   // If codebook is full, start monitoring
                {
                    if (!begin_monitoring){     // if there is no inital compression ratio find it
                        inital_compression_ratio = (double) bits_generated/bits_processed;
                        begin_monitoring = true;
                    }
                    curr_compression_ratio = (double) bits_generated/bits_processed;
                    ratio_of_ratios = inital_compression_ratio/curr_compression_ratio;
                    if (ratio_of_ratios > 1.1){
                        //System.err.println("reset");
                        codeword = BinaryStdIn.readInt(W);
                        W = 9;
                        L = 512;
                        st = new String[65536];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = "";  
                        begin_monitoring = false;
 
                    }

                }
                val = s;

            }
            BinaryStdOut.close();

        }
    }

    


    public static void main(String[] args) {
        if (args[0].equals("-") && (args[1].equals("n") || args[1].equals("r")) ){
            mode = args[1].charAt(0);
            compress();
        }
        else if (args[0].equals("-") && args[1].equals("m")) {
            mode = args[1].charAt(0);
            compressMonitor();
        }
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
