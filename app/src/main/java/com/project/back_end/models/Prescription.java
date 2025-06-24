package com.project.back_end.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a Prescription in the Smart Clinic Management System.
 * @Document annotation:
 * - Marks the class as a MongoDB document (a collection in MongoDB).
 * - The collection name is specified as "prescriptions" to map this class to the "prescriptions" collection in MongoDB.
 */
@Document(collection = "prescriptions")
public class Prescription {

    /**
     * Represents the unique identifier for each prescription.
     * - The @Id annotation marks it as the primary key in the MongoDB collection.
     * - The id is of type String, which is commonly used for MongoDB's ObjectId.
     */
    @Id
    private String id;

    /**
     * Represents the name of the patient receiving the prescription.
     * - The @NotNull annotation ensures that the patient name is required.
     * - The @Size(min = 3, max = 100) annotation ensures that the name length is between 3 and 100 characters.
     */
    @NotNull(message = "Patient name is required.")
    @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters.")
    private String patientName;

    /**
     * Represents the ID of the associated appointment where the prescription was given.
     * - The @NotNull annotation ensures that the appointment ID is required for the prescription.
     */
    @NotNull(message = "Appointment ID is required.")
    private Long appointmentId;

    /**
     * Represents the medication prescribed to the patient.
     * - The @NotNull annotation ensures that the medication name is required.
     * - The @Size(min = 3, max = 100) annotation ensures that the medication name is between 3 and 100 characters.
     */
    @NotNull(message = "Medication name is required.")
    @Size(min = 3, max = 100, message = "Medication name must be between 3 and 100 characters.")
    private String medication;

    /**
     * Represents the dosage information for the prescribed medication.
     * - The @NotNull annotation ensures that the dosage information is provided.
     */
    @NotNull(message = "Dosage information is required.")
    private String dosage;

    /**
     * Represents any additional notes or instructions from the doctor regarding the prescription.
     * - The @Size(max = 200) annotation ensures that the doctor's notes do not exceed 200 characters.
     */
    @Size(max = 200, message = "Doctor's notes cannot exceed 200 characters.")
    private String doctorNotes;

    /**
     * The class includes a no-argument constructor (default constructor) and a parameterized constructor.
     */
    public Prescription() {
    }

    public Prescription(String patientName, Long appointmentId, String medication, String dosage, String doctorNotes) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
    }

    /**
     * Standard getter and setter methods are provided for all fields.
     * These methods allow access and modification of the fields of the Prescription class.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}
