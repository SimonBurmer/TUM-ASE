package edu.tum.ase.backendCommon.model;

public enum DeliveryStatus {

    ORDERED,
    PICKED_UP,
    IN_TARGET_BOX,
    DELIVERED;

    public boolean canBeModified() {
        return this == ORDERED;
    }

    public boolean canBeRemoved() {
        return this != PICKED_UP && this != IN_TARGET_BOX;
    }
}
