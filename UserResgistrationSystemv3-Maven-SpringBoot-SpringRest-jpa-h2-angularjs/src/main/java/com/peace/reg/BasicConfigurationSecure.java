package com.peace.reg;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;


public class BasicConfigurationSecure extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		UserBuilder users = User.withDefaultPasswordEncoder();
		
		auth
			.inMemoryAuthentication()
			.withUser(users.username("user").password("userb").roles("USER"))
			.withUser(users.username("admin").password("admin").roles("ADMIN","USER"));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET,"/api/user/").hasRole("USER")
				.antMatchers(HttpMethod.POST,"/api/user/").hasRole("USER")
				.antMatchers(HttpMethod.PUT,"/api/user/**").hasRole("USER")
				.antMatchers(HttpMethod.DELETE,"/api/user/**").hasRole("ADMIN")
				.anyRequest()
				.authenticated()
			.and()
				.csrf()
				.disable();
			
	}
	
	

}
