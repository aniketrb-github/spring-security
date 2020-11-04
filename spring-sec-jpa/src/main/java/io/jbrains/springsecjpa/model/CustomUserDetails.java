package io.jbrains.springsecjpa.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * CustomUserDetails extends our Users.java & implements spring's UserDetails interface.
 * 
 * @author Aniket Bharsakale
 */
@SuppressWarnings("serial")
public class CustomUserDetails extends Users implements UserDetails {

	public CustomUserDetails(final Users users) {
		super( users );
	}

	/**
	 * At runtime spring-security uses this method to get the authorities of principal(current logged in user)
	 * and checks if the principal is authorized to perform or access the requested URL/operation.  
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Getting all roles for current user & creating a List for Spring Security for authorization
		return getRoles()
				.stream()
				.map( role -> new SimpleGrantedAuthority(role.getRole()) 
					).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
