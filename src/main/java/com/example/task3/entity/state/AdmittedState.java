package com.example.task3.entity.state;

import com.example.task3.entity.Patient;
import com.example.task3.entity.Ward;
import com.example.task3.exception.IllegalStateTransitionException;

public class AdmittedState implements PatientState {
    @Override
    public void admit(Patient patient, Ward ward) {
        throw new IllegalStateTransitionException();
    }

    @Override
    public void startTreatment(Patient patient) {
        patient.setState(new UndergoingTreatmentState());
    }

    @Override
    public void completeTreatment(Patient patient) {
        throw new IllegalStateTransitionException("Patient has not started treatment");
    }

    @Override
    public void discharge(Patient patient) {
        Ward ward = patient.getWard()
                .orElseThrow(() -> new IllegalStateTransitionException("Patient has no ward associated"));

        ward.dischargePatient();
        patient.setWard(null);
        patient.setState(new DischargedState());
    }

    @Override
    public String getStateName() {
        return "Admitted";
    }
}
