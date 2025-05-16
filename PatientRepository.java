// PatientRepository.java
package com.example.doctorsuggestion.repository;

import com.example.doctorsuggestion.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
