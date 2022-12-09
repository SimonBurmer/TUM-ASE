package edu.tum.ase.backendCommon.roles;

public class UserRole {
    public static final String CUSTOMER = "CUSTOMER";
    public static final String DISPATCHER = "DISPATCHER";
    public static final String DELIVERER = "DELIVERER";

    public String getCompleteRole(String role) {
        return "ROLE_" + role;
    }

    private UserRole() {}
}