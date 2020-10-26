package io.javabrains.springsecurityjwt.models;

/**
 * Model class for returning the user output by embedding JWT inside it
 * 
 * @author Aniket Bharsakale
 */
public class AuthenticationResponse {

	private String javaWebToken;

	public String getJavaWebToken() {
		return javaWebToken;
	}

	public AuthenticationResponse(String javaWebToken) {
		this.javaWebToken = javaWebToken;
	}

}
