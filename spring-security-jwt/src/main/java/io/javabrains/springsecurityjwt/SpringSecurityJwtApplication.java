package io.javabrains.springsecurityjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}
}

/**
 * 
 * 1) First the User is authenticated using POST request
 * On successful Login, the user will receive a Java Web Token in response
 * 
 * 2) For all subsequent requests thereafter, the user will send this JWT 
 * as a result of a validated user
 * 
 * Below are the 2 sample HTTP POST(LOGIN) and GET(HOME) request
 * 
curl --location --request POST 'localhost:8080/authenticate' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=13455FC4D060D7C85B0E09B1ADC8E4CB' \
--data-raw '{
"username":"fooUserId",
"password":"fooPassword"
}'
 
 CURL GET Request
curl --location --request GET 'localhost:8080/welcome' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb29Vc2VySWQiLCJleHAiOjE2MDM3NjUzODIsImlhdCI6MTYwMzcyOTM4Mn0.jb6WqQ27LXniEGakN8s5weCZp5mLDRP8jQqB49wBlZk' \
--header 'Cookie: JSESSIONID=13455FC4D060D7C85B0E09B1ADC8E4CB' \
--header 'Content-Type: text/plain' \
--data-binary '@'
  
 * 
 */
