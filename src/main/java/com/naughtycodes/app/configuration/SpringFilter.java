package com.naughtycodes.app.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class SpringFilter extends GenericFilterBean {

	public SpringFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getRequestURL().toString();
		
		if(url.split("/")[url.split("/").length-1].equalsIgnoreCase("auth")){
			System.out.println("Url_:"+url);
			System.out.println(request.getMethod());
			
			request.setAttribute("userName", "admin");
			request.setAttribute("password", "password");
			
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            
			UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken("admin", "password", grantedAuths);
			SecurityContextHolder.getContext().setAuthentication(requestAuthentication);
			
			System.out.println(requestAuthentication.toString());
			 
		}
		
		chain.doFilter(req, res);
	}

}
