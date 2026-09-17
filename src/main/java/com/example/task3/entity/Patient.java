package com.example.task3.entity;

import com.example.task3.entity.state.PatientState;
import com.example.task3.entity.state.RegisteredState;

import java.util.Objects;
import java.util.Optional;

public class Patient {
    private final int id;
    private final String name;
    private Ward currentWard;
    private PatientState currentState;

    public Patient(int id, String name) {
        this.id = id;
        this.name = name;
        this.currentWard = null;
        this.currentState = new RegisteredState();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setWard(Ward ward) {
        currentWard = ward;
    }

    public Optional<Ward> getWard() {
        return Optional.ofNullable(currentWard);
    }

    public void setState(PatientState state) {
        currentState = state;
    }

    public PatientState getState() {
        return currentState;
    }

    public void admitToWard(Ward ward) {
        currentState.admit(this, ward);
    }

    public void startTreatment() {
        currentState.startTreatment(this);
    }

    public void completeTreatment() {
        currentState.completeTreatment(this);
    }

    public void discharge() {
        currentState.discharge(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null) {
            return false;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        Patient other = (Patient) object;

        return this.id == other.id && Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "Patient{%d:%s}".formatted(id, name);
    }
}
