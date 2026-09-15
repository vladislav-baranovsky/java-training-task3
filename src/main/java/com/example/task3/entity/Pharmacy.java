package com.example.task3.entity;

public class Pharmacy {
    private int currentMedicationAmount;

    public Pharmacy(int initialMedicationAmount) {
        currentMedicationAmount = initialMedicationAmount;
    }

    public boolean hasMedication() {
        return currentMedicationAmount > 0;
    }

    public void fetchMedication() {
        currentMedicationAmount--;
    }

    public void restockMedication(int amount) {
        currentMedicationAmount += amount;
    }
}
