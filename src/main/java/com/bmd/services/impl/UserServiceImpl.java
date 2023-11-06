package com.bmd.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bmd.config.AppConstants;
import com.bmd.entity.Doctor;
import com.bmd.entity.Role;
import com.bmd.entity.Users;
import com.bmd.exceptions.ResourceNotFoundException;
import com.bmd.exceptions.SingleRegistrationException;
import com.bmd.payload.DoctorDto;
import com.bmd.payload.RoleDto;
import com.bmd.payload.UserDto;
import com.bmd.repository.RoleRepo;
import com.bmd.repository.UserRepo;
import com.bmd.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;
	
	private UserDto convertToDto1(Users users, Doctor doctor) {
	    UserDto userDto = new UserDto();

	    userDto.setId(users.getId());
	    userDto.setName(users.getName());
	    userDto.setEmail(users.getEmail());
	    userDto.setMobileNumber(users.getMobileNumber());
	    userDto.setOtpExpiration(users.getOtpExpiration());
	    userDto.setOtp(users.getOtp());
	    
	    // Check if the user has the "DOCTOR" role
	    boolean isDoctor = users.getRoles().stream().anyMatch(role -> "DOCTOR".equals(role.getName()));

	    // Include roles in all cases
	    userDto.setRoles(users.getRoles().stream().map(role -> {
	        RoleDto roleDto = new RoleDto();
	        roleDto.setId(role.getId());
	        roleDto.setName(role.getName());
	        return roleDto;
	    }).collect(Collectors.toSet()));

	    // Include doctor information only if the user is a doctor
	    if (isDoctor) {
	        if (doctor != null) {
	            DoctorDto doctorDto = new DoctorDto();
	            doctorDto.setId(doctor.getId());
	            doctorDto.setDrName(doctor.getDrName());
	            doctorDto.setDrEmail(doctor.getDrEmail());
	            doctorDto.setHospitalName(doctor.getHospitalName());
	            doctorDto.setHospitalEmail(doctor.getHospitalEmail());
	            doctorDto.setHospitalAddress(doctor.getHospitalAddress());
	            doctorDto.setDrDegree(doctor.getDrDegree());
	            doctorDto.setHospitalFacilty(doctor.getHospitalFacilty());
	            doctorDto.setSpecilist(doctor.getSpecilist());
	            doctorDto.setNormalAppoinmentFees(doctor.getNormalAppoinmentFees());
	            doctorDto.setEmergencyAppoinmentFees(doctor.getEmergencyAppoinmentFees());

	            userDto.setDoctorDto(doctorDto);
	        }
	    } else {
	        userDto.setDoctorDto(null);
	    }

	    return userDto;
	}

	
	@Override
	public UserDto getUserWithPatientById(Integer userId) {
		Users users = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		if (users != null) {
			UserDto userDto = convertToDto1(users, users.getDoctor());
			return userDto;
		}

		return null;
	}
