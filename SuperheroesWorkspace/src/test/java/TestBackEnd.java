import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestBackEnd {
	
	/** TheURL to test with */
	public static final String TEST_URL="http://localhost:8084";
	
	// Send an http request. 
	private static HttpURLConnection makeRequest(String requestLink, String method, String requestBody) throws IOException {
		// A url object that will be used to make the http connection. 
		URL url = null;
		
		// Used to make the remote connection. 
		HttpURLConnection con = null;
		
		
		try {
			// Attempt to parse the urlString. It's possible it might fail due 
			// to an MalformedURLException. However, it can also throw a 
			// generic RuntimeException on certain URL parameters, which needs 
			// to be handled. 
			url = new URL(TEST_URL + requestLink);
		} catch (RuntimeException ex) {
			// Just wrap the runtime exception into a more appropriate 
			// IOException. 
			throw new IOException(ex);
		}
		
		// Create the object used to connect to the remote API, and set 
		// settings to get the JSON.
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(method);
//		con.setRequestProperty("Content-Type", "application/json");
		
		
		// If there is a body, send it to the remote server
		if (!requestBody.isEmpty()) {
			con.setDoOutput(true);
//			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
//			con.setRequestProperty("Content-Type", "application/json, charset=UTF-8");
//			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Length", ""+requestBody.length());
			try(DataOutputStream writer = new DataOutputStream(con.getOutputStream())) {
				writer.write(requestBody.getBytes(StandardCharsets.UTF_8));
				writer.flush();
			}
		}
		else {
			// Otherwise, just make the connection to the remote resource. 
			con.connect();
		}
		
		return con;
	} // end of 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Make sure we have an account to use for testing. 
		int status = 
				makeRequest("/user/create?user=test&pass=test&email=test", "POST", 
						"{\"user\":\"test\", \"pass\":\"test\", \"email\":\"test\"}"
						).getResponseCode();
		
		// If we got some status besides user taken or user created, an error 
		// occurred, and we likely can't run 
		if (status != 200 && status != 401) {
			fail("Test User Could not be made. ");
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	@Before
	public void preTestLogin() throws IOException{
		// Attempt to log in and get a session. 
		assertEquals(200, makeRequest("/login", "POST", 
						"{\"user\":\"test\", \"pass\":\"test\"}"
						).getResponseCode());
	}
	
	@After
	public void postTestLogout() throws IOException{
		// Make sure we can logout
		assertEquals(200, makeRequest("/logout", "POST", ""
						).getResponseCode());
	}
	
	@Test
	public void testLoginSuccess() throws IOException {
		// Make sure we can logout again
		assertEquals(200, makeRequest("/logout", "POST", ""
						).getResponseCode());
		
		// Make sure we can login again
		assertEquals(200, makeRequest("/login", "POST", 
						"{\"user\":\"test\", \"pass\":\"test\"}"
						).getResponseCode());
	} // end of testLoginSuccess
	
	
	

} // end of class TestBackEnd
