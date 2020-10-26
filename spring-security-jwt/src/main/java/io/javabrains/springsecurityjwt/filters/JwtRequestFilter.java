package io.javabrains.springsecurityjwt.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.javabrains.springsecurityjwt.service.MyUserDetailsService;
import io.javabrains.springsecurityjwt.util.JwtUtil;

/**
 * Intercepts the incoming authenticated requests for JWT's
 * 
 * @author Aniket Bharsakale
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final static String AUTHORIZATION = "Authorization";
	private final static String BEARER = "Bearer ";

	@Autowired
	private MyUserDetailsService userDetailsSerive;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader(AUTHORIZATION);
		String userName = null;
		String javaWebToken = null;

		if (null != authorizationHeader && authorizationHeader.startsWith(BEARER)) {
			javaWebToken = authorizationHeader.substring(7);
			userName = jwtUtil.extractUsername(javaWebToken);
		}

		if (null != userName && null == SecurityContextHolder.getContext().getAuthentication()) {
			UserDetails userDetails = this.userDetailsSerive.loadUserByUsername(userName);
			
			// this  validates the java web token against the retrieved userDetails
			// i.e the user-name matches and the JWT has not expired	
			if (jwtUtil.validateToken(javaWebToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePwdAuthToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePwdAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePwdAuthToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
