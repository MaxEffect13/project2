package com.revature.drivers;

import java.io.IOException;
import java.io.RandomAccessFile;


/** This program is designed to take multiple json hero files, and compile 
 * them into one large heroes json file.  */
public class CombineHeroJson {
	
	
	/** The prefix of the json file. */
	private static final String JSON_PREFIX = "{\"heroes\": [";
	
	/** The suffix of the json file. */
	private static final String JSON_SUFFIX = "]}";
	
	
	private static final String BASE_PATH = 
			"C:\\Users\\David\\Desktop\\Dev Environments\\gitStuff"
					+ "\\Revature\\project2\\";
	
	private static final String[] FILE_ARRAY = {
		"heroes10.json",
		"heroes11.json"
	};
	
	private static final String OUT_PATH = "heroes.json";
	
	
	public static void main(String[] args) throws IOException {
		StringBuilder sBuilder = new StringBuilder();
		RandomAccessFile raf = null;
		
		// Go through each file listed. 
		for (String fileStr : FILE_ARRAY) {
			// Open a new reader. 
			raf = new RandomAccessFile(BASE_PATH + fileStr, "r");
			
			// Read in the whole file and convert to a string. 
			byte[] bytes = new byte[(int)raf.length()];
			raf.readFully(bytes);
			String str = new String(bytes);
			System.out.println(str);
			
			// Remove the prefix and suffix of the json
			str = str.substring(str.indexOf(JSON_PREFIX) + JSON_PREFIX.length());
			str = str.substring(0, str.lastIndexOf(JSON_SUFFIX));
			
			// Add the json to the list. 
			sBuilder.append(str);
			sBuilder.append(",");
			
			// Close the reader.
			raf.close();
		}
		
		// Delete the last comma. 
		sBuilder.deleteCharAt(sBuilder.length()-1);
		
		// Open a writer for the output file for the json. 
		raf = new RandomAccessFile(BASE_PATH + OUT_PATH, "rw");
		
		// Save the new json file.
		raf.write(JSON_PREFIX.getBytes());
		raf.write(sBuilder.toString().getBytes());
		raf.write(JSON_SUFFIX.getBytes());
		raf.close();
	}

}
