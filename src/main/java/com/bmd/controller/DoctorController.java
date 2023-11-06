package com.bmd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bmd.exceptions.DuplicateRegistrationException;
import com.bmd.exceptions.InvalidRoleException;
import com.bmd.payload.DoctorDto;
import com.bmd.services.DoctorService;
import com.bmd.services.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/bmd")
public class DoctorController {
	@Autowired
	private DoctorService doctorService;

	@Autowired
	private UserService userService; // Service for managing userss

//	@PreAuthorize("hasRole('DOCTOR')")
	@PostMapping("/doctor/{userId}")
	public ResponseEntity<?> createDoctorProfile(@RequestBody DoctorDto doctorDto, @PathVariable Integer userId) {

		try {
			DoctorDto createDoctorProfile = this.doctorService.createDoctorProfile(doctorDto, userId);
			return new ResponseEntity<>(createDoctorProfile, HttpStatus.CREATED);
		} catch (DuplicateRegistrationException ex) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Doctor is already registered");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}catch(InvalidRoleException ex){
			Map<String, String> response = new HashMap<>();
			response.put("message", "User does not have the 'DOCTOR' role.Only Doctor can register..");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);	
		}

//		DoctorDto createDoctorProfile = this.doctorService.createDoctorProfile(doctorDto, userId);
//		return new ResponseEntity<DoctorDto>(createDoctorProfile, HttpStatus.CREATED);
	}

//	@PostMapping("/create")
//	public ResponseEntity<Doctor> createDoctorProfile(@RequestBody DoctorProfileRequest profileRequest) {
//		// Retrieve the users based on users ID or mobile number
//		UserDto userDtos = userService.findByMobileNumber(getMobileNumber());
//
//		if (userDtos == null) {
//			return ResponseEntity.notFound().build();
//		}
//
//		Doctor doctor = doctorService.createDoctorProfile(userDto, profileRequest.getSpecialization(),
//				profileRequest.getLicenseNumber());
//
//		return ResponseEntity.ok(doctor);
//	}

//	@GetMapping("/users/{usersId}")
//	public ResponseEntity<Doctor> getDoctorByUsers(@PathVariable Integer usersId) {
//		UserDto users = userService.getUserById(usersId);
//		if (users == null) {
//			return ResponseEntity.notFound().build();
//		}
//
//		 Doctor doctor = doctorService.findDoctorByUsers(users);
//		 
//		if (doctor != null) {
//			return ResponseEntity.ok(doctor);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}
}
