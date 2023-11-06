package com.bmd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmd.entity.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

	Users findByMobileNumber(String mobileNumber);

	boolean existsByMobileNumber(String mobileNumber);
}
