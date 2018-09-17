package com.revature.drivers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.databind.ObjectMapper;


/** This is a program that pulls json from an api, and saves it to a json file. 
 * The various parameters of the program can be changed by modifying several of 
 * the provided constants. */
public class PullJsonFromApi {
	
	
	/** The location to save the resulting json to. */
	private static String OUTPUT_FILE = 
			"C:\\Users\\David\\Desktop\\Dev Environments\\gitStuff"
			+ "\\Revature\\project2\\heroes12.json";
	
	
	
	/** The name of the response field in the json. Used to tell if the 
	 * request was successful. */
	private static final String RESPONSE_TEXT = "response";
	
	/** The response value from the API upon a successful request. */
	private static final String HERO_SUCCESS = "success";
	
	/** The starting hero ID to use. */
	private static final int STARTING_ID = 1;
	
	/** A list of hero ID's that don't return anything. Therefore skip them. */
	private static final int[] BAD_IDS = {132, 173, 368};
	
	
	
	
	/** The base URL to Request from. */
	public static final String BASE_URL = "http://www.superheroapi.com/api.php/";
	
	/** The API key to use, if applicable. I'm not too concerned about hard
	 * coding this for now as it was a free key. */
	public static final String API_KEY = "104846317149136";
	
	/** Returns the string that begins the parameter list, if applicable. */
	public static final String PARAMETER_PREFIX = "/";
	
	
	
	/** The maximum number of retries that getJSON will make when attempting 
	 * to connect to the API. */
	private static final int MAX_RETRIES = 3;
	
	
	/** The maximum number of requests per minute. Used to get by rate limits.
	 * Not an exact metric, only a good approximate. */
	private static final int MAX_REQUESTS_PER_MIN = 19;
	
	
	/** The prefix of the json file. */
	private static final String JSON_PREFIX = "{\"heroes\": [";
	
	/** The suffix of the json file. */
	private static final String JSON_SUFFIX = "]}";
	
	
	
	public static void main(String[] args) {
		
		// A string that holds the response from the api. 
		String jsonString;
		
		// A hash map constructed from the json.
		HashMap jsonMap;
		
		// The file for the resulting hero json
		File outFile = new File(OUTPUT_FILE);
		
		// The writer for the output file. 
		BufferedWriter writer = null;
		
		// True until the first hero is processed. Used to separate heroes by comma
		boolean firstHero = true;
		
		// A set of the bad hero id's. 
		HashSet<Integer> badIDs = new HashSet<>();
		for (int id : BAD_IDS) {
			badIDs.add(id);
		}
		
		
		try {
			// Attempt to create a new file, or not if it already exists. 
			outFile.createNewFile();
			
			// Open a writer to the file. Fails if file wasn't created, which is
			// handled by the exception handler. 
			writer = new BufferedWriter(new FileWriter(outFile));
			
			// Write the json prefix
			writer.write(JSON_PREFIX);
			
			ObjectMapper om = new ObjectMapper();
			
			int currentId = STARTING_ID;
			
			// While we have not encountered an invalid id, keep requesting heros
			// and saving them. Or until we encounter an exception. 
			while (true) {
				// If the current ID is a bad id, skip it. 
				if (badIDs.contains(currentId)) {
					currentId++;
					continue;
				}
				// Get the raw json string, and convert it to a map to read the values
				jsonString = getHeroJSON(currentId++);
				jsonMap = om.readValue(jsonString, HashMap.class);
				
				// If the response isn't a successful one, we're likely done. 
				if (!jsonMap.get(RESPONSE_TEXT).equals(HERO_SUCCESS)) {
					System.out.println("Response: " + jsonMap.get(RESPONSE_TEXT));
					break;
				}
				// If any input is detected on the console, stops requesting. 
				if (System.in.available() > 0) {
					System.out.println("Detected Input. Breaking...");
					break;
				}
				
				// If this isn't the first hero that was written, put a comma to 
				// separate the json entries. 
				if (!firstHero) {
					writer.write(",");
				}
				firstHero = false;
				
				// Attempt to write the JSON to the file. 
				writer.write(jsonString);
				
				// Perform a wait based on the maximum number of requests per 
				// minute. This is only approximate as it doesn't take into 
				// account the time the request took. 
				try {Thread.sleep(60000/MAX_REQUESTS_PER_MIN);} catch(InterruptedException ie) {}
			}
		
		} catch(FileNotFoundException e) {
			// Print the problem to the console if there was a problem 
			// opening the writer. 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Whether success or error, attempt to write the JSON suffix to the 
			// end of the file, and close it. 
			try {writer.write(JSON_SUFFIX);} catch(IOException e) {
				System.err.println("Failed to write JSON suffix. ");
			}
			try {writer.close();} catch(IOException e) {
				System.err.println("Failed to close JSON file.");
			}
		}
		
	} // end of main
	
	
	/**
	 * Takes an id of a hero, and returns a string representing the JSON of the 
	 * requested hero. 
	 * @param heroId - The id of the hero to get. 
	 * @return A string representation of the result of the query. 
	 * @throws IOException - If there was a problem making the request or 
	 * or getting a response. 
	 */
	public static String getHeroJSON(int heroId) throws IOException{
		String heroURL = BASE_URL + API_KEY + PARAMETER_PREFIX 
												+ String.valueOf(heroId);
		return getRequest(heroURL);
	} // end of getHeroJSON
	
	
	
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
			// TODO: Put within loop to retry on failed connection. 
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









