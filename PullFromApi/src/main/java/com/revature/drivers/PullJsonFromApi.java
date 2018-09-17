package com.revature.drivers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PullJsonFromApi {
	
	/** The base URL to Request from. */
	public static final String BASE_URL = "http://www.superheroapi.com/api.php/";
	
	/** The API key to use, if applicable. I'm not too concerned about hard
	 * coding this for now as it was a free key. */
	public static final String API_KEY = "104846317149136";
	
	/** The parameters for the search. Uses a question mark where a parameter 
	 * should be replaced at runtime. */
	public static final String PARAMETERS = "/?";
	
	/** The maximum number of retries that getJSON will make when attempting 
	 * to connect to the API. */
	private static final int MAX_RETRIES = 3;
	
	
	public static void main(String[] args) {
		try {
			getRequest(BASE_URL + API_KEY + "/1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // end of main
	
	
	/**
	 * Takes a URL, with parameters included, and performs a GET request of on 
	 * that URL. 
	 * @param url - The url to connect to.
	 * @return The string result returned from he server.
	 * @throws IOException - If there is a problem parsing the URL or if 
	 */
	public static String getRequest(String urlString) throws IOException {
		
		// A url object that will be used to make the http connection. 
		URL url = null;
		
		// Used to make the remote connection. 
		HttpURLConnection con = null;
		
		// Reads the response from the remote resource. 
		BufferedReader reader = null;
		
		// Used to buffer the response from the server. 
		StringBuilder sBuilder = null;
		
		// Used in pulling lines of input from the response. 
		String inputLine = null;
		
		try {
			// TODO: Put witin loop to retry on failed connection. 
			// Attempt to parse the urlString. It's possible it might fail due 
			// to an MalformedURLException. However, it can also throw a 
			// generic RuntimeException on certain URL parameters, which needs 
			// to be handled. 
			url = new URL(urlString);
		} catch (RuntimeException ex) {
			// Just wrap the runtime exception into a more appropriate 
			// IOException. 
			throw new IOException(ex);
		}
		
		// Create the object used to connect to the remote API, and set 
		// settings to get the JSON.
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		
		// Makes the connection to the remote resource. 
		con.connect();
		
		
		// Initialize the reader for getting the response. 
		reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		sBuilder = new StringBuilder();
		
		// Until we reach the end of the stream, read bytes into the buffer. 
		// then copy those bytes into another buffer that is self expanding. 
		while ((inputLine = reader.readLine()) != null) {
			sBuilder.append(inputLine);
			//TODO: For debugging
			System.out.print(inputLine);
		}
		System.out.println();
		
		return sBuilder.toString();
	} // end of getRequest
	
	
	
	
} // end of class PullJsonFromApi









