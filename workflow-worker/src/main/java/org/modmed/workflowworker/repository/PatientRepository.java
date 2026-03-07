package org.modmed.workflowworker.repository;

import org.modmed.workflowworker.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}