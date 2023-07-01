package com.group15.goldenticket.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.services.UserService;
import com.group15.goldenticket.utils.JWTTokenFIlter;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTTokenFIlter filter;
	
	@Bean
	public CorsConfiguration corsConfiguration() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.addAllowedOrigin("http://localhost:5173");
	    configuration.addAllowedMethod("*");
	    configuration.addAllowedHeader("*");
	    return configuration;
	}
	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable());
		http.cors(Customizer.withDefaults());
	    
	    //Route filter
	    http.authorizeHttpRequests(auth -> 
	    	auth
	    		.requestMatchers("/auth/**","/event/all").permitAll()
	    		.anyRequest().authenticated()
	    );
	    
	    
	    
	    //Statelessness
	    http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    
	    //UnAunthorized handler
	    http.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex) -> {
	        res.sendError(
	        		HttpServletResponse.SC_UNAUTHORIZED,
	        		"Auth fail!"
	        	);
	    }));
	    
	    //JWT filter
	    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

			return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
	    AuthenticationManagerBuilder managerBuilder 
	    	= http.getSharedObject(AuthenticationManagerBuilder.class);
	    
	    managerBuilder
	    	.userDetailsService(identifier -> {
	    		User user = userService.findOneByIdentifier(identifier);
	    		
	    		if(user == null)
	    			throw new UsernameNotFoundException("User: " + identifier + ", not found!");
	    		
	    		return user;
	    	})
	    	.passwordEncoder(passwordEncoder);
	    
	    return managerBuilder.build();
	}
	
}
