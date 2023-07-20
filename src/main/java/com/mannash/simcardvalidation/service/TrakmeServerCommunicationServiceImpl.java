package com.mannash.simcardvalidation.service;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.ProxyAuthenticator;
import com.mannash.simcardvalidation.pojo.*;
//import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
//import com.mannash.trakme.client.pojo.LogType;
//import com.mannash.trakme.client.pojo.RequestClientLogPojo;
//import com.mannash.trakme.client.pojo.ResponseAuthenticationPojo;
//import com.mannash.trakme.client.pojo.ResponseFieldTestingCardInfos;
//import com.mannash.trakme.client.pojo.ResponseFieldTestingProfileConfigPojo;
//import com.mannash.trakme.client.pojo.ResponseProfileTestingConfig;
//import com.mannash.trakme.client.pojo.ResponseStressTestingConfig;
//import com.mannash.trakme.client.pojo.RequestClientLogsPojo;
//import com.mannash.trakme.client.pojo.ServerResponseLogPojo;
//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.LoggerServiceImpl;
//import com.mannash.trakmeserver.rest.service.FieldTestingClientLoggerServiceImpl;

public class TrakmeServerCommunicationServiceImpl implements TrakmeServerCommunicationService {

	private LoggerService loggerService;
	private ResponseAuthenticationPojo authenticationPojo;
	private final Logger logger = LoggerFactory.getLogger(TrakmeServerCommunicationServiceImpl.class);
	public String hostIP = "";
	File file ;
	File proxyFile ;
	Properties properties = new Properties();
	FileInputStream input = null;
	String filePath = "..\\config\\";

//	String filePath = "D:\\Work\\R&D\\SIMVerify\\config\\";

	String proxyUser = null;
	String proxyPassword = null;
	String proxyAddress = null;
	String proxyPort = null;
	String userAgent = null;
	String proxyEnabled = null;
	int timeout = 20000;



