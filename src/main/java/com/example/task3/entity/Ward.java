package com.example.task3.entity;

import java.util.Optional;

public class Ward {
    private final int id;
    private Patient currentPatient = null;

    public Ward(int id) {
        this.id = id;
    }

    public boolean admit(Patient patient) {
        if (isOccupied()) {
            return false;
        }

        currentPatient = patient;

        return true;
    }

    public boolean discharge() {
        if (isOccupied()) {
            currentPatient = null;
            return true;
        }

        return false;
    }

    public boolean isOccupied() {
        return currentPatient != null;
    }

    public Optional<Patient> getPatient() {
        if (isOccupied()) {
            return Optional.of(currentPatient);
        }

        return Optional.empty();
    }

    public int getId() {
        return id;
    }
}
