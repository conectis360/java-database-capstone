## **MySQL Database Design**

This design outlines the relational tables for managing core entities and their interactions within the Smart Clinic system.

### **Table: `patients`**
Stores essential patient information and credentials for portal access.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `full_name`: `VARCHAR(255)`, `NOT NULL`
- `email`: `VARCHAR(255)`, `NOT NULL`, `UNIQUE`
- `phone_number`: `VARCHAR(20)`
- `password_hash`: `VARCHAR(255)`, `NOT NULL`
- `date_of_birth`: `DATE`
- `created_at`: `TIMESTAMP`, `DEFAULT CURRENT_TIMESTAMP`

### **Table: `doctors`**
Stores doctor profiles, credentials, and their primary location.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `full_name`: `VARCHAR(255)`, `NOT NULL`
- `specialization`: `VARCHAR(150)`
- `email`: `VARCHAR(255)`, `NOT NULL`, `UNIQUE`
- `password_hash`: `VARCHAR(255)`, `NOT NULL`
- `primary_clinic_id`: `INT`, Foreign Key → `clinic_locations(id)`
- `created_at`: `TIMESTAMP`, `DEFAULT CURRENT_TIMESTAMP`

### **Table: `admin`**
Stores credentials for platform administrators.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `username`: `VARCHAR(100)`, `NOT NULL`, `UNIQUE`
- `password_hash`: `VARCHAR(255)`, `NOT NULL`
- `last_login`: `TIMESTAMP`

### **Table: `clinic_locations`**
Stores information about the physical clinic branches.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `name`: `VARCHAR(255)`, `NOT NULL`
- `address`: `VARCHAR(255)`, `NOT NULL`
- `phone_number`: `VARCHAR(20)`

### **Table: `appointments`**
Acts as the central table linking patients, doctors, and locations for each visit.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `patient_id`: `INT`, Foreign Key → `patients(id)`, `NOT NULL`, `ON DELETE CASCADE`
    * *Justification for `ON DELETE CASCADE`: If a patient's record is deleted, their associated appointments become irrelevant and should be cleaned up automatically to maintain data integrity.*
- `doctor_id`: `INT`, Foreign Key → `doctors(id)`, `NOT NULL`
- `location_id`: `INT`, Foreign Key → `clinic_locations(id)`, `NOT NULL`
- `appointment_time`: `DATETIME`, `NOT NULL`
- `status`: `ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')`, `NOT NULL`, `DEFAULT 'SCHEDULED'`
- `reason_for_visit`: `TEXT`

### **Table: `doctor_availability`**
Defines specific time slots when a doctor is available for bookings. This prevents overlapping appointments at the database level.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `doctor_id`: `INT`, Foreign Key → `doctors(id)`, `NOT NULL`
- `start_time`: `DATETIME`, `NOT NULL`
- `end_time`: `DATETIME`, `NOT NULL`
- `is_booked`: `BOOLEAN`, `NOT NULL`, `DEFAULT FALSE`
- `UNIQUE` (`doctor_id`, `start_time`)
    * *Justification for `UNIQUE`: This constraint ensures a doctor cannot have two availability slots starting at the exact same time.*

### **Table: `payments`**
Tracks payment information for each completed appointment.
- `id`: `INT`, Primary Key, `AUTO_INCREMENT`
- `appointment_id`: `INT`, Foreign Key → `appointments(id)`, `NOT NULL`, `UNIQUE`
    * *Justification for `UNIQUE`: Ensures that each appointment can only have one payment record associated with it.*
- `amount`: `DECIMAL(10, 2)`, `NOT NULL`
- `payment_method`: `VARCHAR(50)`
- `transaction_status`: `ENUM('PENDING', 'PAID', 'FAILED')`, `NOT NULL`, `DEFAULT 'PENDING'`
- `payment_date`: `TIMESTAMP`, `DEFAULT CURRENT_TIMESTAMP`

***

## **MongoDB Collection Design**

This section outlines a flexible document-based collection to store complex, semi-structured data that complements the MySQL schema.

### **Collection: `medical_records`**
This collection stores a comprehensive record for each patient visit, including free-form notes, structured prescriptions, and optional feedback. Using just the patient and appointment IDs prevents data duplication and keeps the documents lean.

```json
{
  "_id": "ObjectId('64cde567890f12a3b4c5d6e7')",
  "patient_id": 101,
  "appointment_id": 512,
  "record_last_updated": "2025-06-17T12:30:00Z",
  "doctor_notes": [
    {
      "author_doctor_id": 12,
      "note": "Patient complains of seasonal allergies. Symptoms include sneezing and itchy eyes. No fever reported.",
      "timestamp": "2025-06-17T11:15:00Z"
    },
    {
      "author_doctor_id": 12,
      "note": "Prescribed Loratadine. Advised patient to follow up in 2 weeks if symptoms do not improve.",
      "timestamp": "2025-06-17T11:20:00Z"
    }
  ],
  "prescriptions": [
    {
      "medication_name": "Loratadine",
      "dosage": "10mg",
      "frequency": "1 tablet per day",
      "duration": "14 days",
      "refills_available": 1
    }
  ],
  "attachments": [
    {
      "file_name": "blood_test_results.pdf",
      "s3_storage_url": "s3://smartclinic-bucket/patient101/blood_test_results.pdf",
      "upload_date": "2025-06-16T09:00:00Z"
    }
  ],
  "patient_feedback": {
    "rating": 5,
    "comment": "Dr. Eve was very attentive and helpful.",
    "submitted_at": "2025-06-17T12:00:00Z"
  }
}
```