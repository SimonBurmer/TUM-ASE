package edu.tum.ase.deliveryService;

import edu.tum.ase.deliveryService.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.deliveryService.exceptions.BoxNotFoundException;
import edu.tum.ase.deliveryService.model.Box;
import edu.tum.ase.deliveryService.repository.BoxRepository;
import edu.tum.ase.deliveryService.repository.DeliveryRepository;
import edu.tum.ase.deliveryService.service.BoxService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BoxServiceTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

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
    public void testDelete() {
        // Given
        Box box1 = new Box("Garching", "Garching", "123");

        //When
        boxService.deleteBox(box1);

        //Then
        verify(boxRepository).delete(box1);
    }
}
