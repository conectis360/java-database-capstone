package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 3. @Repository annotation:
 * - The @Repository annotation marks this interface as a Spring Data repository bean.
 * - Spring automatically detects this interface and creates a proxy implementation for it.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // 1. Extend JpaRepository:
    // - The interface extends JpaRepository<Patient, Integer>, providing full CRUD functionality (Create, Read, Update, Delete) out of the box.
    // - The second generic parameter, Integer, should match the data type of the Patient entity's primary key (@Id).

    // 2. Custom Query Methods:
    // - Spring Data JPA automatically creates the implementation for these methods based on their names.

    /**
     * Finds a Patient entity by their unique email address.
     * Using Optional<Patient> is a best practice to safely handle cases where no patient is found.
     *
     * @param email The email address to search for.
     * @return An Optional containing the found patient, or an empty Optional if no patient is found.
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Finds a Patient entity by either their email address or their phone number.
     * This provides a flexible way to look up patients.
     *
     * @param email The email address to search for.
     * @param phone The phone number to search for.
     * @return An Optional containing the found patient, or an empty Optional if none match.
     */
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}