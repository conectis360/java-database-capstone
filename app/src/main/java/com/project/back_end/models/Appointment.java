package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents an Appointment in the Smart Clinic Management System.
 * @Entity annotation:
 * - Marks the class as a JPA entity, meaning it represents a table in the database.
 * - Required for persistence frameworks (e.g., Hibernate) to map the class to a database table.
 */
@Entity
public class Appointment {

    /**
     * Represents the unique identifier for each appointment.
     * - The @Id annotation marks it as the primary key.
     * - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation auto-generates the ID value
     * when a new record is inserted into the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the doctor assigned to this appointment.
     * - The @ManyToOne annotation defines the relationship, indicating many appointments can be linked to one doctor.
     * - The @NotNull annotation ensures that an appointment must be associated with a doctor when created.
     */
    @NotNull(message = "Appointment must be assigned to a doctor.")
    @ManyToOne
    private Doctor doctor;

    /**
     * Represents the patient assigned to this appointment.
     * - The @ManyToOne annotation defines the relationship, indicating many appointments can be linked to one patient.
     * - The @NotNull annotation ensures that an appointment must be associated with a patient when created.
     */
    @NotNull(message = "Appointment must be assigned to a patient.")
    @ManyToOne
    private Patient patient;

    /**
     * Represents the date and time when the appointment is scheduled to occur.
     * - The @Future annotation ensures that the appointment time is always in the future when the appointment is created.
     * - It uses LocalDateTime, which includes both the date and time for the appointment.
     */
    @NotNull(message = "Appointment time cannot be null.")
    @Future(message = "Appointment time must be in the future.")
    private LocalDateTime appointmentTime;

    /**
     * Represents the current status of the appointment. It is an integer where:
     * - 0 means the appointment is scheduled.
     * - 1 means the appointment has been completed.
     * - The @NotNull annotation ensures that the status field is not null.
     */
    @NotNull(message = "Status field cannot be null.")
    private int status;

    // A no-argument constructor is required by JPA for entity creation.
    public Appointment() {
    }

    // A parameterized constructor can be added as needed to initialize fields.
    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    /**
     * Standard getter and setter methods are provided for accessing and modifying the fields.
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * This method is a transient field (not persisted in the database).
     * It calculates the end time of the appointment by adding one hour to the start time (appointmentTime).
     * It is used to get an estimated appointment end time for display purposes.
     * @return The calculated end time of the appointment. Returns null if appointmentTime is not set.
     */
    @Transient
    public LocalDateTime getEndTime() {
        if (this.appointmentTime != null) {
            return this.appointmentTime.plusHours(1);
        }
        return null;
    }

    /**
     * This method extracts only the date part from the appointmentTime field.
     * @return A LocalDate object representing just the date (without the time) of the scheduled appointment.
     * Returns null if appointmentTime is not set.
     */
    @Transient
    public LocalDate getAppointmentDate() {
        if (this.appointmentTime != null) {
            return this.appointmentTime.toLocalDate();
        }
        return null;
    }

    /**
     * This method extracts only the time part from the appointmentTime field.
     * @return A LocalTime object representing just the time (without the date) of the scheduled appointment.
     * Returns null if appointmentTime is not set.
     */
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        if (this.appointmentTime != null) {
            return this.appointmentTime.toLocalTime();
        }
        return null;
    }
}

