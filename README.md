# Spring Security - JWT(Java Web Token) Demo

Here, we have created a Spring Boot project with Spring Security to implement JWT based authentication and authorization.
To demonstrate this we have created an endpoint that can authenticate and return a JWT.
Later we filter the incoming requests for JWT in the Authorization header and then authorize requests with valid JWT.
Moreover, we have not connected to any database or have any CRUD operations to save time & focus on the JWT concept.

## Bare minimum required for this is as follows:
- Java v1.8 and above(I have used Java SE 11)
- STS/Intellij/Eclipse/Notepad++/VS or any suitable IDE/editor for writting & viewing code
- Postman client for executing the POST and GET requests
- Git bash/Github desktop to perform git operations

## Following are the Sample POST and GET cURL requests with sample data for better understanding:

1) `POST API`: localhost:8080/authenticate

Sample cURL `POST request`

curl --location --request POST 'localhost:8080/authenticate' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=13455FC4D060D7C85B0E09B1ADC8E4CB' \
--data-raw '{
"username":"fooUserId",
"password":"fooPassword"
}'

Sample expected JSON `POST response`
{
    "javaWebToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb29Vc2VySWQiLCJleHAiOjE2MDM3NjUzODIsImlhdCI6MTYwMzcyOTM4Mn0.jb6WqQ27LXniEGakN8s5weCZp5mLDRP8jQqB49wBlZk"
} 

2) `GET API`: localhost:8080/welcome

Sample cURL `GET request`

curl --location --request GET 'localhost:8080/welcome' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb29Vc2VySWQiLCJleHAiOjE2MDM3NjUzODIsImlhdCI6MTYwMzcyOTM4Mn0.jb6WqQ27LXniEGakN8s5weCZp5mLDRP8jQqB49wBlZk' \
--header 'Cookie: JSESSIONID=13455FC4D060D7C85B0E09B1ADC8E4CB' \
--header 'Content-Type: text/plain' \
--data-binary '@'

Sample expected JSON `GET response`

Welcome to Java Web Tokens(JWT) - spring security demo project home page!
