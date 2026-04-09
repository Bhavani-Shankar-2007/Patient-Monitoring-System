package com.sns.hackathon;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private Long id;
    private String name;
    private int age;
    private String condition;
    private double medicationAdherence;
    private int appointmentsAttended;
    private int totalAppointments;
    private double lifestyleScore;
    private List<String> recentLogs = new ArrayList<>();

    public Patient() {}

    public Patient(Long id, String name, int age, String condition, double medAdh, int att, int total, double life) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.condition = condition;
        this.medicationAdherence = medAdh;
        this.appointmentsAttended = att;
        this.totalAppointments = total;
        this.lifestyleScore = life;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public double getMedicationAdherence() { return medicationAdherence; }
    public void setMedicationAdherence(double medicationAdherence) { this.medicationAdherence = medicationAdherence; }
    public int getAppointmentsAttended() { return appointmentsAttended; }
    public void setAppointmentsAttended(int appointmentsAttended) { this.appointmentsAttended = appointmentsAttended; }
    public int getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(int totalAppointments) { this.totalAppointments = totalAppointments; }
    public double getLifestyleScore() { return lifestyleScore; }
    public void setLifestyleScore(double lifestyleScore) { this.lifestyleScore = lifestyleScore; }
    public List<String> getRecentLogs() { return recentLogs; }
    public void addLog(String log) {
        recentLogs.add(log);
        if (recentLogs.size() > 5) recentLogs.remove(0);
    }
}