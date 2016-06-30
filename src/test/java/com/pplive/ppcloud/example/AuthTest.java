package com.pplive.ppcloud.example;

import com.pplive.ppcloud.auth.Auth;

public class AuthTest {
	
	public static void main(String[] args) {
		String accessKey = "oDgJmy1-HHgSiCvCB4-m5irVU6BKjUkaTeyP4axA";
		String secretKey = "FUAqHxu0_MJB1kZREov0UJ9mChQtS8DyGXad0oec";
		Auth auth = new Auth(accessKey, secretKey);
		
		String accessToken = auth.generate();
		
		System.out.println(String.format("accessToken: %s", accessToken));
	}
}
