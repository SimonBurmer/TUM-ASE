package edu.tum.ase.deliveryService;

import edu.tum.ase.backendCommon.exceptions.SingleCustomerPerBoxViolationException;
import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.backendCommon.request.CreatedNotificationRequest;
import edu.tum.ase.deliveryService.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.deliveryService.exceptions.BoxHasPendingDeliveriesException;
import edu.tum.ase.deliveryService.exceptions.BoxNotFoundException;
import edu.tum.ase.deliveryService.exceptions.DeliveryModificationNotAllowedException;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import edu.tum.ase.deliveryService.service.BoxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BoxServiceTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BoxService boxService;

    @Test
    public void testCreateSuccessful() {
        // Given
        Box before = new Box("Garching", "Garching", "123");
        when(boxRepository.save(before)).thenReturn(before);

        // When
        Box after = boxService.createBox(before);

        // Then
        assertThat(after).isEqualTo(before);
        verify(boxRepository).save(before);
    }

    @Test
    public void testCreateDuplicate() {
        // Given
        String name = "Garching";
        Box box1 = new Box(name, "Garching", "123");

        when(boxRepository.findByName(name)).thenReturn(Optional.of(box1));

        // When
        Box before = new Box(name, "Garching", "123");

        // Then
        assertThatThrownBy(() -> boxService.createBox(before)).isInstanceOf(BoxAlreadyExistsException.class);
        verify(boxRepository).findByName(name);
    }

    @Test
    public void testGetAll() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");
        Box box2 = new Box("Ismaning", "Ismaning", "234");
        Box box3 = new Box("Schwabing", "Schwabing", "345");

        List<Box> allBoxes = new java.util.ArrayList<>(List.of(new Box[]{box1, box2, box3}));
        when(boxRepository.findAll()).thenReturn(allBoxes);

        // When
        List<Box> result = boxService.getAllBoxes();

        // Then
        assertThat(result).containsExactlyInAnyOrderElementsOf(allBoxes);
        verify(boxRepository).findAll();
    }

    @Test
    public void testGetByLocation() {
        // Given
        String name1 = "Garching";
        Box box1 = new Box(name1, "Garching", "123");

        String location2 = "Schwabing";

        when(boxRepository.findByName(name1)).thenReturn(Optional.of(box1));
        when(boxRepository.findByName(location2)).thenReturn(Optional.empty());

        // When
        Box result = boxService.findByName(name1);

        // Then
        assertThat(result).isEqualTo(box1);
        assertThatThrownBy(() -> boxService.findByName(location2)).isInstanceOf(BoxNotFoundException.class);
        verify(boxRepository).findByName(name1);
        verify(boxRepository).findByName(location2);
    }

    @Test
    public void testUpdate() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");

        //When
        box1.setName("Maxvorstadt");
        box1.setAddress("Maxvorstadt");
        boxService.updateBox(box1);

        //Then
        verify(boxRepository).save(box1);
    }

    @Test
    public void testUpdateWhenHasPendingDeliveries() {
        // Given
        Box box1 = new Box("Ismaning", "Ismaning", "234");
        Delivery delivery = new Delivery("customer", "deliverer", box1);

        // When
        box1.setName("Garching");
        box1.setAddress("Garching");

        // Then
        assertThatThrownBy(() -> boxService.updateBox(box1)).isInstanceOf(BoxHasPendingDeliveriesException.class);
    }

    @Test
    public void testDelete() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");

        //When
        boxService.deleteBox(box1);

        //Then
        verify(boxRepository).delete(box1);
    }

    @Test
    public void testDeleteWhenHasPendingDeliveries() {
        // Given
        Box box1 = new Box("Ismaning", "Ismaning", "234");
        Delivery delivery = new Delivery("customer", "deliverer", box1);

        // When & Then
        assertThatThrownBy(() -> boxService.deleteBox(box1)).isInstanceOf(BoxHasPendingDeliveriesException.class);

    }

    @Test
    public void testAssignDeliverySuccessful() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");
        Box box2 = new Box("Ismaning", "Ismaning", "234");

        Delivery delivery1 = new Delivery("customer", "deliverer", box1);
        Delivery delivery2 = new Delivery("customer", "deliverer", box2);

        // When
        boxService.assignDelivery(box1, delivery1);
        boxService.assignDelivery(box2, delivery2);

        // Then
        verify(boxRepository).save(box1);
        verify(boxRepository).save(box2);
        verify(deliveryRepository).save(delivery1);
        verify(deliveryRepository).save(delivery1);
        verify(restTemplate, times(2)).postForEntity("lb://EMAIL-SERVICE/email/notificationCreated",
                new CreatedNotificationRequest(delivery1.getCustomer(), null), String.class);
        assertThat(box1.getDeliveries()).contains(delivery1);
        assertThat(box2.getDeliveries()).contains(delivery2);
        assertThat(delivery1.getBox()).isEqualTo(box1);
        assertThat(delivery2.getBox()).isEqualTo(box2);

        // When
        boxService.assignDelivery(box1, delivery2);

        // Then
        assertThat(delivery2.getBox()).isEqualTo(box1);
        assertThat(box2.getDeliveries()).doesNotContain(delivery2);
    }

    @ParameterizedTest
    @EnumSource(DeliveryStatus.class)
    public void testAssignDeliveryStatusNotAllowed(DeliveryStatus status) {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");
        Box box2 = new Box("Ismaning", "Ismaning", "234");
        Delivery delivery1 = new Delivery("customer", "deliverer", box1);
        box1.addDelivery(delivery1);
        delivery1.setStatus(status);

        // When & Then
        if (!status.canBeModified()) {
            assertThatThrownBy(() -> boxService.assignDelivery(box2, delivery1)).isInstanceOf(DeliveryModificationNotAllowedException.class);
        } else {
            assertThatNoException().isThrownBy(() -> boxService.assignDelivery(box2, delivery1));
        }
    }

    @Test
    public void testAssignDeliverySingleCustomerViolation() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");
        Delivery delivery1 = new Delivery("customer1", "deliverer", box1);

        Delivery delivery2 = new Delivery();
        delivery2.setCustomer("customer2");
        delivery2.setDeliverer("deliverer");

        box1.addDelivery(delivery1);

        // When & Then
        assertThatThrownBy(() -> new Delivery("customer2", "deliverer", box1)).isInstanceOf(SingleCustomerPerBoxViolationException.class);
        assertThatThrownBy(() -> boxService.assignDelivery(box1, delivery2)).isInstanceOf(SingleCustomerPerBoxViolationException.class);
    }

    @Test
    void testPlaceDeliveries() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");

        Delivery delivery1 = new Delivery("customer1", "deliverer1", box1);
        delivery1.setStatus(DeliveryStatus.PICKED_UP);
        Delivery delivery2 = new Delivery("customer1", "deliverer1", box1);
        delivery2.setStatus(DeliveryStatus.ORDERED);
        Delivery delivery3 = new Delivery("customer1", "deliverer2", box1);
        delivery3.setStatus(DeliveryStatus.PICKED_UP);

        // When
        boxService.placeDeliveries(box1, "deliverer1");

        // Then
        assertThat(delivery1.getStatus()).isEqualTo(DeliveryStatus.IN_TARGET_BOX); // Changed
        assertThat(delivery2.getStatus()).isEqualTo(DeliveryStatus.ORDERED); // Unchanged because of wrong status
        assertThat(delivery3.getStatus()).isEqualTo(DeliveryStatus.PICKED_UP); // Unchanged because of wrong deliverer

        verify(deliveryRepository).save(delivery1);
        verify(boxRepository).save(box1);
    }

    @Test
    void testRetrieveDeliveries() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");

        Delivery delivery1 = new Delivery("customer1", "deliverer1", box1);
        delivery1.setStatus(DeliveryStatus.IN_TARGET_BOX);
        Delivery delivery2 = new Delivery("customer1", "deliverer1", box1);
        delivery2.setStatus(DeliveryStatus.PICKED_UP);

        // When
        boxService.retrieveDeliveries(box1);

        // Then
        assertThat(delivery1.getStatus()).isEqualTo(DeliveryStatus.DELIVERED); // Changed
        assertThat(delivery2.getStatus()).isEqualTo(DeliveryStatus.PICKED_UP); // Unchanged because of wrong status

        verify(deliveryRepository).save(delivery1);
        verify(boxRepository).save(box1);
    }
}
