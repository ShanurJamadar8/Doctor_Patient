package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.modal.Admin;
import com.app.repository.AdminRepository;

@Service
@Transactional
public class AdminServiceImpl implements AdminServiceIntf {

	@Autowired
	private AdminRepository adminRepo;
	
	@Override
	public List<Admin> getListOfAdmin() {
		return adminRepo.findAll();
	}

}
