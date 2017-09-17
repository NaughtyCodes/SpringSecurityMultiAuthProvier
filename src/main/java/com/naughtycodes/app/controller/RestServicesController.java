package com.naughtycodes.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.naughtycodes.app.model.User;
import com.naughtycodes.app.service.UserService;

@RestController
public class RestServicesController {

	 @Autowired
	 UserService userService;
	 
	@RequestMapping(value = { "/auth" }, method = RequestMethod.GET)
	public String authService(HttpServletRequest req) {
		
//		String username = req.getAttribute("username").toString();
//		String password = req.getAttribute("password").toString();
//		
//		System.out.println(username+"__"+password);
//		
//		UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
		
		return req.getRequestURI();
	}
	
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String loginPage() {
		return "welcome";
	}  
    
	@RequestMapping(value = "/user/", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<com.naughtycodes.app.model.User>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
}
