package com.naughtycodes.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService
{
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
		System.out.println("username recieved :: " + username);
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));	
        
		@SuppressWarnings("deprecation")
		UserDetails user = new User(username, 
									"password", 
									true, 
									true, 
									true, 
									true,
									grantedAuths);
		return user;
    }
}
