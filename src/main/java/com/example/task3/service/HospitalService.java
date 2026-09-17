package com.example.task3.service;

import com.example.task3.entity.Hospital;
import com.example.task3.entity.Patient;
import com.example.task3.entity.Pharmacy;
import com.example.task3.entity.Ward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class HospitalService {
    private static final Logger logger = LogManager.getLogger();

    private final ReentrantLock hospitalLock = new ReentrantLock(true);
    private final Condition wardAvailableCondition = hospitalLock.newCondition();

    private final ReentrantLock pharmacyLock = new ReentrantLock(true);
    private final Condition medicationAvailableCondition = pharmacyLock.newCondition();

    public HospitalService() {
    }

    public void treatPatient(Patient patient) {
        Hospital hospital = Hospital.getInstance();
        Ward assignedWard = null;

        hospitalLock.lock();
        try {
            while (assignedWard == null) {
                for (Ward ward : hospital.getWards()) {
                    if (!ward.isReserved()) {
                        ward.reserve();
                        assignedWard = ward;
                        logger.debug("Ward {} has been reserved", ward);
                        break;
                    }
                }

                if (assignedWard == null) {
                    wardAvailableCondition.await();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted", e);
            return;
        } finally {
            hospitalLock.unlock();
        }

        patient.admitToWard(assignedWard);
        logger.debug("Patient {} admitted", patient);
        try {
            TimeUnit.MILLISECONDS.sleep(200);

            pharmacyLock.lock();
            try {
                Pharmacy pharmacy = hospital.getPharmacy();
                while (!pharmacy.hasMedication()) {
                    logger.debug("Waiting for medication");
                    medicationAvailableCondition.await();
                }
                pharmacy.fetchMedication();
                TimeUnit.MILLISECONDS.sleep(200);
            } finally {
                pharmacyLock.unlock();
            }

            logger.debug("Starting treatment of patient {}", patient);
            patient.startTreatment();
            TimeUnit.MILLISECONDS.sleep(200);

            logger.debug("Completing treatment of patient {}", patient);
            patient.completeTreatment();
            TimeUnit.MILLISECONDS.sleep(200);

            logger.debug("Discharging patient {}", patient);
            patient.discharge();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted", e);
            return;
        } finally {
            hospitalLock.lock();
            try {
                assignedWard.unreserve();
                wardAvailableCondition.signalAll();
            } finally {
                hospitalLock.unlock();
            }
        }

        hospitalLock.lock();
        try {
            wardAvailableCondition.signalAll();
        } finally {
            hospitalLock.unlock();
        }
    }

    public void restockMedicine(int medicationAmount) {
        Pharmacy pharmacy = Hospital.getInstance()
                .getPharmacy();

        pharmacyLock.lock();
        try {
            logger.debug("Restocking medication with {} units", medicationAmount);
            pharmacy.restockMedication(medicationAmount);
            medicationAvailableCondition.signalAll();
        } finally {
            pharmacyLock.unlock();
        }
    }
}
