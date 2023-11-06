package com.bmd.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmd.entity.Users;
import com.bmd.exceptions.ApiException;
import com.bmd.payload.JwtAuthRequest;
import com.bmd.payload.JwtAuthResponse;
import com.bmd.payload.UserDto;
import com.bmd.repository.UserRepo;
import com.bmd.security.JwtTokenHelper;
import com.bmd.services.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/bmd")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping("/send-otp")
	public ResponseEntity<Object> sendOtp(@RequestParam String mobileNumber) {
		if (userService.isMobileNumberRegistered(mobileNumber)) {
			// Generate a random 6-digit OTP
			String otp = generateOTP();

			// Simulate sending OTP to the mobile number (replace with actual SMS sending
			// logic)
			System.out.println("Sending OTP to " + mobileNumber + ": " + otp);

			// Set OTP and expiration in the database
			LocalDateTime otpExpiration = LocalDateTime.now().plusMinutes(1); // OTP expires in 5 minutes
			userService.storeOtp(mobileNumber, otp, otpExpiration);
			return new ResponseEntity<Object>(Map.of("message", otp), HttpStatus.OK);
//			return ResponseEntity.ok("Otp is :: " + otp);
		} else {
			return new ResponseEntity<Object>(Map.of("message", "Mobile number not registered."),
					HttpStatus.BAD_GATEWAY);

		}

	}

	// Helper method to generate a random 6-digit OTP
	private String generateOTP() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

		this.authenticate(request.getMobileNumber(), request.getOtp());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getMobileNumber());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.mapper.map((Users) userDetails, UserDto.class));
		System.out.println("Invalid");
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid Detials !!");
			throw new ApiException("Invalid username or password !!");
		}

	}

	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto, @RequestParam int id) {
		UserDto registeredUser = this.userService.registerNewUser(userDto, id);
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}

	// get loggedin user data
	@GetMapping("/current-user/")
	public ResponseEntity<UserDto> getUser(Principal principal) {
		Users user = this.userRepo.findByMobileNumber(principal.getName());
		return new ResponseEntity<UserDto>(this.mapper.map(user, UserDto.class), HttpStatus.OK);
	}

}
