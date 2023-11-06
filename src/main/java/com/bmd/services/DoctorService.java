package com.bmd.services;

import com.bmd.payload.DoctorDto;

public interface DoctorService {

//	Doctor findDoctorByUsers(UserDto users);

	DoctorDto createDoctorProfile(DoctorDto doctorDto, Integer userId);
}
