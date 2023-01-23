package edu.tum.ase.authService;

import edu.tum.ase.backendCommon.model.AseUser;
import edu.tum.ase.authService.service.RfidService;
import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RfidServiceTest {

    @Mock
    RfidService.FetchDeliveriesUtil util;

    @Mock
    HttpServletRequest request;

    @InjectMocks
    RfidService rfidService;


    private final String boxId = "1";

    @BeforeEach
    public void setUp() {
        UserDetails user = new User(boxId, "",
                List.of(new SimpleGrantedAuthority(UserRole.getCompleteRole(UserRole.RASPI))));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    public void testCustomerAccessGranted() {
        // Given
        String customerId = "customer";

        Box box1 = new Box("Garching", "Garching", "123");
        box1.setId(boxId);
        Delivery delivery = new Delivery(customerId, "deliverer", box1);
        delivery.setStatus(DeliveryStatus.IN_TARGET_BOX);

        AseUser customer = new AseUser("foo@bar.de", "password", UserRole.CUSTOMER);
        customer.setId(customerId);

        when(util.fetchDeliveries(request, boxId)).thenReturn(box1.getDeliveries());

        // When
        boolean accessGranted = rfidService.checkCustomerAccess(request, customer);

        // When
        assertThat(accessGranted).isTrue();
    }

    @Test
    public void testCustomerAccessDeniedWrongUser() {
        // Given
        String customerId1 = "customer1";
        String customerId2 = "customer2";

        Box box1 = new Box("Garching", "Garching", "123");
        box1.setId(boxId);
        Delivery delivery = new Delivery(customerId2, "deliverer", box1); // Other customer here
        delivery.setStatus(DeliveryStatus.IN_TARGET_BOX);

        // Has wrong Id
        AseUser customer = new AseUser("foo@bar.de", "password", UserRole.CUSTOMER);
        customer.setId(customerId1);

        when(util.fetchDeliveries(request, boxId)).thenReturn(box1.getDeliveries());

        // When
        boolean accessGranted = rfidService.checkCustomerAccess(request, customer);

        // When
        assertThat(accessGranted).isFalse();
    }

    @Test
    public void testCustomerAccessDeniedNoDeliveries() {
        // Given
        String customerId = "customer";

        Box box1 = new Box("Garching", "Garching", "123");
        box1.setId(boxId);
        Delivery delivery = new Delivery(customerId, "deliverer", box1);
        delivery.setStatus(DeliveryStatus.PICKED_UP); // Wrong status here

        AseUser customer = new AseUser("foo@bar.de", "password", UserRole.CUSTOMER);
        customer.setId(customerId);

        when(util.fetchDeliveries(request, boxId)).thenReturn(box1.getDeliveries());

        // When
        boolean accessGranted = rfidService.checkCustomerAccess(request, customer);

        // When
        assertThat(accessGranted).isFalse();
    }

    @Test
    public void testDelivererAccessGranted() {
        // Given
        String delivererId = "deliverer";

        Box box1 = new Box("Garching", "Garching", "123");
        box1.setId(boxId);
        Delivery delivery = new Delivery("customer", delivererId, box1);
        delivery.setStatus(DeliveryStatus.PICKED_UP);

        AseUser deliverer = new AseUser("foo@bar.de", "password", UserRole.DELIVERER);
        deliverer.setId(delivererId);

        when(util.fetchDeliveries(request, boxId)).thenReturn(box1.getDeliveries());

        // When
        boolean accessGranted = rfidService.checkDelivererAccess(request, deliverer);

        // When
        assertThat(accessGranted).isTrue();
    }

    @Test
    public void testDelivererAccessDeniedWrongUser() {
        // Given
        String delivererId1 = "deliverer1";
        String delivererId2 = "deliverer2";

        Box box1 = new Box("Garching", "Garching", "123");
        box1.setId(boxId);
        Delivery delivery = new Delivery("customer", delivererId2, box1); // Other deliverer here
        delivery.setStatus(DeliveryStatus.PICKED_UP);

        // Has wrong Id
        AseUser deliverer = new AseUser("foo@bar.de", "password", UserRole.DELIVERER);
        deliverer.setId(delivererId1);

        when(util.fetchDeliveries(request, boxId)).thenReturn(box1.getDeliveries());

        // When
        boolean accessGranted = rfidService.checkDelivererAccess(request, deliverer);

        // When
        assertThat(accessGranted).isFalse();
    }

    @Test
    public void testDelivererAccessDeniedNoDeliveries() {
        // Given
        String delivererId = "deliverer";

        Box box1 = new Box("Garching", "Garching", "123");
        box1.setId(boxId);
        Delivery delivery = new Delivery("customer", delivererId, box1);
        delivery.setStatus(DeliveryStatus.ORDERED); // Wrong status here

        AseUser deliverer = new AseUser("foo@bar.de", "password", UserRole.DELIVERER);
        deliverer.setId(delivererId);

        when(util.fetchDeliveries(request, boxId)).thenReturn(box1.getDeliveries());

        // When
        boolean accessGranted = rfidService.checkDelivererAccess(request, deliverer);

        // When
        assertThat(accessGranted).isFalse();
    }

}
