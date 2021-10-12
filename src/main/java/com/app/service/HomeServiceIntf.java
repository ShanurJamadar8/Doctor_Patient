package com.app.service;

import com.app.dto.LoginResponse;

public interface HomeServiceIntf {
	
	//authenticate patient
	LoginResponse authenticateUser(String email, String password);
		
	
}
