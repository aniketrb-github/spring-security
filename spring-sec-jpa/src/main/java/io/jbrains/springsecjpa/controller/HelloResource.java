package io.jbrains.springsecjpa.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

	/**
	 * Unauthenticated ReST end-point
	 * @return String
	 */
    @GetMapping("/all")
    public String hello() {
        return ("<h1>TechPrimers Login & Registration page!</h1>");
    }

    /**
     * Authenticated ReST end-point
     * @return String
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/auth/all")
    public String secured() {
        return ("<h1>Welcome to TechPrimers HomePage!!!</h1>");
    }

}

