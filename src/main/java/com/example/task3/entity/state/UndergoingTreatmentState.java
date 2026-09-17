package com.example.task3.entity.state;

import com.example.task3.entity.Patient;
import com.example.task3.entity.Ward;
import com.example.task3.exception.IllegalStateTransitionException;

public class UndergoingTreatmentState implements PatientState {
    @Override
    public void admit(Patient patient, Ward ward) {
        throw new IllegalStateTransitionException();
    }

    @Override
    public void startTreatment(Patient patient) {
        throw new IllegalStateTransitionException("Patient has already started treatment");
    }

    @Override
    public void completeTreatment(Patient patient) {
        patient.setState(new AdmittedState());
    }

    @Override
    public void discharge(Patient patient) {
        throw new IllegalStateTransitionException();

    }

    @Override
    public String getStateName() {
        return "Undergoing Treatment";
    }
}
