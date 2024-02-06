package com.parmeshwar.ecom.services.auth;

import com.parmeshwar.ecom.dto.SignupRequest;
import com.parmeshwar.ecom.dto.UserDto;

public interface AuthService {
	UserDto createUser(SignupRequest signupRequest);

	boolean hasUserWithEmail(String email);
}
