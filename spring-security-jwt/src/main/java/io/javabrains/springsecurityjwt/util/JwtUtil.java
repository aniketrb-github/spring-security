package io.javabrains.springsecurityjwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A Utility class for JWT which provides several JWT utility methods
 * 
 * @author Aniket Bharsakale
 */
@Service
public class JwtUtil {

    private final String SECRET_KEY = "al4sdf3x432xcqs873dlkoetk2135";

    /**
     * returns back the user-name for the given token i.e 'subject'(key)
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * returns the value for the token 'expiration'(key)
     * 
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * returns the value for the given token
     * 
     * @param <T>
     * @param token is the key for which a value is extracted & returned
     * @param claimsResolver
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	/**
	 * checks if the JWT is expired
	 * 
	 * @param token
	 * @return
	 */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * When a user authenticates/does success login, we need to create a JWT for auth. user
     * which the user is expected to send in each & every subsequent https request  
     * 
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * creates & returns the JWT for a 'successfully authenticated user'
     * 
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder() // JWT Builder used
        		.setClaims(claims) // if any extra permissions/authorities/grants need to be set
        		.setSubject(subject) // the user who has been authenticated
        		.setIssuedAt(new Date(System.currentTimeMillis())) // the issue date of JWT
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // expiration date of JWT(10hrs)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // algorithm used to sign the JWT using a Super Secret Key
                .compact(); // builds the JWT and serializes it to a compact, URL-safe string
    }

    /**
     * validates the received JWT for a user-name
     * 
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}