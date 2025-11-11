package com.asn.trac.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.asn.trac.api.service.UserService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain filterChain(
			HttpSecurity http,
			JwtUtil jwtUtil,
			UserService userService) throws Exception {

		http.csrf().disable()
			.cors().configurationSource(corsConfigurationSource)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/error").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers(HttpMethod.GET, "/subject/**", "/student/**").hasAnyRole("ADMIN","FACULTY","STUDENT")
				.antMatchers("/subject/**", "/student/**").hasAnyRole("ADMIN","FACULTY")
				.antMatchers("/attendance/**").hasAnyRole("ADMIN","FACULTY","STUDENT")
				.anyRequest().authenticated();

		http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}