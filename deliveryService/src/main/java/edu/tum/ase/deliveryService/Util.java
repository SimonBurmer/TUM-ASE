package edu.tum.ase.deliveryService;

import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.security.core.Authentication;

public class Util {

    public static boolean isCustomer(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals(UserRole.getCompleteRole(UserRole.CUSTOMER)));
    }

    public static boolean isDeliverer(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals(UserRole.getCompleteRole(UserRole.DELIVERER)));
    }

    public static boolean isRasPi(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals(UserRole.getCompleteRole(UserRole.RASPI)));
    }
}
