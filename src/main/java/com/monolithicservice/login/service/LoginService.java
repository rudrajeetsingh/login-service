package com.monolithicservice.login.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

	/**
	 * This method authenticates the user based on username and password.
	 * 
	 * @param username The username of the user.
	 * @param password The password of the user.
	 * @return A string indicating the login status.
	 */
	public String authenticate(String username, String password) {
		// TODO Auto-generated method stub
		if (username.equals("admin") && password.equals("admin123")) {
			return "Login successful";
		} else {
			return "Invalid credentials";
		}
	}
}
