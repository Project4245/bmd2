package com.bmd.services;

import java.time.LocalDateTime;
import java.util.List;

import com.bmd.entity.Users;
import com.bmd.payload.UserDto;

public interface UserService {

	UserDto registerNewUser(UserDto user, int rId);
	
	UserDto createUser(UserDto user);

	UserDto updateUser(UserDto user, Integer userId);

	UserDto getUserById(Integer userId);

	List<UserDto> getAllUsers();

	void deleteUser(Integer userId);
	
	UserDto findByMobileNumber(String mobileNumber);
	
//	String findByMobileNumber(String mobileNumber);
	

	public boolean isMobileNumberRegistered(String mobileNumber);
	public void storeOtp(String mobileNumber, String otp, LocalDateTime otpExpiration);
//	public UserDto getUsersByMobileNumber(String mobileNumber);

	UserDto getUserWithDoctorById(Integer userId);

	UserDto getUserWithPatientById(Integer userId);
}
