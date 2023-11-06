package com.bmd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmd.entity.Doctor;
import com.bmd.entity.Users;
import com.bmd.payload.UserDto;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
	Doctor findByUsers(Users users);

	Doctor findByDrEmail(String userEmail);
}
