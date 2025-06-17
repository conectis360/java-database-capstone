## **Smart Clinic Database Design**

This document outlines the database schema for the Smart Clinic Management System, which uses a hybrid approach with MySQL for relational data and MongoDB for document-based data.

***

### **MySQL Database Design**

The MySQL database will store structured data related to users and appointments, ensuring data integrity and consistent relationships.

#### **1. `admins` Table**
Stores administrator login credentials.

```sql
CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### **2. `doctors` Table**
Stores doctor profiles and login credentials.

```sql
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    specialization VARCHAR(150),
    contact_info VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### **3. `patients` Table**
Stores patient profiles and login credentials.

```sql
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### **4. `appointments` Table**
Stores appointment information, linking patients and doctors.

```sql
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_datetime DATETIME NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);
```

***

### **MongoDB Collection Design**

The MongoDB database will store less structured, document-based data like medical records, which can vary greatly from one appointment to another.

#### **`medical_records` Collection**
This collection stores detailed notes and prescriptions from each patient consultation. A new document is created for each appointment that requires medical notes.

**Example JSON Document:**
Here is a sample document representing a single medical record from an appointment.

```json
{
  "_id": "66708b1a3e4f7a2d1c9b4e21",
  "appointment_id": 101,
  "patient_id": 45,
  "doctor_id": 7,
  "visit_date": "2025-06-18T10:00:00Z",
  "notes": "Patient reports persistent headaches and sensitivity to light. Blood pressure is slightly elevated at 135/85. Advised to monitor symptoms and reduce screen time.",
  "prescriptions": [
    {
      "medication": "Ibuprofen",
      "dosage": "400mg",
      "frequency": "As needed for pain, max 3 times a day",
      "duration_days": 10
    },
    {
      "medication": "Lisinopril",
      "dosage": "10mg",
      "frequency": "Once daily in the morning",
      "duration_days": 30
    }
  ]
}
```
* **Justification:** Storing medical records in MongoDB allows for flexibility. A doctor can add extensive notes, a list of prescriptions (or none at all), and other diagnostic data without being constrained by a rigid table structure. The use of `patient_id` and `doctor_id` provides a clear reference back to the relational data in MySQL.