	public TrakmeServerCommunicationServiceImpl() {
		file = new File(filePath+"user.properties");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		proxyFile = new File(filePath+"proxy.properties");
		if (!proxyFile.exists()) {
			try {
				proxyFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

		try {
			this.input = new FileInputStream(filePath+"proxy.properties");
			this.properties.load(input);
			this.proxyUser = this.properties.getProperty("proxyUser");
			this.proxyPassword = this.properties.getProperty("proxyPassword");
			this.proxyEnabled = this.properties.getProperty("proxyEnabled");
			this.proxyAddress = this.properties.getProperty("proxyAddress");
			this.proxyPort = this.properties.getProperty("proxyPort");

			System.out.println("proxyUser : "+this.proxyUser+" proxyPassword: "+this.proxyPassword+" proxyAddress : "+this.proxyAddress+" proxyPort : "+this.proxyPort);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		if (this.proxyEnabled.equalsIgnoreCase("yes")) {
			this.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";
			System.setProperty("http.proxyHost", this.proxyAddress);
			System.setProperty("http.proxyPort", this.proxyPort);
			System.setProperty("http.proxyUser", this.proxyUser);
			System.setProperty("http.proxyPassword", this.proxyPassword);
			System.setProperty("http.agent", this.userAgent);

//			String encodedUserPwd = new String(Base64.encodeBase64((proxyUser + ":" + proxyPassword).getBytes()));
			Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));
		}


	}

	Gson gson;

	ResponseAuthenticationPojo responseAuthenticationPojo = new ResponseAuthenticationPojo();


	@SuppressWarnings("deprecation")
	public ResponseAuthenticationPojo authenticateClient(String userName, String password) {

//		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
//		String proxyUser = null;
//		String proxyPassword = null;
//		String proxyAddress = null;
//		String proxyPort = null;
//
//		try {
//			input = new FileInputStream(filePath+"proxy.properties");
//			properties.load(input);
//			proxyUser = properties.getProperty("proxyUser");
//			proxyPassword = properties.getProperty("proxyPassword");
//
//			proxyAddress = properties.getProperty("proxyAddress");
//			proxyPort = properties.getProperty("proxyPort");
//
//			System.out.println("proxyUser : "+proxyUser+" proxyPassword: "+proxyPassword+" proxyAddress : "+proxyAddress+" proxyPort : "+proxyPort);
//		} catch (FileNotFoundException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";
//
//		System.setProperty("http.proxyHost", proxyAddress);
//		System.setProperty("http.proxyPort", proxyPort );
//		System.setProperty("http.proxyUser", proxyUser);
//		System.setProperty("http.proxyPassword", proxyPassword);
//		System.setProperty("http.agent", userAgent);
////            System.setProperty("http.auth.preference", "Negotiate");
//
////		String encodedUserPwd = new String(Base64.encodeBase64((proxyUser+":"+proxyPassword).getBytes()));
//
//
//		Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));



		if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
			responseAuthenticationPojo.setMessage("Username or Password can not be empty");
			return responseAuthenticationPojo;
		}

		else {
			try {
				HttpClient client = new DefaultHttpClient();
				String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/auth/validateUser?userId="
						+ userName + "&password=" + URLEncoder.encode(password);

				//-------------------
				URL url = new URL(completeUrl);
				HttpURLConnection response = (HttpURLConnection) url.openConnection();
				response.setRequestMethod("POST");

				response.setConnectTimeout(timeout);
				response.setReadTimeout(timeout);

				InputStream inputStream = response.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				StringBuilder response_string = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					response_string.append(line);
				}
				reader.close();
				inputStream.close();

				System.out.println(response_string.toString());
				//-------

//				HttpPost httpPost = new HttpPost(completeUrl);
//				response.
////				httpPost.setHeader("Content-type", "application/json");
//				// this.logger.info("Sending request to TrakmeServer for authenticating user : " + userName);
//				HttpResponse response2 = client.execute(httpPost);
//				System.out.println("Authentic user rersponse : "+response+" code : "+response.getResponseCode());

				if (response != null) {
//					String responseString = EntityUtils.toString((HttpEntity) response2.getEntity());
					if (response.getResponseCode() != 200) {
						System.out.println("inside != 200");

						if (readCredentialsFromLocal(userName, password)) {
							return responseAuthenticationPojo;
						}
						if (response_string != null) {
							if (response.getResponseCode() == 401) {
								responseAuthenticationPojo.setMessage("Invalid username/password");
								// this.logger.error("Invalid Username & Password , Status code : " + 401);
								responseAuthenticationPojo.setStatusCode(401);
							} else {
								responseAuthenticationPojo.setMessage("Unable to authenticate user");
								// this.logger.debug("Unable to authenticate user with Status code : " + response.getStatusLine().getStatusCode());
								responseAuthenticationPojo.setStatusCode(response.getResponseCode());
							}
						} else {
							responseAuthenticationPojo.setMessage("Unable to authenticate user");
							responseAuthenticationPojo.setStatusCode(500);
							// this.logger.debug("Unable to authenticate user with Status code : " + 500);
						}
					}

					else {
						responseAuthenticationPojo.setStatusCode(200);
						System.out.println("SERVER : user authenticate successfully with user name : "+userName);

						try (FileWriter writer = new FileWriter(file);
							 BufferedWriter bw = new BufferedWriter(writer)) {

							bw.write("userId="+userName);
							bw.newLine();
							bw.write("password="+password);
							bw.newLine();
							// add more user IDs and passwords as required
						} catch (IOException e) {
							e.printStackTrace();
						}

						// this.logger.info("TrakmeServer authenticated successfully with user" + userName);
						// System.out.println("Status Code : " + 200);
					}
				} else {
					responseAuthenticationPojo.setMessage("Unable to authenticate user");
					responseAuthenticationPojo.setStatusCode(500);
					// this.logger.debug("Unable to authenticate user, response is null");
				}

			} catch (SocketTimeoutException e) {
				System.out.println("Request timed out");
//				readCredentialsFromLocal(userName,password);
			} catch (Exception e) {
				readCredentialsFromLocal(userName,password);
				System.out.println("inside the catch");
				e.printStackTrace();
//				responseAuthenticationPojo.setMessage("Unable to authenticate user");
//				responseAuthenticationPojo.setStatusCode(500);
				// this.logger.info("Unable to authentcate user");
			}
			return responseAuthenticationPojo;
		}

	}

	//----------
	public ResponseAuthenticationPojo authenticateClient2(String userName, String password) {
		authenticateProxy();
		if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
			responseAuthenticationPojo.setMessage("Username or Password can not be empty");
			return responseAuthenticationPojo;
		}

		else {
			try {
				org.apache.http.client.HttpClient client = new DefaultHttpClient();
				String completeUrl = "http://trakme.mannash.com/trakmeserver/api/external/auth/validateUser?userId="
						+ userName + "&password=" + URLEncoder.encode(password);

				HttpPost httpPost = new HttpPost(completeUrl);
				httpPost.setHeader("Content-type", "application/json");
				// this.logger.info("Sending request to TrakmeServer for authenticating user : " + userName);
				HttpResponse response = client.execute(httpPost);
				if (response != null) {
					String responseString = EntityUtils.toString((HttpEntity) response.getEntity());
					if (response.getStatusLine().getStatusCode() != 200) {
						System.out.println("inside != 200");

						if (readCredentialsFromLocal(userName, password)) {
							return responseAuthenticationPojo;
						}
						if (responseString != null) {
							if (response.getStatusLine().getStatusCode() == 401) {
								responseAuthenticationPojo.setMessage("Invalid username/password");
								// this.logger.error("Invalid Username & Password , Status code : " + 401);
								responseAuthenticationPojo.setStatusCode(401);
							} else {
								responseAuthenticationPojo.setMessage("Unable to authenticate user");
								// this.logger.debug("Unable to authenticate user with Status code : " + response.getStatusLine().getStatusCode());
								responseAuthenticationPojo.setStatusCode(response.getStatusLine().getStatusCode());
							}
						} else {
							responseAuthenticationPojo.setMessage("Unable to authenticate user");
							responseAuthenticationPojo.setStatusCode(500);
							// this.logger.debug("Unable to authenticate user with Status code : " + 500);
						}
					}

					else {
						responseAuthenticationPojo.setStatusCode(200);
						System.out.println("SERVER : user authenticate successfully with user name : "+userName);

						try (FileWriter writer = new FileWriter(file);
							 BufferedWriter bw = new BufferedWriter(writer)) {
							bw.write("userId="+userName);
							bw.newLine();
							bw.write("password="+password);
							bw.newLine();
							// add more user IDs and passwords as required
						} catch (IOException e) {
							e.printStackTrace();
						}

						// this.logger.info("TrakmeServer authenticated successfully with user" + userName);
						// System.out.println("Status Code : " + 200);
					}
				} else {
					responseAuthenticationPojo.setMessage("Unable to authenticate user");
					responseAuthenticationPojo.setStatusCode(500);
					// this.logger.debug("Unable to authenticate user, response is null");
				}
			} catch (Exception e) {
				readCredentialsFromLocal(userName,password);
//				e.printStackTrace();
//				responseAuthenticationPojo.setMessage("Unable to authenticate user");
//				responseAuthenticationPojo.setStatusCode(500);
				// this.logger.info("Unable to authentcate user");
			}
			return responseAuthenticationPojo;
		}

	}

	//----------------

	public void authenticateProxy(){
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

		String proxyUser = null;
		String proxyPassword = null;

//            System.setProperty("http.maxRedirects", "100");

		String proxyAddress = null;
		String proxyPort = null;
		// set up proxy authentication
		try {
			input = new FileInputStream(filePath+"proxy.properties");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		proxyUser = properties.getProperty("proxyUser");
		proxyPassword = properties.getProperty("proxyPassword");

		proxyAddress = properties.getProperty("proxyAddress");
		proxyPort = properties.getProperty("proxyPort");

		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";

		System.setProperty("http.proxyHost", proxyAddress);
		System.setProperty("http.proxyPort", proxyPort );
		System.setProperty("http.proxyUser", proxyUser);
		System.setProperty("http.proxyPassword", proxyPassword);
		System.setProperty("http.agent", userAgent);

		Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));
