package edu.tum.ase.deliveryService;

import org.springframework.security.core.Authentication;

public class Util {

    public static boolean isCustomer(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals("ROLE_CUSTOMER"));
    }

    public static boolean isDeliverer(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch((a) -> a.getAuthority().equals("ROLE_DELIVERER"));
    }
}
