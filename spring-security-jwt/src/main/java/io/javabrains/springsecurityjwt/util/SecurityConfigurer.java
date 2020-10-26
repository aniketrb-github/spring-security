package io.javabrains.springsecurityjwt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.javabrains.springsecurityjwt.filters.JwtRequestFilter;
import io.javabrains.springsecurityjwt.service.MyUserDetailsService;

/**
 * Security configurer to configure security using the JWT
 *  
 * @author Aniket Bharsakale
 */
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	/**
	 * Sets the user credentials to the AuthenticationManagerBuilder object
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}
	
	/**
	 * We have used the NoOpPasswordEncoder, so as to treat credentials as is,
	 * and to not perform any hashing over it after it is received.   
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	/**
	 * an essential explicit spring config. required to notify spring-security 
	 * ie. we simply don't want spring to again authenticate our custom /authenticate API
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests().antMatchers("/authenticate").permitAll() // permit all requests with '/authenticate' in URL
		.anyRequest().authenticated()	// and any other URLs will be authenticated except the above
		.and().sessionManagement() // This tells the spring-sec not to bother creating a session
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// tells spring-sec to execute our filter before this mentioned filter is invoked
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	/**
	 * Spring will instantiate & use this as a bean in HomePageController.java
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
