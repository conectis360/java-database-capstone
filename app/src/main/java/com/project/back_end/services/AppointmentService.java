package com.project.back_end.services;

import com.project.back_end.exception.ResourceNotFoundException;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// 1. Add @Service Annotation
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    // Note: TokenService would be used in the Controller layer to get user details/roles.

    // 2. Constructor Injection for Dependencies
    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * 4. Book Appointment Method
     * Saves a new appointment. Throws exceptions for invalid patient/doctor IDs.
     * @param appointment The appointment object to save.
     * @return The saved Appointment entity.
     */
    // 3. Add @Transactional for methods that modify the database
    @Transactional
    public Appointment bookAppointment(Appointment appointment) {
        // Ensure patient and doctor exist before booking
        patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointment.getPatient().getId()));
        doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointment.getDoctor().getId()));

        return appointmentRepository.save(appointment);
    }

    /**
     * An alternative method implementation as per the prompt's return type suggestion.
     * While throwing exceptions is preferred, this version returns an integer code.
     * @param appointment The appointment object to save.
     * @return 1 for success, 0 for failure.
     */
    @Transactional
    public int bookAppointmentWithReturnCode(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // Success
        } catch (Exception e) {
            // Log the exception e.getMessage()
            return 0; // Failure
        }
    }


    /**
     * 5. Update Appointment Method
     * Updates an existing appointment after performing necessary validations.
     * @param appointmentId The ID of the appointment to update.
     * @param patientId The ID of the patient requesting the update (for validation).
     * @param newAppointmentDetails The object containing the new appointment details.
     * @return The updated Appointment entity.
     */
    @Transactional
    public Appointment updateAppointment(Integer appointmentId, Integer patientId, Appointment newAppointmentDetails) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        // Validate that the patient owns this appointment
        if (!existingAppointment.getPatient().getId().equals(patientId)) {
            throw new IllegalStateException("You are not authorized to update this appointment.");
        }

        // Validate that the appointment is in a state that can be updated (e.g., not "Completed")
        if (!existingAppointment.getStatus().equalsIgnoreCase("Scheduled")) {
            throw new IllegalStateException("Cannot update an appointment that is already " + existingAppointment.getStatus());
        }

        // Update details
        existingAppointment.setDate(newAppointmentDetails.getDate());
        existingAppointment.setTime(newAppointmentDetails.getTime());
        existingAppointment.setReason(newAppointmentDetails.getReason());

        return appointmentRepository.save(existingAppointment);
    }

    /**
     * 6. Cancel Appointment Method
     * Deletes an appointment after verifying the owner.
     * @param appointmentId The ID of the appointment to cancel.
     * @param patientId The ID of the patient requesting the cancellation.
     */
    @Transactional
    public void cancelAppointment(Integer appointmentId, Integer patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        // Ensure the patient who owns the appointment is trying to cancel it
        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new IllegalStateException("You are not authorized to cancel this appointment.");
        }

        appointmentRepository.delete(appointment);
    }

    /**
     * 7. Get Appointments Method
     * Retrieves appointments for a specific doctor on a given day.
     * @param doctorId The ID of the doctor.
     * @param date The date to fetch appointments for.
     * @return A list of appointments.
     */
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsForDoctorByDate(Integer doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
        return appointmentRepository.findByDoctorAndDate(doctor, date);
    }

    /**
     * 8. Change Status Method
     * A generic method to update the status of any appointment.
     * This was used in the PrescriptionController logic.
     * @param appointmentId The ID of the appointment to update.
     * @param newStatus The new status string (e.g., "Completed", "Cancelled").
     * @return The updated Appointment entity.
     */
    @Transactional
    public Appointment updateAppointmentStatus(Integer appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        appointment.setStatus(newStatus);
        return appointmentRepository.save(appointment);
    }
}