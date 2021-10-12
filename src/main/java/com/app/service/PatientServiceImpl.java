package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.custome_exception.UserHandlingException;
import com.app.dto.PatientDTO;
import com.app.entity.modal.Patient;
import com.app.repository.AppointmentRepository;
import com.app.repository.PatientRepository;

@Service
@Transactional
public class PatientServiceImpl implements PatientServiceIntf {

	@Autowired
	private PatientRepository patientRepo;

	@Autowired
	private DoctorServiceImpl doctorService;

	@Autowired
	private AppointmentRepository appointmentRepo;

	@Override
	public Patient savePatient(PatientDTO patient) {
		System.out.println("in service" + patient);
		Patient newPatient = Patient.createPatient(patient);
		return patientRepo.save(newPatient);
	}

	@Override
	public String deletePatientById(Long patient_id) {
		List<Long> appoitments = appointmentRepo.getAppointmentIdListForPatient(patient_id);
		appoitments.forEach(System.out::println);
		Long appointmentId = null;
		for (int i = 0; i < appoitments.size(); i++) {
			appointmentId = appoitments.get(0);
			doctorService.makeSlotsAvailable(appointmentId);
		}

		patientRepo.deleteById(patient_id);
		return "Successfully Deleted Patient with id : " + patient_id;
	}

	@Override
	public List<Patient> getAllPatients() {
		return patientRepo.findAll();
	}

	@Override
	public Patient getPatientDetails(Long id) {
		return patientRepo.findById(id).orElseThrow(() -> new UserHandlingException("Invalid patient ID..."));
	}

	@Override
	public Patient updatePatientDetails(PatientDTO patient, long id) {
		Patient patient2 = Patient.createPatient(patient);
		patient2.setId(id);
		return patientRepo.save(patient2);
	}

	/*
	 * @Override public LoginResponse authenticatePatient(String email, String
	 * password) { Patient patient = patientRepo.findByEmailAndPassword(email,
	 * password) //populated or empty optional .orElseThrow(() -> new
	 * RuntimeException("Auth Failed")); return new LoginResponse(patient.getId(),
	 * patient.getFirstName()); }
	 */

}
