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

	@GetMapping("/welcome")
	public String welcome() {
		return "<h1>Welcome to Spring Security Demo</h1>";
	}
}