//		String encodedUserPwd = new String(Base64.encodeBase64((proxyUser+":"+proxyPassword).getBytes()));
//		Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));

// set up proxy properties
//		String proxyAddress = properties.getProperty("proxyAddress");
//		String proxyPort = properties.getProperty("proxyPort");
//		System.setProperty("http.proxyHost", proxyAddress);
//		System.setProperty("http.proxyPort", proxyPort);
	}

	public Boolean readCredentialsFromLocal(String userName, String password){
		String userId=null;
		String pass = null;

//		System.out.println("inside the catch");
		try {
			input = new FileInputStream(filePath+"user.properties");
			properties.load(input);
			userId = properties.getProperty("userId");
			pass = properties.getProperty("password");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("userId from file : "+userId);
		System.out.println("password from file : "+pass);
		System.out.println("userId from user : "+userName);
		System.out.println("password from user : "+password);

		if (userId.equalsIgnoreCase(userName) && pass.equalsIgnoreCase(password)){
			System.out.println("inside the if condition");
			responseAuthenticationPojo.setStatusCode(200);
			System.out.println("PROPERTY FILE : user authenticate successfully with user name : "+userName);
			return true;
		}else {
			responseAuthenticationPojo.setMessage("Invalid username or password");
			return false;
		}
	}

	public ResponseFieldTestingCardInfos fetchWorkOrderInfo(String userName) {

		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/getftIccid?usrId="
					+ userName;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpGet get = new HttpGet(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(get);
			// String responseString = "{ \"responseFieldTestingCardPojos\": [ {
			// \"cardIccid\": \"8991000905506201104F\", \"cardTestingPercentage\": 0.0,
			// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 29,
			// \"woId\": 908, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
			// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
			// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 55,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
			// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
			// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 56,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
			// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
			// \"OTA_TESTING\", \"fieldTestingCardStageId\": 57,
			// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
			// \"8991000905506200510F\", \"cardTestingPercentage\": 0.0,
			// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
			// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
			// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
			// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
			// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
			// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
			// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
			// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
			// \"fieldTestingStageStatus\": null } ] } ] }";
			if (response.getEntity() != null) {
				String responseString = EntityUtils.toString(response.getEntity());

				// String responseString = "{ \"responseFieldTestingCardPojos\": [ {
				// \"cardIccid\": \"8991000905506201104F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 29,
				// \"woId\": 908, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 55,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 56,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 57,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905506200510F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905506200387F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700780F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700764F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700566F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700749F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905506200585F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] } ] }";
				ResponseFieldTestingCardInfos serverResponse = (ResponseFieldTestingCardInfos) gson
						.fromJson(responseString, ResponseFieldTestingCardInfos.class);

				return serverResponse;
			} else {
				return null;
			}

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

		finally {

			try {

				client.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	public void updateWOStatus(String woID, String iccid, String status, String userName) {
		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/client/wo/status?usrId="
					+ userName + "&woId=" + woID + "&iccid=" + iccid + "&status=" + status;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpPost post = new HttpPost(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateCardStageStatus(String woID, String iccid, int cardStageId, String status, String userName) {
		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/client/card/status?usrId="
					+ userName + "&woId=" + woID + "&iccid=" + iccid + "&status=" + status + "&cardStageId="
					+ cardStageId;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpPost post = new HttpPost(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateCardStageCounter(String woID, String iccid, int cardStageId, long counter, String userName) {
		System.out.println("### current counter which is being sent to server : " + counter);
		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/client/card/counter?usrId="
					+ userName + "&woId=" + woID + "&iccid=" + iccid + "&counter=" + counter + "&cardStageId="
					+ cardStageId;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpPost post = new HttpPost(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ResponseProfileTestingConfig getProfileTestingConfig(String iccid, String woId, String userName) {
		CloseableHttpClient client = HttpClients.createDefault();
		ResponseProfileTestingConfig responseProfileTestingConfig = new ResponseProfileTestingConfig();
		try {

			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
					+ userName + "&woId=" + woId;

			URL url = new URL(completeUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);


			connection.connect();
			int statusCode = connection.getResponseCode();
			System.out.println("Status : " + statusCode);

			if (statusCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String responseString = bufferedReader.readLine();
				Gson gson = new Gson();
				ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = gson.fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
				responseProfileTestingConfig.setFileContentConfig(responseFieldTestingProfileConfigPojo.getFileContentConfigs());
				responseProfileTestingConfig.setFileSystemConfig(responseFieldTestingProfileConfigPojo.getFileSystemConfigs());
				bufferedReader.close();
				inputStream.close();
			}

			connection.disconnect();
			return responseProfileTestingConfig;


			// this.logger.debug("Calling  Server : " + completeUrl);

//			HttpGet get = new HttpGet(completeUrl);
//
//			Gson gson = new Gson();
//			CloseableHttpResponse response = client.execute(get);
//
//			System.out.println("Status : "+response.getStatusLine());
//
////			String responseString = "{\"fileSystemConfig\":[\"2F05,3F00,T,ALWAYS,CHV1,ADM,ADM,YES,NA,NA,8\",\"2FE2,3F00,T,ALWAYS,NEVER,ADM,ADM,NO,NA,NA,10\",\"2F00,3F00,LF,ALWAYS,ADM,ADM,ADM,YES,53,1,53\"],\"fileContentConfig\":[\"2F05,3F00,T,ALWAYS,1,1,FFFFFFFFFFFFFFFF\",\"2FE2,3F00,T,ALWAYS,1,1,ICCIDI\",\"2F00,3F00,LF,ALWAYS,1,1,41697274656C203447\"]}";
//			// this.System.out.println("Sever Response : " + responseString);
//			String responseString = EntityUtils.toString(response.getEntity());
//
//			ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = (ResponseFieldTestingProfileConfigPojo) gson
//					.fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
//			responseProfileTestingConfig
//					.setFileContentConfig(responseFieldTestingProfileConfigPojo.getFileContentConfigs());
//			responseProfileTestingConfig
//					.setFileSystemConfig(responseFieldTestingProfileConfigPojo.getFileSystemConfigs());


//			return responseProfileTestingConfig;

		}catch (SocketTimeoutException e2){
			System.out.println("Request timed out");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int sendReportsToServer(RequestSimVerificationCardPojos pojoList){


		// Create an instance of HttpClient
		HttpClient client = HttpClientBuilder.create().build();
		HttpURLConnection conn = null;
		int statusCode = 0;

		try {
		// Set the URL of the API
		String completeUrl = "http://103.228.113.86:32100/svr/receive";
		URL	url = new URL(completeUrl);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");


			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);


			conn.setRequestProperty("Content-Type", "application/json");

		conn.setDoOutput(true);
		conn.setDoInput(true);

		Gson gson = new Gson();
		String json = gson.toJson(pojoList);

		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			writer.write(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Get the response code from the connection

		try {
			statusCode = conn.getResponseCode();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Status code : "+statusCode);

		} catch (SocketTimeoutException e) {
			System.out.println("Request timed out");
		} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (conn != null) {
			conn.disconnect();
		}
	}

//		// Convert the pojo to a JSON string using Gson library
//		Gson gson = new Gson();
//		String json = gson.toJson(pojoList);
//
//		// Create an instance of HttpPost with the URL
//		HttpPost post = new HttpPost(url);
//
//		// Set the content type of the request to application/json
//		post.setHeader("Content-type", "application/json");
//
//		// Set the JSON string as the content of the request
//		try {
//			post.setEntity(new StringEntity(json));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// Execute the request and get the response
//		HttpResponse response = null;
//		try {
//			response = client.execute(post);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		int statusCode = response.getStatusLine().getStatusCode();
//
//		System.out.println("Status code : "+statusCode);
//
//
////		// Convert the response to a string
////		String responseString = null;
////		try {
////			responseString = EntityUtils.toString(response.getEntity());
////		} catch (IOException e) {
////			throw new RuntimeException(e);
////		}
////
////		// Print the response
////		System.out.println(responseString);

		return statusCode;

	}

	public ResponseTestingConfig getTestingConfig(String iccid, String woId, String userName) {
		CloseableHttpClient client = HttpClients.createDefault();
		ResponseTestingConfig responseTestingConfig = new ResponseTestingConfig();
		try {

			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
					+ userName + "&woId=" + woId;


			URL url = new URL(completeUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);

			connection.connect();
			int statusCode = connection.getResponseCode();
			System.out.println("Status : " + statusCode);

			if (statusCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String responseString = bufferedReader.readLine();
				Gson gson = new Gson();
				ResponseTestingConfig responseFieldTestingConfigPojo = gson.fromJson(responseString, ResponseTestingConfig.class);
				responseTestingConfig
					.setFileVerificationSystemConfigs(responseFieldTestingConfigPojo.getFileVerificationSystemConfigs());
			responseTestingConfig
					.setFileVerificationContentConfigs(responseFieldTestingConfigPojo.getFileVerificationContentConfigs());
				bufferedReader.close();
				inputStream.close();
			}

			connection.disconnect();
			return responseTestingConfig;

//			URL url = new URL(completeUrl);
//
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//			connection.setRequestMethod("GET");
//
//			int responseCode = connection.getResponseCode();
//			InputStream inputStream = null;
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				inputStream = connection.getInputStream();
//			} else {
//				inputStream = connection.getErrorStream();
//			}
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//			StringBuilder responseBuilder = new StringBuilder();
//			String line;
//			while ((line = reader.readLine()) != null) {
//				responseBuilder.append(line);
//			}
//			String responseString = responseBuilder.toString();
//
//			// Parse the JSON response using Gson library
//			Gson gson = new Gson();
//			responseTestingConfig = gson.fromJson(responseString, ResponseTestingConfig.class);
//
//			return responseTestingConfig;

			// this.logger.debug("Calling  Server : " + completeUrl);

//			HttpGet get = new HttpGet(completeUrl);
//
//			Gson gson = new Gson();
//			CloseableHttpResponse response = client.execute(get);
////			String responseString = "{\"fileSystemConfig\":[\"2F05,3F00,T,ALWAYS,CHV1,ADM,ADM,YES,NA,NA,8\",\"2FE2,3F00,T,ALWAYS,NEVER,ADM,ADM,NO,NA,NA,10\",\"2F00,3F00,LF,ALWAYS,ADM,ADM,ADM,YES,53,1,53\"],\"fileContentConfig\":[\"2F05,3F00,T,ALWAYS,1,1,FFFFFFFFFFFFFFFF\",\"2FE2,3F00,T,ALWAYS,1,1,ICCIDI\",\"2F00,3F00,LF,ALWAYS,1,1,41697274656C203447\"]}";
//			// this.System.out.println("Sever Response : " + responseString);
//			String responseString = EntityUtils.toString(response.getEntity());
//
////			System.out.println(responseString.toString());
//
//			ResponseTestingConfig responseFieldTestingConfigPojo = (ResponseTestingConfig) gson
//					.fromJson(responseString, ResponseTestingConfig.class);
//
//			responseTestingConfig
//					.setFileVerificationSystemConfigs(responseFieldTestingConfigPojo.getFileVerificationSystemConfigs());
//			responseTestingConfig
//					.setFileVerificationContentConfigs(responseFieldTestingConfigPojo.getFileVerificationContentConfigs());
//
//			return responseTestingConfig;


		} catch (SocketTimeoutException e) {
			System.out.println("Request timed out");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String sendLogsToServer(List<RequestClientLogPojo> requestLogPojos) {

//
//		CloseableHttpClient client = HttpClients.createDefault();
//		try {
//
//			String logPushUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/logs/post";
//			HttpPost post = new HttpPost(logPushUrl);
//			RequestClientLogsPojo pushRequest = new RequestClientLogsPojo();
//			pushRequest.setRequestClientLogPojos(requestLogPojos);
//
//			Gson gson = new Gson();
//
//			StringEntity input = new StringEntity(gson.toJson(pushRequest));
//			input.setContentType("application/json");
//			post.setEntity(input);
//
//			// this.logger.info("Sending logs to server");
//
//			CloseableHttpResponse response = client.execute(post);
//
//			String responseString = EntityUtils.toString(response.getEntity());
//
////			  ServerResponseLogPojo serverResponse = (ServerResponseLogPojo) gson.fromJson(responseString, ServerResponseLogPojo.class);
//			String status = "OK";
//			return status;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		finally {
//			try {
//				client.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}

	public ResponseStressTestingConfig getStressTestingConfig(String iccid, String woId, String userName) {

		CloseableHttpClient client = HttpClients.createDefault();
		ResponseStressTestingConfig responseStressTestingConfig = new ResponseStressTestingConfig();
		HttpURLConnection connection = null;

		try {
			String completeUrl = "http://103.228.113.86:32100/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
					+ userName + "&woId=" + woId;

			URL url = new URL(completeUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);

			connection.connect();
			int statusCode = connection.getResponseCode();
			System.out.println("Status : " + statusCode);

			if (statusCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String responseString = bufferedReader.readLine();
				Gson gson = new Gson();
				ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = gson.fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
				responseStressTestingConfig.setApduList(responseFieldTestingProfileConfigPojo.getStressTestingApdus());
				responseStressTestingConfig.setLoopCount(responseFieldTestingProfileConfigPojo.getStressTestingLoopCount());
				responseStressTestingConfig.setStartCounter(1);
				bufferedReader.close();
				inputStream.close();
			}

			connection.disconnect();


			// this.logger.debug("Calling  Server : " + completeUrl);

//			HttpGet get = new HttpGet(completeUrl);
//
//			Gson gson = new Gson();
//			CloseableHttpResponse response = client.execute(get);
//			// String responseString =
//			// "{\"apduList\":[\"RESET\",\"A0A40000023F00\",\"A0A40000027F20\",\"A0A40000026F7E\"],\"loopCount\":10000,\"startCounter\":1}";
//			// this.System.out.println("Sever Response : " + responseString);
//			String responseString = EntityUtils.toString(response.getEntity());
//			ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = (ResponseFieldTestingProfileConfigPojo) gson
//					.fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
//			responseStressTestingConfig.setApduList(responseFieldTestingProfileConfigPojo.getStressTestingApdus());
//			responseStressTestingConfig.setLoopCount(responseFieldTestingProfileConfigPojo.getStressTestingLoopCount());
//			responseStressTestingConfig.setStartCounter(1);
			
			return responseStressTestingConfig;

		} catch (SocketTimeoutException e) {
			System.out.println("Request timed out");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public boolean checkUserAccessibility (String userId) {
//        String version = null;

		String isUserAccessed = null;
		try {
			URL url = new URL("http://103.228.113.86:32100/trakmeserver/simverify/singleCard/userAccess/" + userId + ".txt");

			HttpURLConnection conn = null;
//			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				isUserAccessed = line;
			}
			reader.close();
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}


		if (isUserAccessed.equalsIgnoreCase("yes")){
			return true;
		}else {
			return false;
		}


	}

	public ResponseUserDataInfos getUserByEmail(String userName){

		ResponseUserDataInfos responseUserDataInfos = new ResponseUserDataInfos();

		String USERNAME = "hitesh.khanna@airtel.com";
		String PASSWORD = "hihite49";
		String encodedHeader = Base64.getEncoder()
				.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));

		System.out.println(encodedHeader);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpURLConnection connection = null;

		try {
			String completeUrl = "http://103.228.113.86:32100/svr/api/external/user/getByEmail?usrEmail=" + userName;

			URL url = new URL(completeUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Basic " + encodedHeader);
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);

			connection.connect();
			int statusCode = connection.getResponseCode();
			System.out.println("Status : " + statusCode);

			if (statusCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String responseString = bufferedReader.readLine();
				Gson gson = new Gson();
				responseUserDataInfos = gson.fromJson(responseString, ResponseUserDataInfos.class);
				bufferedReader.close();
				inputStream.close();
			}

			connection.disconnect();
			return responseUserDataInfos;

		} catch (SocketTimeoutException e) {
			System.out.println("Request timed out");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	public int updateUserData(String userName, int id, String requestType, int flag, String version){

		ResponseUserDataInfos responseUserDataInfos = new ResponseUserDataInfos();

		String USERNAME = "hitesh.khanna@airtel.com";
		String PASSWORD = "hihite49";
		String encodedHeader = Base64.getEncoder()
				.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));

		System.out.println(encodedHeader);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpURLConnection connection = null;

		try {
			String completeUrl = "http://103.228.113.86:32100/svr/api/external/user/updateUserData";

			URL url = new URL(completeUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Basic " + encodedHeader);

//			connection.setRequestProperty("id", String.valueOf(id));
//			connection.setRequestProperty("updateRequestType",requestType);
//			connection.setRequestProperty("usrDataFlag", String.valueOf(flag));
//			connection.setRequestProperty("usrDataVersion",version);
//			connection.setRequestProperty("usrEmail",userName);

			connection.setDoOutput(true);

			// Construct the request body as a Map
			Map<String, Object> requestBodyMap = new HashMap<>();
			requestBodyMap.put("id", id);
			requestBodyMap.put("updateRequestType", requestType);
			requestBodyMap.put("usrDataFlag", flag);
			requestBodyMap.put("usrDataVersion", version);
			requestBodyMap.put("usrEmail", userName);

			// Convert the Map to a JSON string
			String requestBody = new Gson().toJson(requestBodyMap);

			connection.setRequestProperty("Content-Type", "application/json");

			try (OutputStream outputStream = connection.getOutputStream()) {
				byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
				outputStream.write(requestBodyBytes, 0, requestBodyBytes.length);
			}

			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);

			connection.connect();
			int statusCode = connection.getResponseCode();
			System.out.println("Status : " + statusCode);

//			if (statusCode == HttpURLConnection.HTTP_OK) {
//				InputStream inputStream = connection.getInputStream();
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//				String responseString = bufferedReader.readLine();
//				Gson gson = new Gson();
//				responseUserDataInfos = gson.fromJson(responseString, ResponseUserDataInfos.class);
//				bufferedReader.close();
//				inputStream.close();
//			}

			connection.disconnect();
//			return responseUserDataInfos;
			return statusCode;

		} catch (SocketTimeoutException e) {
			System.out.println("Request timed out");
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


}
