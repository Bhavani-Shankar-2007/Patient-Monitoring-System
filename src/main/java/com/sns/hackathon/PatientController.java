// File: src/main/java/com/sns/hackathon/PatientController.java
package com.sns.hackathon;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return service.getAllPatients();
    }

    @PostMapping("/patients/{id}/log")
    public String logData(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        service.updateAdherence(id, data);
        return "Data logged successfully!";
    }

    @PostMapping("/patients/{id}/intervention")
    public Intervention generateIntervention(@PathVariable Long id) {
        return service.generateIntervention(id);
    }
}