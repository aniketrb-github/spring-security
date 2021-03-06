package io.javabrains.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration class which deals with Spring Security
 * 
 * @author Aniket Bharsakale
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String ADMIN = "ADMIN";
	private static final String USER = "USER";
	
	@Autowired
	DataSource dataSource;
	
	/**
	 * An inherited method from class WebSecurityConfigurerAdapter
	 * Override this method for "in-memory authentication implementation"
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource); // spring-sec points to embedded H2 database
			/*.withDefaultSchema()	// and populates following data at start-up time
			.withUser(
					User.withUsername("user")
					.password("pass")
					.roles(USER)	
					)
			.withUser( 
					User.withUsername("admin")
					.password("pass")
					.roles(ADMIN) 
			);*/
	}
	
	/**
	 * An inherited method from class WebSecurityConfigurerAdapter
	 * Override this method for "in-memory authorization implementation"
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/admin").hasRole(ADMIN) // for /admin only admin role is allowed
		.antMatchers("/user").hasAnyRole(ADMIN, USER) // for /user only user & admin roles allowed 
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
