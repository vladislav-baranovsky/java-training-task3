package com.example.task3;

import com.example.task3.entity.Hospital;
import com.example.task3.entity.Patient;
import com.example.task3.service.HospitalService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    void main() {
        HospitalService hospitalService = new HospitalService(Hospital.getInstance());

        try (ExecutorService executorService = Executors.newFixedThreadPool(2)) {
            for (int i = 0; i < 10; i++) {
                Patient patient = new Patient(i, "Patient %d".formatted(i));
                executorService.submit(() -> hospitalService.processPatient(patient));
            }

            TimeUnit.SECONDS.sleep(2);

            executorService.submit(() -> hospitalService.restockMedication(3));

            TimeUnit.SECONDS.sleep(2);

            executorService.submit(() -> hospitalService.restockMedication(4));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
