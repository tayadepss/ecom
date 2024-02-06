package com.parmeshwar.ecom.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication. AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails. UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.parmeshwar.ecom.dto.AuthenticationRequest;
import com.parmeshwar.ecom.dto.SignupRequest;
import com.parmeshwar.ecom.dto.UserDto;
import com.parmeshwar.ecom.entity.User;
import com.parmeshwar.ecom.repository.UserRepository;
import com.parmeshwar.ecom.services.auth.AuthService;
import com.parmeshwar.ecom.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
public class AuthController {
	private static final String HEADER_STRING = "Authorizaion";
	private static final String TOCKEN_PREFIX = "Bearer";
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final AuthService authService;
	
	@PostMapping("/authenticate")
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,HttpServletResponse response)throws JSONException, IOException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password.");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername()); 
		Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
		final String jwt=jwtUtil.generateToken(userDetails.getUsername());
		
		if(optionalUser.isPresent()){
			response.getWriter().write(new JSONObject()
					.put("userId", optionalUser.get().getId())
					.put("role", optionalUser.get().getRole())
					.toString()
			);		
			
			response.addHeader(HEADER_STRING,TOCKEN_PREFIX+jwt);
		}
	}
	
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
		if(authService.hasUserWithEmail(signupRequest.getEmail()))
			return new ResponseEntity<>("User already Exists",HttpStatus.NOT_ACCEPTABLE);
		
		UserDto userDto=authService.createUser(signupRequest);		
		return new ResponseEntity<>(userDto,HttpStatus.OK);
	}
	
}
