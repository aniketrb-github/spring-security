package io.javabrains.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Set your configuration over the auth object
		auth.inMemoryAuthentication()
		.withUser("foo")
		.password("foo")
		.roles("USER")
		.and()
		.withUser("asd")
		.password("asd")
		.roles("ADMIN");
	}
	
	/**
	 * This currently allows the clear text passwords & does not perform
	 * any encoding over the passwords
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
