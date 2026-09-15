package com.example.task3.entity;

import com.example.task3.config.HospitalConfigurator;
import com.example.task3.config.HospitalConfigurator.HospitalConfig;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Hospital {
    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static Hospital instance;

    private final List<Ward> wards;
    private final Pharmacy pharmacy;

    private Hospital() {
        HospitalConfig config = HospitalConfigurator.getConfig();

        wards = config.wards();
        pharmacy = config.pharmacy();
    }

    public static Hospital getInstance() {
        if (LATCH.getCount() == 0) {
            return instance;
        }

        if (INITIALIZED.compareAndSet(false, true)) {
            instance = new Hospital();
            LATCH.countDown();
        }

        try {
            LATCH.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }

        return instance;
    }

    public List<Ward> getWards() {
        return Collections.unmodifiableList(wards);
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }
}
