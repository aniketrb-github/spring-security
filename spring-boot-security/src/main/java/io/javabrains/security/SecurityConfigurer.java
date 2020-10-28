package io.javabrains.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration class which deals with Spring Security
 * 
 * @author Aniket Bharsakale
 */
@EnableWebSecurity	// Enables the security for this web based application
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	
	/**
	 * An inherited method from class WebSecurityConfigurerAdapter
	 * Override this method for "in-memory authentication implementation"
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Set your configuration over the AuthenticationManagerBuilder object
		auth.inMemoryAuthentication()
		.withUser("user")
		.password("user")
		.roles("USER")
		.and()
		.withUser("admin")
		.password("admin")
		.roles("ADMIN");
	}
	
	/**
	 * An inherited method from class WebSecurityConfigurerAdapter
	 * Override this method for "in-memory authorization implementation"
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/admin").hasRole("ADMIN") // for /admin only admin role is allowed
		.antMatchers("/user").hasAnyRole("USER", "ADMIN") // for /user only user & admin roles allowed 
		.antMatchers("/").permitAll() // for root URL allow all users
		.and()
		.formLogin(); // We need a form based Login for our users
	}
	
	/**
	 * This currently allows to save passwords in clear text format without any encoding or hashing
	 * Highly not recommended approach for industry standards
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() { return NoOpPasswordEncoder.getInstance(); }
}
