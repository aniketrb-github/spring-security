package io.jbrains.springsecjpa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jbrains.springsecjpa.model.CustomUserDetails;
import io.jbrains.springsecjpa.model.Users;
import io.jbrains.springsecjpa.repository.UsersRepository;

/**
 * This implements spring's UserDetailsService
 *
 * @author Aniket Bharsakale
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	// Auto-wired this self-implemented JPA Repository so as to fetch data from DB
	@Autowired
	UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<Users> optionalUserDetails = usersRepository.findByName( userName );
		
		optionalUserDetails.orElseThrow(() -> new UsernameNotFoundException("Username not found!") );
		
		return optionalUserDetails.map( users -> { return new CustomUserDetails(users); } ).get();
	}

}