//	
//	private UserDto convertToDto1(Users users, Doctor doctor) {
//		UserDto userDto = new UserDto();
//
//		userDto.setId(users.getId());
//		userDto.setName(users.getName());
//		userDto.setEmail(users.getEmail());
//		userDto.setMobileNumber(users.getMobileNumber());
//		userDto.setOtpExpiration(users.getOtpExpiration());
//		userDto.setOtp(users.getOtp());
//		userDto.setRoles(users.getRoles().stream().map(role -> {
//			RoleDto roleDto = new RoleDto();
//			roleDto.setId(role.getId());
//			roleDto.setName(role.getName());
//			return roleDto;
//		}).collect(Collectors.toSet()));
//
//		return userDto;
//	}

	
	
	
	@Override
	public UserDto getUserWithDoctorById(Integer userId) {
		Users users = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		if (users != null) {
			UserDto userDto = convertToDto(users, users.getDoctor());
			return userDto;
		}

		return null;
	}

	private UserDto convertToDto(Users users, Doctor doctor) {
		UserDto userDto = new UserDto();

		userDto.setId(users.getId());
		userDto.setName(users.getName());
		userDto.setEmail(users.getEmail());
		userDto.setMobileNumber(users.getMobileNumber());
		userDto.setOtpExpiration(users.getOtpExpiration());
		userDto.setOtp(users.getOtp());
		userDto.setRoles(users.getRoles().stream().map(role -> {
			RoleDto roleDto = new RoleDto();
			roleDto.setId(role.getId());
			roleDto.setName(role.getName());
			return roleDto;
		}).collect(Collectors.toSet()));

		if (doctor != null) {
			// Populate doctor information if available
			DoctorDto doctorDto = new DoctorDto();
			doctorDto.setId(doctor.getId());
			doctorDto.setDrName(doctor.getDrName());
			doctorDto.setDrEmail(doctor.getDrEmail());
			doctorDto.setHospitalName(doctor.getHospitalName());
			doctorDto.setHospitalEmail(doctor.getHospitalEmail());
			doctorDto.setHospitalAddress(doctor.getHospitalAddress());
			doctorDto.setDrDegree(doctor.getDrDegree());
			doctorDto.setHospitalFacilty(doctor.getHospitalFacilty());
			doctorDto.setSpecilist(doctor.getSpecilist());
			doctorDto.setNormalAppoinmentFees(doctor.getNormalAppoinmentFees());
			doctorDto.setEmergencyAppoinmentFees(doctor.getEmergencyAppoinmentFees());

			userDto.setDoctorDto(doctorDto);
		}

		return userDto;
	}

	public boolean isMobileNumberRegistered(String mobileNumber) {
		return userRepo.findByMobileNumber(mobileNumber) != null;
	}

	public void registerUser(Users user) {
		userRepo.save(user);
	}

	public void storeOtp(String mobileNumber, String otp, LocalDateTime otpExpiration) {
		Users user = userRepo.findByMobileNumber(mobileNumber);
		user.setOtp(otp);
		user.setOtpExpiration(otpExpiration);
		userRepo.save(user);
	}

	public Users getUserByMobileNumber(String mobileNumber) {
		return userRepo.findByMobileNumber(mobileNumber);
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		Users user = this.dtoToUser(userDto);
		Users savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {

		Users user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		user.setName(userDto.getName());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setOtp(userDto.getOtp());
		user.setEmail(userDto.getEmail());

		Users updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updatedUser);
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {

		Users user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<Users> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		Users user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		this.userRepo.delete(user);

	}

	public Users dtoToUser(UserDto userDto) {
		Users user = this.modelMapper.map(userDto, Users.class);
		return user;
	}

	public UserDto userToDto(Users user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto findByMobileNumber(String mobileNumber) {
		Users user = userRepo.findByMobileNumber(mobileNumber);
		return userToDto(user);
	}

	@Override
	public UserDto registerNewUser(UserDto userDto, int rId) {

		Users user = this.modelMapper.map(userDto, Users.class);

		// encoded the password

//		 user.setOtp(this.passwordEncoder.encode(user.getOtp()));
		if (userRepo.existsByMobileNumber(userDto.getMobileNumber())) {
			throw new SingleRegistrationException("User with this mobile number is already registered.");
		} else {
			// roles

//		int id = 501;

			if (rId == AppConstants.DOCTOR) {
				Role role = this.roleRepo.findById(AppConstants.DOCTOR).get();

				user.getRoles().add(role);

				Users newUser = this.userRepo.save(user);
				return this.modelMapper.map(newUser, UserDto.class);

			} else if (rId == AppConstants.PATIENT) {
				Role role = this.roleRepo.findById(AppConstants.PATIENT).get();

				user.getRoles().add(role);

				Users newUser = this.userRepo.save(user);
				return this.modelMapper.map(newUser, UserDto.class);

			} else {
				System.out.println("Please Enter some value");

				return null;
			}
		}
	}
}
