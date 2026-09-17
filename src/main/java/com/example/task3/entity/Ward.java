package com.example.task3.entity;

import java.util.Objects;

public class Ward {
    private final int id;
    private boolean isOccupied = false;
    private boolean isReserved = false;

    public Ward(int id) {
        this.id = id;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void reserve() {
        this.isReserved = true;
    }

    public void unreserve() {
        this.isReserved = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void admitPatient() {
        isOccupied = true;
    }

    public void dischargePatient() {
        isOccupied = false;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isOccupied, isReserved);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null) {
            return false;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        Ward other = (Ward) object;

        return this.id == other.id && this.isOccupied == other.isOccupied && this.isReserved == other.isReserved;
    }

    @Override
    public String toString() {
        return "Ward{%d}".formatted(id);
    }
}
