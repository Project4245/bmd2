package com.bmd.payload;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.bmd.entity.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

	private int id;

	@NotEmpty
	@Size(min = 4, message = "Username must be min of 4 characters !!")
	private String name;

	@NotEmpty(message = "Email is required !!")
	@Email
	@Column(unique = true)
	private String email;

	@NotEmpty
	@Column(unique = true)
	@Size(min = 4, message = "Username must be min of 4 characters !!")
	private String mobileNumber;

	private LocalDateTime otpExpiration;

	private String otp;

	private Set<RoleDto> roles = new HashSet<>();
	private DoctorDto doctorDto = new DoctorDto();

}
