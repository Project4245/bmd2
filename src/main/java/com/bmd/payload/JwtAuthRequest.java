package com.bmd.payload;

import lombok.Data;

@Data
public class JwtAuthRequest {

//	private String username;
//	
//	private String password;
//	
	private String mobileNumber;
	
	private String otp;
}
