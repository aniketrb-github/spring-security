package io.jbrains.springsecjpa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jbrains.springsecjpa.repository.UsersRepository;

/**
 * The main Spring Security Configuration class for authentication & authorization
 */
@EnableGlobalMethodSecurity(prePostEnabled = true) // this is for the PreAuthorize annotation used in controller class
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UsersRepository.class) // special annotation for Repository to interact with DB
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	// Auto-wired spring's interface since we have an implementation for it: CustomUserDetailsService
	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder( new PasswordEncoder() {
			// Here we can use the BCryptEncoder Java class instead			
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return true;
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
		});
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("**/auth/**").authenticated() // for requests with 'auth' in URL, authenticate them
		.anyRequest().permitAll()	// for any other requests allow with authentication
		.and()
		.formLogin().permitAll(); // form based login(spring's default login page)
	}
}
