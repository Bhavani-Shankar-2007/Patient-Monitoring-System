// File: src/main/java/com/sns/hackathon/PatientService.java
package com.sns.hackathon;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PatientService {

    private final Map<Long, Patient> patients = new HashMap<>();
    private final GeminiService geminiService;

    public PatientService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostConstruct
    public void init() {
        patients.put(1L, new Patient(1L, "John Doe", 45, "Type 2 Diabetes", 82, 4, 5, 75));
        patients.put(2L, new Patient(2L, "Jane Smith", 58, "Hypertension", 65, 2, 4, 60));
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public Patient getPatient(Long id) {
        return patients.get(id);
    }

    public void updateAdherence(Long id, Map<String, Object> data) {
        Patient p = patients.get(id);
        if (p != null) {
            if (data.containsKey("medicationAdherence")) 
                p.setMedicationAdherence(Double.parseDouble(data.get("medicationAdherence").toString()));
            if (data.containsKey("appointmentsAttended")) 
                p.setAppointmentsAttended(Integer.parseInt(data.get("appointmentsAttended").toString()));
            if (data.containsKey("totalAppointments")) 
                p.setTotalAppointments(Integer.parseInt(data.get("totalAppointments").toString()));
            if (data.containsKey("lifestyleScore")) 
                p.setLifestyleScore(Double.parseDouble(data.get("lifestyleScore").toString()));
            if (data.containsKey("logEntry")) 
                p.addLog((String) data.get("logEntry"));
        }
    }

    public Intervention generateIntervention(Long id) {
        Patient p = patients.get(id);
        if (p == null) return null;

        String context = String.format(
            "Name: %s, Age: %d, Condition: %s, Medication Adherence: %.1f%%, " +
            "Appointments: %d/%d, Lifestyle Score: %.1f%%",
            p.getName(), p.getAge(), p.getCondition(), p.getMedicationAdherence(),
            p.getAppointmentsAttended(), p.getTotalAppointments(), p.getLifestyleScore()
        );

        return geminiService.generateInterventionForPatient(context);
    }
}