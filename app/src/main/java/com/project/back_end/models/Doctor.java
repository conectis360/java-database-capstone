package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Represents a Doctor in the Smart Clinic Management System.
 * @Entity annotation:
 * - Marks the class as a JPA entity, meaning it represents a table in the database.
 * - Required for persistence frameworks (e.g., Hibernate) to map the class to a database table.
 */
@Entity
public class Doctor {

    /**
     * Represents the unique identifier for each doctor.
     * - The @Id annotation marks it as the primary key.
     * - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation auto-generates the ID value
     * when a new record is inserted into the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the doctor's name.
     * - The @NotNull annotation ensures that the doctor's name is required.
     * - The @Size(min = 3, max = 100) annotation ensures that the name length is between 3 and 100 characters,
     * providing validation for correct input and user experience.
     */
    @NotNull(message = "Doctor's name is required.")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
    private String name;

    /**
     * Represents the medical specialty of the doctor.
     * - The @NotNull annotation ensures that a specialty must be provided.
     * - The @Size(min = 3, max = 50) annotation ensures that the specialty name is between 3 and 50 characters long.
     */
    @NotNull(message = "Specialty must be provided.")
    @Size(min = 3, max = 50, message = "Specialty must be between 3 and 50 characters.")
    private String specialty;

    /**
     * Represents the doctor's email address.
     * - The @NotNull annotation ensures that an email address is required.
     * - The @Email annotation validates that the email address follows a valid email format (e.g., doctor@example.com).
     */
    @NotNull(message = "Email address is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    /**
     * Represents the doctor's password for login authentication.
     * - The @NotNull annotation ensures that a password must be provided.
     * - The @Size(min = 6) annotation ensures that the password must be at least 6 characters long.
     * - The @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) annotation ensures that the password
     * is not serialized in the response (hidden from the frontend).
     */
    @NotNull(message = "Password must be provided.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Represents the doctor's phone number.
     * - The @NotNull annotation ensures that a phone number must be provided.
     * - The @Pattern(regexp = "^[0-9]{10}$") annotation validates that the phone number must be exactly 10 digits long.
     */
    @NotNull(message = "Phone number must be provided.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phone;

    /**
     * Represents the available times for the doctor in a list of time slots.
     * - Each time slot is represented as a string (e.g., "09:00-10:00", "10:00-11:00").
     * - The @ElementCollection annotation ensures that the list of time slots is stored as a separate collection
     * in the database, linked to the Doctor entity.
     */
    @ElementCollection
    private List<String> availableTimes;

    /**
     * Standard getter and setter methods are provided for all fields: id, name, specialty,
     * email, password, phone, and availableTimes.
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}