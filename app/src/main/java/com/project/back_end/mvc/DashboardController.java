package com.project.back_end.mvc;

import com.project.back_end.services.TokenValidationService;
import com.project.back_end.models.Role; // Assuming you have a Role enum: ADMIN, DOCTOR
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DashboardController {

    private final TokenValidationService tokenValidationService;

    @Autowired
    public DashboardController(TokenValidationService tokenValidationService) {
        this.tokenValidationService = tokenValidationService;
    }

    /**
     * Handles requests for the Admin Dashboard.
     * Validates the provided JWT token and checks if the user has the ADMIN role.
     *
     * @param token The JWT token from the URL path.
     * @return The name of the Thymeleaf template to render ("admin/adminDashboard") or a redirect to the login page.
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        // Validate the token and get the user's role
        Role userRole = tokenValidationService.getRoleFromToken(token);

        // Check if the token is valid and the role is ADMIN
        if (tokenValidationService.isTokenValid(token) && userRole == Role.ADMIN) {
            // If valid, serve the admin/adminDashboard.html template
            return "admin/adminDashboard";
        } else {
            // If invalid or wrong role, redirect to the login page
            return "redirect:/index.html"; // Or simply "redirect:/" if your login is at the root
        }
    }

    /**
     * Handles requests for the Doctor Dashboard.
     * Validates the provided JWT token and checks if the user has the DOCTOR role.
     *
     * @param token The JWT token from the URL path.
     * @return The name of the Thymeleaf template to render ("doctor/doctorDashboard") or a redirect to the login page.
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        // Validate the token and get the user's role
        Role userRole = tokenValidationService.getRoleFromToken(token);

        // Check if the token is valid and the role is DOCTOR
        if (tokenValidationService.isTokenValid(token) && userRole == Role.DOCTOR) {
            // If valid, serve the doctor/doctorDashboard.html template
            return "doctor/doctorDashboard";
        } else {
            // If invalid or wrong role, redirect to the login page
            return "redirect:/index.html";
        }
    }
}
