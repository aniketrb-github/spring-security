package io.javabrains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application
 */
@SpringBootApplication
public class SpringBootSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityApplication.class, args);
	}

}

/**
Root URL	: http://localhost:8080 
Logout URL	: http://localhost:8080/logout

For Role: User
Login: user 
Password: user
User Access	: http://localhost:8080/user

For Role: Admin
Login: admin 
Password: admin
Admin Access: http://localhost:8080/admin

*/