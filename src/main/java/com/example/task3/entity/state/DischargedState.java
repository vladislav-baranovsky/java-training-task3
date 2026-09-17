package com.example.task3.entity.state;

import com.example.task3.entity.Patient;
import com.example.task3.entity.Ward;
import com.example.task3.exception.IllegalStateTransitionException;

public class DischargedState implements PatientState {
    @Override
    public void admit(Patient patient, Ward ward) {
        if (ward.isOccupied()) {
            throw new IllegalStateTransitionException("Ward is occupied");
        }

        ward.admitPatient();
        patient.setWard(ward);
        patient.setState(new AdmittedState());
    }

    @Override
    public void startTreatment(Patient patient) {
        throw new IllegalStateTransitionException("Patient is not admitted");
    }

    @Override
    public void completeTreatment(Patient patient) {
        throw new IllegalStateTransitionException("Patient has not started treatment");
    }

    @Override
    public void discharge(Patient patient) {
        throw new IllegalStateTransitionException("Patient is not admitted");
    }

    @Override
    public String getStateName() {
        return "Discharged";
    }
}
