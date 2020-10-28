package io.javabrains.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home page controller
 * 
 * @author Aniket Bharsakale
 */
@RestController
public class WelcomeController {

	/**
	 * Root URL to be accessed by any authenticated/unauthenticated user
	 * @return
	 */
	@GetMapping("/")
	public String welcome() {
		return "<h1>Welcome to Spring Security Demo</h1>";
	}
	
	/**
	 * Access allowed for User & Admin roles only
	 * @return
	 */
	@GetMapping("/user")
	public String welcomeUser() {
		return "<h1>Welcome USER to Spring Security Demo</h1>";
	}
	
	/**
	 * Access strictly allowed for Admin only
	 * @return
	 */
	@GetMapping("/admin")
	public String welcomeAdmin() {
		return "<h1>Welcome ADMIN to Spring Security Demo</h1>";
	}
}
