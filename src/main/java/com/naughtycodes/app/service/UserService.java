package com.naughtycodes.app.service;

import java.util.List;

import com.naughtycodes.app.model.User;



public interface UserService {
	
	User findByName(String name);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(long id);

	List<User> findAllUsers(); 
	
	void deleteAllUsers();
	
	public boolean isUserExist(User user);
	
}
