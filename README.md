Spring Boot OTP (One time password authentication)
Spring Boot project with demonstration of OTP authentication technique.

Running the project
Create database with name otp
Open terminal and navigate to your project
Type command mvn install
Type command mvn spring-boot:run
Check application running state
Route: http://localhost:8080/

Available profiles
Development profile (dev)
Production profile (prod)
Authentication
Route: /auth/authenticate
Method: POST
Content-Type: application/json
Request payload: { username: "admin", password: "admin" }
Response: { id_token: "token_hash" }

OTP routes that you can called after getting access token
1.Generate OTP and send it to e-mail 
Route: /api/otp/generate 
Method: POST 
Empty request body in this case.

2.Validate OTP 
Route: /api/otp/validate 
Method: POST 
Example Request Payload: { "otp": "your otp number" }

