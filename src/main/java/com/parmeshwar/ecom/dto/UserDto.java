package com.parmeshwar.ecom.dto;

import com.parmeshwar.ecom.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
	private Long id;
	private String email;
	private String name;
	private UserRole userRole;
}
