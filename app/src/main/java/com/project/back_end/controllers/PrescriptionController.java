package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 1. Set Up the Controller Class:
 * - Annotate the class with @RestController to define it as a REST API controller.
 * - Use @RequestMapping("/api/prescriptions") to set the base path for all prescription-related endpoints.
 * - This controller manages creating and retrieving prescriptions tied to appointments.
 */
@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin("*")
public class PrescriptionController {

    /**
     * 2. Autowire Dependencies:
     * - Inject PrescriptionService to handle logic related to saving and fetching prescriptions.
     * - Inject TokenService for token validation and role-based access control.
     * - Inject AppointmentService to update appointment status after a prescription is issued.
     */
    private final PrescriptionService prescriptionService;
    private final TokenService tokenService;
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService, TokenService tokenService, AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.tokenService = tokenService;
        this.appointmentService = appointmentService;
    }

    /**
     * 3. Define the `savePrescription` Method:
     * - Handles HTTP POST requests to save a new prescription for a given appointment.
     * - Endpoint: POST /api/prescriptions
     * - Accepts a validated Prescription object in the request body.
     * - Validates the token from the Authorization header for the "doctor" role.
     * - If the token is valid, it saves the prescription and then updates the corresponding appointment's status.
     * @param prescription The Prescription object from the request body.
     * @param token The JWT for authentication (from Authorization header).
     * @return A response entity indicating success or failure.
     */
    @PostMapping
    public ResponseEntity<String> savePrescription(@Valid @RequestBody Prescription prescription, @RequestHeader("Authorization") String token) {
        // Validate token and role
        if (!tokenService.getRoleFromToken(token).equals("doctor")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Doctor role required.");
        }

        // Save the prescription
        Prescription savedPrescription = prescriptionService.savePrescription(prescription);

        // Update the appointment status
        // The appointment ID should be part of the Prescription object
        appointmentService.updateAppointmentStatus(savedPrescription.getAppointment().getId(), "Completed");

        return ResponseEntity.status(HttpStatus.CREATED).body("Prescription created successfully for appointment ID: " + savedPrescription.getAppointment().getId());
    }

    /**
     * 4. Define the `getPrescription` Method:
     * - Handles HTTP GET requests to retrieve a prescription by its associated appointment ID.
     * - Endpoint: GET /api/prescriptions/appointment/{appointmentId}
     * - Accepts the appointment ID from the path.
     * - Validates the token from the Authorization header. A doctor or the involved patient should be able to view it.
     * @param appointmentId The ID of the appointment to fetch the prescription for.
     * @param token The JWT for authentication (from Authorization header).
     * @return A response entity with the prescription details or an error message.
     */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionByAppointmentId(@PathVariable Integer appointmentId, @RequestHeader("Authorization") String token) {
        // Note: For a robust system, you would also check if the user (doctor/patient) from the token
        // is actually associated with this appointment. Here, we simplify to checking if the token is valid.
        if (!tokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        // Fetch the prescription
        Prescription prescription = prescriptionService.getPrescriptionByAppointmentId(appointmentId);

        return ResponseEntity.ok(prescription);
    }
}