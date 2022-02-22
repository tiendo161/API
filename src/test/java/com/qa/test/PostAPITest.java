package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;
import com.qa.utils.TestUtil;

public class PostAPITest extends TestBase {
	String url;
	RestClient restClient;
	CloseableHttpResponse closebaleHttpResponse;
	
	
	@BeforeMethod
	public void setup(){
		url = prop.getProperty("URL") + prop.getProperty("serviceURL");
	}
	
	@Test(priority=1)
	public void postAPITest() throws ClientProtocolException, IOException{
	
		restClient = new RestClient();
		HttpPost httpPost = new HttpPost();
		
		HashMap<String, String> headerHashmap = new HashMap <String, String>();
		headerHashmap.put("Content-Type", "application/json");
		
		//jackson API:
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus", "leader");
		
		mapper.writeValue(new File ("D:\\Automation\\Software\\Windows OS-20200219T143743Z-001\\Windows OS\\eclipse-java-2019-12-R-win32-x86_64\\workspace\\com.org.restapitest\\src\\main\\java\\com\\qa\\data\\users.json"), users);
		
		String userJsonString = mapper.writeValueAsString(users);
		
		closebaleHttpResponse = restClient.post(url, userJsonString, headerHashmap);
		
		// 1. Status code
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_201);
		
		// 2. JsonString
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity() , "UTF-8");
		
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response from API Json " + responseJson);
		
		//3. json to Java object
		Users userResObj = mapper.readValue(responseString, Users.class);
		System.out.println(userResObj);
		Assert.assertEquals(userResObj.getName(), users.getName());
		Assert.assertEquals(userResObj.getJob(), users.getJob());
		
		System.out.println(userResObj.getId());
		System.out.println(userResObj.getCreatedAt());
		
		
		
		
	}
	
	

}
