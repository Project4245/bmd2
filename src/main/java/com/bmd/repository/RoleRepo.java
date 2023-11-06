package com.bmd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmd.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

}
