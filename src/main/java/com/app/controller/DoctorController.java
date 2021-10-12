package com.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.DoctorDTO;
import com.app.dto.LoginRequest;
import com.app.entity.modal.DoctorTimeTable;
import com.app.service.DoctorServiceIntf;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(value = "*", allowedHeaders = "*")
public class DoctorController {

	// dependency : doctorService
	@Autowired
	DoctorServiceIntf doctorService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateDoctor(@RequestBody @Valid LoginRequest request) {
		return new ResponseEntity<>(doctorService.authenticateDoctor(request.getEmail(), request.getPassword()),
				HttpStatus.ACCEPTED);
	}

	@PostMapping("/createAppointmentSlot/{doctorId}")
	public List<LocalDateTime> createAppointmentSlots(@PathVariable Long doctorId,
			@RequestBody DoctorTimeTable doctorTimeTable) {
		return doctorService.createAvailableSlotsDetails(doctorId, doctorTimeTable);
	}

	@PostMapping("/getDoctorDetails/{doctorId}")
	public ResponseEntity<?> getDoctorDetails(@PathVariable Long doctorId) {
		return ResponseEntity.ok(doctorService.getDoctorDetails(doctorId));
	}

	@PutMapping("/updateDoctor/{doctorId}")
	public ResponseEntity<?> updateDoctorDetails(@RequestBody DoctorDTO detachedDoctor, @PathVariable Long doctorId) {
		return ResponseEntity.ok(doctorService.updateDoctorDetails(detachedDoctor, doctorId));
	}
}