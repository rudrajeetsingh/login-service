package com.monolithicservice.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monolithicservice.login.model.User;
import com.monolithicservice.login.service.LoginService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

	private LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * This method handles the login request.
	 * 
	 * @param user The user object containing username and password.
	 * @return ResponseEntity with the login status.
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		if (user != null && user.getUsername() != null && user.getPassword() != null) {
			String respnse = loginService.authenticate(user.getUsername(), user.getPassword());
			if (respnse != null) {
				return ResponseEntity.ok(respnse);
			} else {
				return ResponseEntity.status(401).body(respnse);
			}
		} else {
			return ResponseEntity.badRequest().body("Invalid request");
		}

	}
}
