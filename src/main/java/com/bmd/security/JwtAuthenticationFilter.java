package com.bmd.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//      try {
//      Thread.sleep(500);
//  } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//  }
		// Authorization

		final String requestHeader = request.getHeader("Authorization");
		// Bearer 2352345235sdfrsfgsdfsdf
//		logger.info(" Header :  {}", requestHeader);
		String username = null;
		String token = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			// looking good
			token = requestHeader.substring(7);
			try {

				username = this.jwtTokenHelper.getUsernameFromToken(token);

			} catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				logger.info("Given jwt token is expired !!");
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}

		} else {
//			logger.info("Invalid Header Value !! ");
		}

		//
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// fetch user detail from username
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			Boolean validateToken = this.jwtTokenHelper.validateToken(token, userDetails);
			if (validateToken) {

				// set the authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} else {
				logger.info("Validation fails !!");
				
			}

		}

		filterChain.doFilter(request, response);

	}

}

		


//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//
////		1. get token 
//
//		String requestToken = request.getHeader("Authorization");
//		Enumeration<String> headerNames = request.getHeaderNames();
//
//		while(headerNames.hasMoreElements())
//		{
//			System.out.println(headerNames.nextElement());
//		}
//		// Bearer 2352523sdgsg
//
//		System.out.println(requestToken);
//
//		String username = null;
//
//		String token = null;

//		if (requestToken != null && requestToken.startsWith("Bearer ")) {
//
//			token = requestToken.substring(7);
//
//			try {
//				username = this.jwtTokenHelper.getUsernameFromToken(token);
//			} catch (IllegalArgumentException e) {
//				System.out.println("Unable to get Jwt token");
//			} catch (ExpiredJwtException e) {
//				System.out.println("Jwt token has expired");
//			} catch (MalformedJwtException e) {
//				System.out.println("invalid jwt");
//
//			}
//
//		} else {
//			System.out.println("Jwt token does not begin with Bearer");
//		}
//
//		// once we get the token , now validate
//
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//			if (this.jwtTokenHelper.validateToken(token, userDetails)) {
//				// shi chal rha hai
//				// authentication karna hai
//
//				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//				usernamePasswordAuthenticationToken
//						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//			} else {
//				System.out.println("Invalid jwt token");
//			}
//
//		} else {
//			System.out.println("username is null or context is not null");
//		}
//
//		
//		filterChain.doFilter(request, response);
//	}


