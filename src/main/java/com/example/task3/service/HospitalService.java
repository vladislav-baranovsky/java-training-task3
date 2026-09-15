package com.example.task3.service;

import com.example.task3.entity.Hospital;
import com.example.task3.entity.Patient;
import com.example.task3.entity.Pharmacy;
import com.example.task3.entity.Ward;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class HospitalService {
    private final Hospital hospital;

    private final Map<Integer, ReentrantLock> wardLocks = new ConcurrentHashMap<>();
    private final Map<Integer, Ward> occupiedWards = new ConcurrentHashMap<>();

    private final ReentrantLock admissionLock = new ReentrantLock();
    private final Condition wardAvailableCondition = admissionLock.newCondition();

    private final ReentrantLock pharmacyLock = new ReentrantLock();
    private final Condition medicationAvailableCondition = pharmacyLock.newCondition();

    public HospitalService(Hospital hospital) {
        this.hospital = hospital;
    }

    public void processPatient(Patient patient) {
        Ward assignedWard = null;

        admissionLock.lock();
        try {
            while (true) {
                for (Ward ward : hospital.getWards()) {
                    if (occupiedWards.putIfAbsent(ward.getId(), ward) == null) {
                        assignedWard = ward;
                        break;
                    }
                }

                if (assignedWard != null) {
                    break;
                }

                wardAvailableCondition.await();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return; //todo rethrow?
        } finally {
            admissionLock.unlock();
        }

        ReentrantLock assignedWardLock = wardLocks.computeIfAbsent(assignedWard.getId(), _ -> new ReentrantLock());
        assignedWardLock.lock();
        try {
            assignedWard.admit(patient);

            pharmacyLock.lock();
            try {
                Pharmacy pharmacy = hospital.getPharmacy();
                while (!pharmacy.hasMedication()) {
                    medicationAvailableCondition.await();
                }
                pharmacy.fetchMedication();
            } finally {
                pharmacyLock.unlock();
            }

            TimeUnit.SECONDS.sleep(2);

            assignedWard.discharge();
            occupiedWards.remove(assignedWard.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            assignedWard.discharge();
            occupiedWards.remove(assignedWard.getId());
        } finally {
            assignedWardLock.unlock();
        }

        admissionLock.lock();
        try {
            wardAvailableCondition.signalAll();
        } finally {
            admissionLock.unlock();
        }
    }

    public void restockMedication(int amount) {
        pharmacyLock.lock();
        try {
            Pharmacy pharmacy = hospital.getPharmacy();

            pharmacy.restockMedication(amount);
            medicationAvailableCondition.signalAll();
        } finally {
            pharmacyLock.unlock();
        }
    }
}
