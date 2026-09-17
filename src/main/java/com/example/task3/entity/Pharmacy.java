package com.example.task3.entity;

import java.util.Objects;

public class Pharmacy {
    private final int id;
    private int medicationAmount;

    public Pharmacy(int id, int medicationAmount) {
        this.id = id;
        this.medicationAmount = medicationAmount;
    }

    public void restockMedication(int medicationAmount) {
        this.medicationAmount += medicationAmount;
    }

    public void fetchMedication() {
        this.medicationAmount--;
    }

    public boolean hasMedication() {
        return medicationAmount > 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, medicationAmount);
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

        Pharmacy other = (Pharmacy) object;

        return this.id == other.id && this.medicationAmount == other.medicationAmount;
    }

    @Override
    public String toString() {
        return "Pharmacy{%d:%d}".formatted(id, medicationAmount);
    }
}
