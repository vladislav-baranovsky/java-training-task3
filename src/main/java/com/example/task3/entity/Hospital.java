package com.example.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Hospital {
    private static final Logger logger = LogManager.getLogger();

    private static final CountDownLatch latch = new CountDownLatch(1);
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static Hospital instance;

    private final List<Ward> wards;
    private final Pharmacy pharmacy;

    private Hospital() {
        wards = List.of(
                new Ward(1),
                new Ward(2),
                new Ward(3)
        );

        pharmacy = new Pharmacy(1, 0);
    }

    public static Hospital getInstance() {
        if (latch.getCount() == 0) {
            return instance;
        }

        if (initialized.compareAndSet(false, true)) {
            instance = new Hospital();
            latch.countDown();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted", e);
        }

        return instance;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }
}
