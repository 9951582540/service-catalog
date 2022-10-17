package com.wissen.servicecatalog.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.service.impl.CustomUserDetailsService;
import com.wissen.servicecatalog.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
	Marker confidentialMarker = MarkerFactory.getMarker("ACCESS");
	 
	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	CustomUserDetailsService userDetailsService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return new AntPathMatcher().match("/service-catalog/employee/signin/**", request.getServletPath());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info(confidentialMarker, "Requested URL:=> {}", request.getRequestURL());

		@SuppressWarnings("unused")
		final String authorizationHeader = request.getHeader("Authorization");

		String jwtToken = null;
		String username = null;
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

			jwtToken = bearerToken.substring(7, bearerToken.length());

			username = jwtUtil.extractUsername(jwtToken);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				try {
					if (Boolean.TRUE.equals(jwtUtil.validateToken(jwtToken, userDetails))) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} catch (EmployeeException e) {
					throw new ExpiredJwtException(null, null, "Invalid token!");
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}