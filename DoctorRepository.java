// DoctorRepository.java
package com.example.doctorsuggestion.repository;

import com.example.doctorsuggestion.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByCityAndSpeciality(String city, String speciality);
    List<Doctor> findByCity(String city);
}
