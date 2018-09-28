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
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
		ObjectMapper om = new ObjectMapper();
		HashMap<String, Object> userMap = om.readValue(
				makeRequest("/user", "POST", "{\"userId\":61}").getInputStream(), HashMap.class);

		assertEquals("test", userMap.get("username").toString());
		assertEquals("test", userMap.get("email").toString());
		assertEquals("", userMap.get("password").toString());
	} // end of testUserInfo
	
	/** Tests that a bad parameter causes it to send a 400 code. */
	@Test
	public void testUserBadParameter() throws IOException {
		assertEquals(400, makeRequest("/user", "POST", "{\"badparam\":61}").getResponseCode());
	} // end of testUserBadParameter
	
	/** Tests that a missing user causes it to send a 410 code. */
	@Test
	public void testUserMissingUser() throws IOException {
		assertEquals(410, makeRequest("/user", "POST", "{\"userId\":-5}").getResponseCode());
	} // end of testUserBadParameter
	
	/** Tests that a missing user causes it to send a 400 code. */
	@Test
	public void testUserNoParams() throws IOException {
		assertEquals(400, makeRequest("/user", "POST", "{}").getResponseCode());
	} // end of testUserNoParams
	
	
	
	
	/** Tests that the create user function returns the correct status code. 
	 * @throws IOException */
	@Test
	public void testCreateUserBadUsernameParam() throws IOException {
		int status = 
			makeRequest("/user/create", "POST", 
					"{\"asdf\":\"test\", \"password\":\"test\", \"email\":\"test\"}"
					).getResponseCode();
		assertEquals(400, status);
	} // end of testCreateUserBadUsernameParam
	
	/** Tests that the create user function returns the correct status code. 
	 * @throws IOException */
	@Test
	public void testCreateUserBadPasswordParam() throws IOException {
		int status = 
			makeRequest("/user/create", "POST", 
					"{\"username\":\"test\", \"asdf\":\"test\", \"email\":\"test\"}"
					).getResponseCode();
		assertEquals(400, status);
	} // end of testCreateUserBadPasswordParam
	
	/** Tests that the create user function returns the correct status code. 
	 * @throws IOException */
	@Test
	public void testCreateUserBadEmailParam() throws IOException {
		int status = 
			makeRequest("/user/create", "POST", 
					"{\"username\":\"test\", \"password\":\"test\", \"asdf\":\"test\"}"
					).getResponseCode();
		assertEquals(400, status);
	} // end of testCreateUserBadEmailParam
	
	
	/** Tests that the create user function returns the correct status code. 
	 * @throws IOException */
	@Test
	public void testCreateUserNameTaken() throws IOException {
		int status = 
			makeRequest("/user/create", "POST", 
					"{\"username\":\"test\", \"password\":\"test\", \"email\":\"test\"}"
					).getResponseCode();
		assertEquals(401, status);
	} // end of testCreateUserNameTaken
	
	
	
	/** Tests that the create user function makes a user. 
	 * Requires the /user mapping to work. 
	 * @throws IOException */
	/*@Test
	public void testCreateUser() throws IOException {
		ObjectMapper om = new ObjectMapper();
		String username;
		Random rand = new Random();
		HashMap<String, Object> userMap;
		
		// Find an available username
		while(true) {
			// Generate a random username
			username = "testUser" + rand.nextInt();
			
			userMap = om.readValue(
					makeRequest("/user", "POST", "{\"userId\":61}").getInputStream(), HashMap.class);
			if (!userMap.get("username").toString().equals(username)) {
				break;
			}
		}
		
		HttpURLConnection con = makeRequest("/user/create", "POST", 
				"{\"username\":\"" + username + "\""
						+ ", \"password\":\"test\""
						+ ", \"email\":\"" + username + "-email\"}");
		
		
		userMap = om.readValue(con.getInputStream(), HashMap.class);
		int status = con.getResponseCode();
		long userId = (long) userMap.get("id");
		

		
		assertEquals(200, status);
		
		userMap = om.readValue(
				makeRequest("/user", "POST", "{\"userId\":61}").getInputStream(), HashMap.class);

		assertEquals(username, userMap.get("username").toString());
		assertEquals("test", userMap.get("email").toString());
		assertEquals("", userMap.get("password").toString());
	} // end of testCreateUserNameTaken
*/	
	
	
	
	/** Tests the update user function to be sure it's working correctly. 
	 * @throws IOException */
	@Test
	public void testUpdateUserBadUsername() throws IOException {
		int status = 
				makeRequest("/user/update", "POST", 
						"{\"asdf\":\"test\", "
						+ "\"password\":\"test\", "
						+ "\"email\":\"test\", "
						+ "\"role\":\"y\"}"
						).getResponseCode();
		assertEquals(400, status);
	} // testUpdateUserBadUsername
	
	/** Tests the update user function to be sure it's working correctly. 
	 * @throws IOException */
	@Test
	public void testUpdateUserBadPassword() throws IOException {
		int status = 
				makeRequest("/user/update", "POST", 
						"{\"username\":\"test\", "
						+ "\"asdf\":\"test\", "
						+ "\"email\":\"test\", "
						+ "\"role\":\"y\"}"
						).getResponseCode();
		assertEquals(400, status);
	} // testUpdateUserBadPassword
	
	/** Tests the update user function to be sure it's working correctly. 
	 * @throws IOException */
	@Test
	public void testUpdateUserBadEmail() throws IOException {
		int status = 
				makeRequest("/user/update", "POST", 
						"{\"username\":\"test\", "
						+ "\"password\":\"test\", "
						+ "\"asdf\":\"test\", "
						+ "\"role\":\"y\"}"
						).getResponseCode();
		assertEquals(400, status);
	} // testUpdateUserBadEmail
	
	
	/** Tests the update user function to be sure it's working correctly. 
	 * @throws IOException */
	@Test
	public void testUpdateUserBadRole() throws IOException {
		int status = 
				makeRequest("/user/update", "POST", 
						"{\"username\":\"test\", "
						+ "\"password\":\"test\", "
						+ "\"email\":\"test\", "
						+ "\"asdf\":\"y\"}"
						).getResponseCode();
		assertEquals(400, status);
	} // testUpdateUserBadRole
	
	
	/** Tests the update user function to be sure it's working correctly. 
	 * @throws IOException */
	@Test
	public void testUpdateUser() throws IOException {
//		Map<String, Object> userMap = getUserJson("test", "test");
//		String oUsername = (String) userMap.get("username");
//		String oPassword = "test";
//		String oEmail = (String) userMap.get("email");
		String oUsername = "test";
		String oPassword = "test";
		String oEmail = "test";
		Map<String, Object> userMap;
		
		int status = 
				makeRequest("/user/update", "POST", 
						"{\"username\":\"" + oUsername + "\", "
						+ "\"password\":\"1234\", "
						+ "\"email\":\"test12345\", "
						+ "\"role\":\"y\"}"
						).getResponseCode();
		assertEquals(200, status);
		
		userMap = getUserJson("test", "1234");
		
		assertEquals("test", userMap.get("username").toString());
//		assertEquals("", userMap.get("password").toString());
		assertEquals("test12345", userMap.get("email").toString());
		assertEquals("y", userMap.get("role").toString());
		
		status = 
				makeRequest("/user/update", "POST", 
						"{\"username\":\"" + oUsername + "\", "
						+ "\"password\":\"" + oPassword + "\", "
						+ "\"email\":\"" + oEmail + "\", "
						+ "\"role\":\"n\"}"
						).getResponseCode();
		assertEquals(200, status);
		
		userMap = getUserJson(oUsername, oPassword);
		
		assertEquals("test", userMap.get("username").toString());
//		assertEquals("", userMap.get("password").toString());
		assertEquals("test", userMap.get("email").toString());
		assertEquals("n", userMap.get("role").toString());
	} // testUpdateUserBadRole
	
	
	
	
	
	
	// ========================================================
	// Hero Tests
	// ========================================================
	
	/** Tests to make sure that /allheroes returns something. 
	 * Can make more robust later. 
	 * @throws IOException */
	@Test
	public void testGetAllHeroes() throws IOException {
		// TODO: Actually check if heroes were returned
		int status = 
				makeRequest("/allheroes", "GET", "").getResponseCode();
		assertEquals(200, status);
	} // testGetAllHeroes
