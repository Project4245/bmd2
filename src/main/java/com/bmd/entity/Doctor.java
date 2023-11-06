package com.bmd.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctor")
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @OneToOne(mappedBy = "doctor",fetch = FetchType.EAGER)
//    private Users users;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

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
    
}
