package com.example.task3.config;

import com.example.task3.entity.Pharmacy;
import com.example.task3.entity.Ward;

import java.util.List;

public class HospitalConfigurator {
    private static final int INITIAL_MEDICATION_AMOUNT = 3;

    public record HospitalConfig(List<Ward> wards, Pharmacy pharmacy) {
    }

    private static HospitalConfig config;

    public static HospitalConfig getConfig() {
        if (config == null) {
            createConfig();
        }

        return config;
    }

    private static void createConfig() {
        List<Ward> wards = List.of();
        Pharmacy pharmacy = new Pharmacy(INITIAL_MEDICATION_AMOUNT);

        config = new HospitalConfig(wards, pharmacy);
    }
}