//	
//
//	@Test
//	public void testGetHeroById() throws IOException {
//		int status = 
//				makeRequest("/hero?id=1", "GET", "").getResponseCode();
//		assertEquals(200, status);
//		status = 
//				makeRequest("/hero?id=717", "GET", "").getResponseCode();
//		assertEquals(200, status);
//	} // testGetHeroById
//	
//	@Test
//	public void testGetHeroByIdBadHero() throws IOException {
//		int status = 
//				makeRequest("/hero?id=1000000", "GET", "").getResponseCode();
//		assertEquals(410, status);
//	} // testGetHeroByIdBadHero
//	
//	
//	@Test
//	public void testGetHeroRange() throws IOException {
//		int status = 
//				makeRequest("/rangeheroes?low=1&high=20", "GET", "").getResponseCode();
//		assertEquals(200, status);
//		status = 
//				makeRequest("/rangeheroes?id=717", "GET", "").getResponseCode();
//		assertEquals(200, status);
//	} // testGetHeroById
	
	
	
	
	// ========================================================
	// Team Tests
	// ========================================================
	
	@Test
	public void testGetAllTeams() throws IOException {
		// TODO: Actually check if teams were returned
		int status = 
				makeRequest("/team/all", "GET", "").getResponseCode();
		assertEquals(200, status);
	} // testGetAllHeroes
	
	
	
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
	
	
	public Map<String, Object> getUserJson(String username, String password) throws IOException {
		
		ObjectMapper om = new ObjectMapper();
		HashMap<String, Object> userMap = om.readValue(
				makeRequest("/login", "POST", 
				"{\"user\":\"" + username + "\", \"pass\":\"" + password + "\"}"
				).getInputStream(), HashMap.class);
		
		
		return userMap;
	}
	
	
	
	 
	
	
	
	

} // end of class TestBackEnd
