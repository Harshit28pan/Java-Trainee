// SuggestionController.java
package com.example.doctorsuggestion.controller;

import com.example.doctorsuggestion.model.Doctor;
import com.example.doctorsuggestion.model.Patient;
import com.example.doctorsuggestion.repository.DoctorRepository;
import com.example.doctorsuggestion.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suggest")
public class SuggestionController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/doctors/patient/{patientId}")
    public ResponseEntity<?> suggestDoctors(@PathVariable Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        Patient patient = patientOptional.get();
        String patientCity = patient.getCity();
        String patientSymptom = patient.getSymptom();

        // Logic to map symptom to speciality (This would ideally be more robust)
        String speciality = getSpecialityBySymptom(patientSymptom);

        List<Doctor> suggestedDoctors = doctorRepository.findByCityAndSpeciality(patientCity, speciality);

        if (suggestedDoctors.isEmpty()) {
            List<Doctor> doctorsInCity = doctorRepository.findByCity(patientCity);
            if (doctorsInCity.isEmpty()) {
                return new ResponseEntity<>("We are still waiting to expand to your location.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There isn't any doctor present at your location for your symptom.", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(suggestedDoctors, HttpStatus.OK);
    }

    private String getSpecialityBySymptom(String symptom) {
        // Simple mapping - you might need a more sophisticated approach
        if (symptom.toLowerCase().contains("arthritis") || symptom.toLowerCase().contains("back pain") || symptom.toLowerCase().contains("tissue injuries")) {
            return "Orthopedic";
        } else if (symptom.toLowerCase().contains("skin infection") || symptom.toLowerCase().contains("burn")) {
            return "Dermatology";
        } else if (symptom.toLowerCase().contains("ear pain")) {
            return "ENT";
        } else if (symptom.toLowerCase().contains("gynecology")) {
            return "Gynecology";
        }
        return "General"; // Default speciality
    }
}
