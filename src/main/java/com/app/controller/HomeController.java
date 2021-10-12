package com.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.LoginRequest;
import com.app.service.HomeServiceIntf;

@RestController
@RequestMapping("/home")
@CrossOrigin(value = "*", allowedHeaders = "*")
public class HomeController {

	// dependency : homeService
	@Autowired
	private HomeServiceIntf homeService;

	@PostMapping("/userLogin")
	public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest request) {
		return ResponseEntity.ok(homeService.authenticateUser(request.getEmail(), request.getPassword()));
	}

}
