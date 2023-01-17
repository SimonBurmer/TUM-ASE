package edu.tum.ase.deliveryService;

import edu.tum.ase.backendCommon.model.Box;
import edu.tum.ase.backendCommon.model.Delivery;
import edu.tum.ase.backendCommon.model.DeliveryStatus;
import edu.tum.ase.deliveryService.exceptions.DeliveryDeletionNotAllowedException;
import edu.tum.ase.deliveryService.exceptions.DeliveryModificationNotAllowedException;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import edu.tum.ase.deliveryService.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeliveryServiceTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    public void testGetAll() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");

        Delivery delivery1 = new Delivery("customer", "deliverer", box1);
        Delivery delivery2 = new Delivery("customer", "deliverer1", box1);
        Delivery delivery3 = new Delivery("customer", "deliverer2", box1);

        List<Delivery> allDeliveries = List.of(delivery1, delivery2, delivery3);
        when(deliveryRepository.findAll()).thenReturn(allDeliveries);

        // When
        List<Delivery> result = deliveryService.getAllDeliveries();

        // Then
        assertThat(result).containsExactlyInAnyOrderElementsOf(allDeliveries);
        verify(deliveryRepository).findAll();
    }

    @ParameterizedTest
    @EnumSource(DeliveryStatus.class)
    public void testUpdate(DeliveryStatus status) {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");
        Delivery delivery1 = new Delivery("customer", "deliverer", box1);
        delivery1.setStatus(status);

        // When & Then
        if (!status.canBeModified()) {
            assertThatThrownBy(() -> deliveryService.updateDelivery(delivery1)).isInstanceOf(DeliveryModificationNotAllowedException.class);
        } else {
            assertThatNoException().isThrownBy(() -> deliveryService.updateDelivery(delivery1));

            verify(deliveryRepository).save(delivery1);
        }
    }

    @ParameterizedTest
    @EnumSource(DeliveryStatus.class)
    public void testDelete(DeliveryStatus status) {
        // Given
        Box box1 = Mockito.mock(Box.class);
        Delivery delivery1 = new Delivery("customer", "deliverer", box1);
        delivery1.setBox(box1); // This is not done via constructor because box1 is a mock
        delivery1.setStatus(status);

        // When & Then
        if (!status.canBeRemoved()) {

            assertThatThrownBy(() -> deliveryService.deleteDelivery(delivery1)).isInstanceOf(DeliveryDeletionNotAllowedException.class);
        } else {
            assertThatNoException().isThrownBy(() -> deliveryService.deleteDelivery(delivery1));

            verify(box1).removeDelivery(delivery1);
            verify(boxRepository).save(box1);
            verify(deliveryRepository).delete(delivery1);
        }
    }
}
