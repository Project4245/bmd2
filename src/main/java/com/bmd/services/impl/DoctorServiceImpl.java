package com.bmd.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmd.entity.Doctor;
import com.bmd.entity.Users;
import com.bmd.exceptions.DuplicateRegistrationException;
import com.bmd.exceptions.InvalidRoleException;
import com.bmd.exceptions.ResourceNotFoundException;
import com.bmd.payload.DoctorDto;
import com.bmd.repository.DoctorRepository;
import com.bmd.repository.UserRepo;
import com.bmd.services.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public DoctorDto createDoctorProfile(DoctorDto doctorDto, Integer userId) {
	    Users user = this.userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

	    // Check the user's roles to determine if they can become a doctor
	    boolean isDoctorRole = user.getRoles().stream()
	            .anyMatch(role -> "DOCTOR".equals(role.getName()));

	    if (isDoctorRole) {
	        String userEmail = user.getEmail();

	        // Check if a doctor with the same email already exists
	        Doctor existingDoctor = this.doctorRepository.findByDrEmail(userEmail);

	        if (existingDoctor != null) {
	            throw new DuplicateRegistrationException("Email is already registered as a doctor.");
	        }

	        // If no existing doctor with the same email is found, proceed to create the doctor profile.
	        Doctor doctor = this.modelMapper.map(doctorDto, Doctor.class);
	        doctor.setUsers(user);
	        doctor.setDrEmail(userEmail);
	        Doctor profile = this.doctorRepository.save(doctor);

	        return this.modelMapper.map(profile, DoctorDto.class);
	    } else {
	        // Handle the case where the user's role is not "DOCTOR"
	        throw new InvalidRoleException("User does not have the 'DOCTOR' role.");
	    }
	}

	
	
//	@Override
//	public DoctorDto createDoctorProfile(DoctorDto doctorDto, Integer userId) {
//	    Users user = this.userRepo.findById(userId)
//	            .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
//
//	    String userEmail = user.getEmail();
//
//	    // Check if a doctor with the same email already exists
//	    Doctor existingDoctor = this.doctorRepository.findByDrEmail(userEmail);
//
//	    if (existingDoctor != null) {
//	        
//	        throw new DuplicateRegistrationException("Email is already registered as a doctor.");
//	    }
//
//	    // If no existing doctor with the same email is found, proceed to create the doctor profile.
//	    Doctor doctor = this.modelMapper.map(doctorDto, Doctor.class);
//	    doctor.setUsers(user);
//	    doctor.setDrEmail(userEmail);
//	    Doctor profile = this.doctorRepository.save(doctor);
//
//	    return this.modelMapper.map(profile, DoctorDto.class);
//	}


//	@Override
//	public DoctorDto createDoctorProfile(DoctorDto doctorDto, Integer userId) {
//
//		Users user = this.userRepo.findById(userId)
//				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));
//
//		// Set the email from the user to the doctor
//		String userEmail = user.getEmail();
//
//		Doctor doctor = this.modelMapper.map(doctorDto, Doctor.class);
//		doctor.setUsers(user);
//		doctor.setDrEmail(userEmail);
//		Doctor profile = this.doctorRepository.save(doctor);
//
//		return this.modelMapper.map(profile, DoctorDto.class);
//
//	}

//	@Override
//	public Doctor createDoctorProfile(UserDto users, String specialization, String licenseNumber) {
//		Doctor doctor = new Doctor();
//		
//		doctor.setSpecialization(specialization);
//		doctor.setLicenseNumber(licenseNumber);
//
//		return doctorRepository.save(doctor);
//	}

//	@Override
//	public Doctor findDoctorByUsers(UserDto users) {
//		return doctorRepository.findByUsers(users);
//	}
}
