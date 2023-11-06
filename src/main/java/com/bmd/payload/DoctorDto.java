package com.bmd.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class DoctorDto {

	private int id;
	
	private String drName;
	private String drEmail;
	private String hospitalName;
	private String hospitalEmail;
	private String hospitalAddress;
	private String drDegree;
	private String hospitalFacilty;
	private String specilist;
	private String normalAppoinmentFees;
	private String emergencyAppoinmentFees;

//	private UserDto userDto;
}
