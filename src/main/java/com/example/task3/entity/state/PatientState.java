package com.example.task3.entity.state;

import com.example.task3.entity.Patient;
import com.example.task3.entity.Ward;

public interface PatientState {
    void admit(Patient patient, Ward ward);
    void startTreatment(Patient patient);
    void completeTreatment(Patient patient);
    void discharge(Patient patient);
    String getStateName();
}
