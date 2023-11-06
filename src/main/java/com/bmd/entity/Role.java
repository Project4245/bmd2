package com.bmd.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "role_table")
public class Role {

	@Id	
	private int id;
	
	private String name;
	
}
