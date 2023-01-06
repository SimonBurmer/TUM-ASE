package edu.tum.ase.backendCommon.model;

public enum DeliveryStatus {

    ORDERED,
    PICKED_UP,
    IN_TARGET_BOX,
    DELIVERED;

    public boolean canBeReassigned() {
        return this == ORDERED;
    }

    public boolean canBeRemoved() {
        return this != PICKED_UP && this != IN_TARGET_BOX;
    }

    public boolean canBeAssignedByBox() {
        return this == IN_TARGET_BOX || this == DELIVERED;
    }
}
