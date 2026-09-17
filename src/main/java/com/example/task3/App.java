package com.example.task3;

import com.example.task3.entity.Patient;
import com.example.task3.service.HospitalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    private final static Logger logger = LogManager.getLogger();

    void main() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        HospitalService hospitalService = new HospitalService();

        List<Patient> patients = new ArrayList<>();
        try (ScheduledExecutorService pharmacyScheduler = Executors.newSingleThreadScheduledExecutor()) {
            pharmacyScheduler.scheduleAtFixedRate(
                    () -> hospitalService.restockMedicine(3),
                    0,
                    2,
                    TimeUnit.SECONDS
            );

            try (ExecutorService executorService = Executors.newFixedThreadPool(corePoolSize)) {
                for (int i = 0; i < 10; i++) {
                    Patient patient = new Patient(i, "Patient %d".formatted(i));
                    patients.add(patient);
                    executorService.submit(() -> hospitalService.treatPatient(patient));
                }
            }

            pharmacyScheduler.shutdown();
        }

        for (var patient : patients) {
            logger.info("Patient {}, status: {}", patient, patient.getState().getStateName());
        }
    }
}
