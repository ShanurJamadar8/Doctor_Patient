package com.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.custome_exception.UserHandlingException;
import com.app.entity.modal.Appointment;
import com.app.entity.modal.Doctor;
import com.app.entity.modal.DoctorTimeTable;
import com.app.entity.modal.Patient;
import com.app.repository.AppointmentRepository;
import com.app.repository.DoctorRepository;
import com.app.repository.DoctorTimeTableRepository;
import com.app.repository.PatientRepository;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentServiceIntf {

	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	DoctorTimeTableRepository doctorTimeTableRepo;
	
	@Autowired
	PatientRepository patientRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@Override
	public String cancelAppointment(Long appointmentId) {
		System.out.println("Hello aalo me");
		Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()->new UserHandlingException("appointment Id not found"));
		Doctor doctor = appointment.getDoctor();
		System.out.println("Doctor Id :: "+doctor.getId());
		LocalDateTime appointmentTime = appointment.getAppointmentTime();
		System.out.println("---------->>>"+doctor.getTimeSlot().getAvailableSlots());
		doctor.getTimeSlot().bookAvailableSlot(appointmentTime);
		appointmentRepo.deleteById(appointmentId);
		System.out.println("---------->>>"+doctor.getTimeSlot().getAvailableSlots());
		return "Appointment cancelled successfully(for "+appointmentId+")...!!!";
	}

	@Override
	public List<Appointment> getAllPatientCurrentAppoitments(Long patientId) {
		return appointmentRepo.getAllPatientCurrentAppoitments(patientId);
	}

	@Override
	public List<Appointment> getAllPatientAppoitmentsHistory(Long patientId) {
		return appointmentRepo.getAllPatientAppoitmentsHistory(patientId);
	}

	@Override
	public List<Appointment> getAllCurrentAppoitmentsForDoctor(Long doctorId) {
		return appointmentRepo.getAllCurrentAppoitmentsForDoctor(doctorId);
	}

	@Override
	public List<Appointment> getPatientAppoitmentsHistoryForDoctor(Long doctorId, Long patientId) {
		return appointmentRepo.getPatientAppoitmentsHistoryForDoctor(doctorId, patientId);
	}

	@Override
	public List<Appointment> getAllAppoitmentsHistoryForDoctor(Long doctorId) {
		return appointmentRepo.getAllAppoitmentsHistoryForDoctor(doctorId);
	}

//	@Override
//	public DoctorTimeTable generateTimeTableForDoctor(DoctorTimeTable timeTable, Long doctor_id) {
//		DoctorTimeTable dTimeTable = doctorTimeTableRepo.save(timeTable);
//		//dTimeTable.openSlots();
//		Doctor doctor = doctorRepo.findById(doctor_id).orElseThrow(() -> new UserHandlingException("Doctor not found"));
//		doctor.setTimeSlot(dTimeTable);
//		return dTimeTable;
//	}

	@Override
	public List<LocalDateTime> bookAppointmentForPatient(Long doctorId, Long patientId, String stime) {
		
		System.out.println("IN bookAppointmentForPatient....");

		//String[] split = stime.split("T");
		//LocalDateTime time = LocalDateTime.of(LocalDate.parse(split[0]), LocalTime.parse(split[1]));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime time = LocalDateTime.parse(stime, formatter);
		
		System.out.println("!@#$%%!@$!@$!@$@$!$ TIME : "+time);
		System.out.println("\n#################");
		Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new UserHandlingException("Doctor not found...!!!"));
		System.out.println("#######Doctor#########"+doctor);
		
		System.out.println("Doctor List : "+doctor.getTimeSlot().getAvailableSlots());
		
		Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new UserHandlingException("Patient not found...!!!"));
		System.out.println("#######Patient#########"+patient);

		DoctorTimeTable timeTable = doctor.getTimeSlot();
		System.out.println("#######time slot : ########"+timeTable);
		
		Appointment appointment = new Appointment(time, doctor, patient);
		appointmentRepo.save(appointment);

		List<LocalDateTime> availableSlotList = timeTable.bookAvailableSlot(time);
		
		System.out.println("*****Doctors appointment : "+doctor.getAppointement());
		System.out.println("*****Patients appointment : "+patient.getAppointement());
		
		System.out.println("^^^^^^^ AV SLOTS : "+availableSlotList);

		return availableSlotList;
	}

	@Override
	public Patient getPatientByAppointmentId(Long appointmentId) {
		
		Appointment appointment = appointmentRepo.findById(appointmentId).get();		
		Patient patient = appointment.getPatient();
		System.out.println("*****Patient from app id : "+patient);
		return patient;
	}

	@Override
	public List<LocalDateTime> getAllAppointmentSlots(Long doctorId) {
		
		Doctor doctor = doctorRepo.findById(doctorId).get();
		Map<LocalDateTime, Boolean> availableSlots = doctor.getTimeSlot().getAvailableSlots();
		List<LocalDateTime> list = new ArrayList<>();
		for(Map.Entry<LocalDateTime, Boolean> entry : availableSlots.entrySet()) {
			int currDate = LocalDate.now().getDayOfMonth();
			int currMonth = LocalDate.now().getMonthValue();
			int slotDate = entry.getKey().getDayOfMonth();
			int slotMonth = entry.getKey().getMonthValue();
			if(entry.getValue() == true && currDate == slotDate && currMonth == slotMonth) { //send only list whose boolean value is true (not booked slots)
				list.add(entry.getKey());
			}
		}
		Collections.sort(list);
		
		return list;
	}
}