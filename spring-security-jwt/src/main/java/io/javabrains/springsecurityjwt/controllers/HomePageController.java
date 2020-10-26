package io.javabrains.springsecurityjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.springsecurityjwt.models.AuthenticationRequest;
import io.javabrains.springsecurityjwt.models.AuthenticationResponse;
import io.javabrains.springsecurityjwt.service.MyUserDetailsService;
import io.javabrains.springsecurityjwt.util.JwtUtil;

/**
 * This controller provides a list of authenticated end-points, i.e a user needs
 * to be authenticated so as to use the below end-points
 * 
 * @author Aniket Bharsakale
 */
@RestController
public class HomePageController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * An authenticated API wherein only authenticated users can use this API
	 * @return
	 */
	@GetMapping({ "/welcome" })
	public String helloWorld() {
		return "Welcome to Java Web Tokens(JWT) - spring security demo project home page!";
	}

	/**
	 * UnAuthenticated API 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password!");
		}

		// create a JWT using the user details & return it in response
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		final String javaWebToken = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(javaWebToken));
	}
}
