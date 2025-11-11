package com.asn.trac.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.asn.trac.api.service.UserService;
import com.asn.trac.api.entity.User;

import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserService userService;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				String username = jwtUtil.extractUsername(token);
				User user = userService.getUserByName(username);
				if (user != null && jwtUtil.isTokenValid(token, username)) {
					String role = jwtUtil.extractRole(token);
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							username,
							null,
							Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (Exception ex) {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}
}