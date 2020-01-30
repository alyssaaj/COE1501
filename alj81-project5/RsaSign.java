// Alyssa Jordan
// Project 5
// 12/7/19

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.math.BigInteger;


// Signing/Verification Program
public class RsaSign{
	
	public static void main(String args[]) {

		// accept two command-line argruments 
			// 1. a flag to specify whether to sign or verify
			// 2. the name of the file to sign/verify
			//String filename = args[1]; 
			if (args.length != 2){
				System.out.println("Error: Your command line arguments are incorrect. Please enter s or v followed by the filename.");
				System.exit(0);
			}

			String mode = args[0];
			String filename = args[1];
			System.out.println("mode: "+mode);


		// Sign
			if (args[0].equals("s")){
			// Generate a SHA-256 has of the contents of the original file
				try {
					// read in the file to hash
					Path path = Paths.get(args[1]);
					byte[] data = Files.readAllBytes(path);

					// create class instance to create SHA-256 hash
					MessageDigest md = MessageDigest.getInstance("SHA-256");

					// process the file
					md.update(data);
					// generate a hash of the file
					byte[] digest = md.digest();

					// print each byte in hex
					for (byte b : digest) {
						System.out.print(String.format("%02x", b));
					}
					System.out.println();

					try{
						
						ObjectInputStream inPriv = new ObjectInputStream(new FileInputStream("privkey.rsa"));
						List<byte[]> privkey = (List<byte[]>) inPriv.readObject();
						inPriv.close();

						byte[] d = privkey.get(0);
						byte[] n = privkey.get(1);


						LargeInteger d_Int = new LargeInteger(d);
						LargeInteger n_Int = new LargeInteger(n);
						LargeInteger hash_Int = new LargeInteger(digest);
						System.out.println("d: "+ Arrays.toString(d_Int.getVal()));
						System.out.println("n: "+ Arrays.toString(n_Int.getVal()));
						System.out.println("hash: "+ Arrays.toString(hash_Int.getVal()));

					
						LargeInteger decrypted_hash = hash_Int.modularExp(d_Int, n_Int);
						System.out.println("decrypted hash: "+ Arrays.toString(decrypted_hash.getVal()));
			

					// Write out the signature to a file with an extra .sig extension
						List<byte[]> signature = new ArrayList<byte[]>();
						signature.add(decrypted_hash.getVal());
						
						String sign_filename = filename+".sig";
						
						try{  
							ObjectOutputStream fw = new ObjectOutputStream(new FileOutputStream(sign_filename));
							fw.writeObject(signature);
							fw.close();

						} catch (IOException e) {
							System.out.println("Error: There was a problem with the signature file.");
						}

					} catch(Exception e){
						System.out.println("Error: privkey.rsa not found");
						System.exit(-1);
					}
				} catch(Exception e) {
					System.out.println(e.toString());
				}



			}
		// Verify
			else if (args[0].equals("v")){
			
			// Read the contents of the original file
			// Generate a SHA-256 hash of the contents of the original file
				try {
					// read in the file to hash
					Path path = Paths.get(args[1]);
					byte[] data = Files.readAllBytes(path);

					// create class instance to create SHA-256 hash
					MessageDigest md = MessageDigest.getInstance("SHA-256");

					// process the file
					md.update(data);
					// generate a has of the file
					byte[] digest = md.digest();

					// print each byte in hex
					for (byte b : digest) {
						System.out.print(String.format("%02x", b));
					}
					System.out.println();

					// Read the signed hash of the original file from the corresponding .sig file
					// Program should exit and display an error if .sig is not found in current directory
					try {
						String sign_filename = filename+".sig";
						ObjectInputStream inSig = new ObjectInputStream(new FileInputStream(sign_filename));
						List<byte[]> signature = (List<byte[]>) inSig.readObject();
						inSig.close();

						byte[] signed_hash = signature.get(0);

						// "Encrypt" this value with the key from pubkey.rsa
						// Program should exit and display an error if pubkey.rsa is not found in current directory
						try{
							
							ObjectInputStream inPub = new ObjectInputStream(new FileInputStream("pubkey.rsa"));
							List<byte[]> pubkey = (List<byte[]>) inPub.readObject();
							inPub.close();
							
							byte[] ebyte = pubkey.get(0);
							byte[] nbyte = pubkey.get(1);

							LargeInteger ee_Int = new LargeInteger(ebyte);
							LargeInteger n_Int = new LargeInteger(nbyte);
							LargeInteger hash_Int = new LargeInteger(digest);
							LargeInteger signed_hash_Int = new LargeInteger(signed_hash);

							LargeInteger encrypted_hash = hash_Int.modularExp(ee_Int, n_Int);
							System.out.println("encrypted hash: "+ Arrays.toString(encrypted_hash.getVal()));


							// Compare the hash value that was generated from myfile.txt to the one that was recovered from the signature.
							// Print a message to the console indicating whether the signature is valid.
							if (encrypted_hash.getVal().equals(signed_hash_Int.getVal())){
								System.out.println("The signature is valid.");
							}
							else{
								System.out.println("The signature is not valid.");
							}


						} catch(Exception e){
							System.out.println("Error: corresponding .sig file was not found");
							System.exit(-1);
						}


					} catch(Exception e){
						System.out.println("Error: corresponding .sig file was not found");
						System.exit(-1);
					}

				} catch(Exception e) {
					System.out.println(e.toString());
				}
				

			}
		// error not one of two modes
			else {
				System.out.println("Your command line arguments are incorrect. Please enter s or v followed by the filename.");
			}

	}
}