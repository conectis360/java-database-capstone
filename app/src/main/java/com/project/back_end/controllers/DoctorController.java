package com.project.back_end.controllers;

import com.project.back_end.dto.LoginDTO;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1. Set Up the Controller Class:
 * - Annotate the class with @RestController to define it as a REST controller that serves JSON responses.
 * - Use @RequestMapping("/api/doctors") to prefix all endpoints with a common path.
 * - This class manages doctor-related functionalities such as registration, login, updates, and availability.
 * - @CrossOrigin("*") is added to allow requests from any origin, which is useful for development.
 */
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("*")
public class DoctorController {

    /**
     * 2. Autowire Dependencies:
     * - Inject DoctorService for handling the core logic related to doctors.
     * - Inject TokenService for handling JWT validation and authorization.
     * - Using constructor-based injection is a best practice.
     */
    private final DoctorService doctorService;
    private final TokenService tokenService;

    @Autowired
    public DoctorController(DoctorService doctorService, TokenService tokenService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    /**
     * 3. Define the `getDoctorAvailability` Method:
     * - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
     * - Endpoint: GET /api/doctors/availability/{doctorId}
     * - Requires doctorId as a path variable and date as a request parameter.
     * - Authorization token is passed in the header to ensure the request is authenticated.
     *
     * @param doctorId The ID of the doctor to check.
     * @param date     The date to check for availability (format: YYYY-MM-DD).
     * @param token    The JWT for authentication (from Authorization header).
     * @return A response entity with the doctor's availability.
     */
    @GetMapping("/availability/{doctorId}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable Integer doctorId,
            @RequestParam("date") LocalDate date,
            @RequestHeader("Authorization") String token) {

        // Note: For security, the token is validated from the header, not the URL.
        if (!tokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        boolean isAvailable = doctorService.getDoctorAvailability(doctorId, date);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("date", date);
        response.put("isAvailable", isAvailable);

        return ResponseEntity.ok(response);
    }

    /**
     * 4. Define the `getDoctors` Method:
     * - Handles HTTP GET requests to retrieve a list of all doctors.
     * - Endpoint: GET /api/doctors
     *
     * @return A response entity containing the list of all doctors.
     */
    @GetMapping
    public ResponseEntity<Map<String, List<Doctor>>> getDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        Map<String, List<Doctor>> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    /**
     * 5. Define the `saveDoctor` Method:
     * - Handles HTTP POST requests to register a new doctor.
     * - Endpoint: POST /api/doctors/register
     * - Requires an 'admin' role for authorization.
     *
     * @param newDoctor The Doctor object from the request body.
     * @param token     The JWT for authentication (from Authorization header).
     * @return A success or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> saveDoctor(@Valid @RequestBody Doctor newDoctor, @RequestHeader("Authorization") String token) {
        if (!tokenService.getRoleFromToken(token).equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Admin role required.");
        }

        doctorService.saveDoctor(newDoctor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Doctor registered successfully.");
    }

    /**
     * 6. Define the `doctorLogin` Method:
     * - Handles HTTP POST requests for doctor login.
     * - Endpoint: POST /api/doctors/login
     *
     * @param loginDTO A DTO containing the login credentials.
     * @return A response entity with the login status and a JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(doctorService.login(loginDTO));
    }

    /**
     * 7. Define the `updateDoctor` Method:
     * - Handles HTTP PUT requests to update an existing doctor's information.
     * - Endpoint: PUT /api/doctors/{id}
     * - Requires an 'admin' role for authorization.
     *
     * @param id            The ID of the doctor to update.
     * @param doctorDetails The updated Doctor object from the request body.
     * @param token         The JWT for authentication (from Authorization header).
     * @return A success or error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDoctor(@PathVariable Integer id, @Valid @RequestBody Doctor doctorDetails, @RequestHeader("Authorization") String token) {
        if (!tokenService.getRoleFromToken(token).equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Admin role required.");
        }

        Doctor updatedDoctor = doctorService.updateDoctor(id, doctorDetails);
        return ResponseEntity.ok("Doctor with ID " + updatedDoctor.getId() + " updated successfully.");
    }

    /**
     * 8. Define the `deleteDoctor` Method:
     * - Handles HTTP DELETE requests to remove a doctor by ID.
     * - Endpoint: DELETE /api/doctors/{id}
     * - Requires an 'admin' role for authorization.
     *
     * @param id    The ID of the doctor to delete.
     * @param token The JWT for authentication (from Authorization header).
     * @return A success or error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        if (!tokenService.getRoleFromToken(token).equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Admin role required.");
        }

        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor with ID " + id + " deleted successfully.");
    }

    /**
     * 9. Define the `filter` Method:
     * - Handles HTTP GET requests to filter doctors based on criteria.
     * - Endpoint: GET /api/doctors/filter
     * - Uses request parameters for filtering, which is more flexible than path variables.
     *
     * @param name      The name to filter by (optional).
     * @param specialty The specialty to filter by (optional).
     * @return A list of doctors matching the filter criteria.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Doctor>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty) {

        List<Doctor> filteredDoctors = doctorService.filterDoctors(name, specialty);
        return ResponseEntity.ok(filteredDoctors);
    }
}