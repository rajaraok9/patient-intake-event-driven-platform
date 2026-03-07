package org.modmed.patientintakeservice.repository;

import org.modmed.patientintakeservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Additional query methods if needed
}
