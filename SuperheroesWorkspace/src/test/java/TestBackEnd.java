import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		con.setDoInput(true);
		
		// If there is a body, send it to the remote server
		if (!requestBody.isEmpty()) {
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
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
				makeRequest("/user/create", "POST", 
						"{\"username\":\"test\", \"password\":\"test\", \"email\":\"test\"}"
						).getResponseCode();
		
		// If we got some status besides user taken or user created, an error 
		// occurred, and we likely can't run 
		if (status != 200 && status != 401) {
			fail("Test User Could not be made. Status:" + status);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	
	
	
	// ========================================================
	// Login Tests
	// ========================================================
	
	/** Test that repeated logins and logouts can be made */
	@Test
	public void testLoginSuccess() throws IOException {
		// Test that we can login and logout
		login();
		logout();
		
		// Test that we can login and logout again
		login();
		logout();
	} // end of testLoginSuccess
	
	
	/** Test that a bad username returns the 401 status code, unauthorized. */
	@Test
	public void testLoginBadUsername() throws IOException {
		assertEquals(401, makeRequest("/login", "POST", 
						"{\"user\":\"asdfqwerzxcv1234567890\", \"pass\":\"test\"}"
						).getResponseCode());
	} // end of testLoginBadUsername
	
	/** Test that a bad password returns the 401 status code, unauthorized. */
	@Test
	public void testLoginBadPassword() throws IOException {
		assertEquals(401, makeRequest("/login", "POST", 
						"{\"user\":\"test\", \"pass\":\"asdfqwerzxcv1234567890\"}"
						).getResponseCode());
	} // end of testLoginBadPassword
	
	
	/** Test that a bad username and password returns the 401 status code, unauthorized. */
	@Test
	public void testLoginBadUsernamePassword() throws IOException {
		assertEquals(401, makeRequest("/login", "POST", 
						"{\"user\":\"asdfqwerzxcv1234567890\", \"pass\":\"asdfqwerzxcv1234567890\"}"
						).getResponseCode());
	} // end of testLoginBadUsernamePassword
	
	/** Test that missing parameters returns the 400 status code, bad request. */
	@Test
	public void testLoginBadParameters() throws IOException {
		assertEquals(400, makeRequest("/login", "POST", 
						"{}"
						).getResponseCode());
	}
	
	
	// ========================================================
	// User Tests
	// ========================================================
	
	/** Tests that the user information can be retrieved. */
	@Test
	public void testUserInfo() throws IOException {
		login();
		ObjectMapper om = new ObjectMapper();
		HashMap test = om.readValue(
				makeRequest("/user", "POST", "{\"userId\":61}").getInputStream(), HashMap.class);

		System.out.println(test);
	} // end of testUserInfo
	
	
	
	// ========================================================
	// Helper Functions
	// ========================================================
	
	
	
	public void login() throws IOException{
		// Attempt to log in and get a session. 
		assertEquals(200, makeRequest("/login", "POST", 
						"{\"user\":\"test\", \"pass\":\"test\"}"
						).getResponseCode());
	}
	
	public void logout() throws IOException{
		// Make sure we can logout
		assertEquals(200, makeRequest("/logout", "POST", ""
						).getResponseCode());
	}

} // end of class TestBackEnd
