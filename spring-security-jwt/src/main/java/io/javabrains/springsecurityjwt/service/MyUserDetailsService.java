package io.javabrains.springsecurityjwt.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A pretend service to return a hard-coded user for demo purpose
 * 
 * @author Aniket Bharsakale
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

	/**
	 * This method is invoked by the SecurityConfigurer 
	 * We simulate this like, we're receiving a username from the JWT/UI.
	 * Using the received user info. we check if user exist & retrieve the user details if any.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Here we may query the database to check if user exist and get user details
		// But instead, we're just returning hard-coded user details 
		return new User("fooUserId", "fooPassword", new ArrayList<>());
	}

}
