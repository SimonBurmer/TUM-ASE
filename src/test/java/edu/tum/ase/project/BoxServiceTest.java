package edu.tum.ase.project;

import edu.tum.ase.project.exceptions.BoxAlreadyExistsException;
import edu.tum.ase.project.exceptions.BoxNotFoundException;
import edu.tum.ase.project.model.Box;
import edu.tum.ase.project.repository.BoxRepository;
import edu.tum.ase.project.service.BoxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class BoxServiceTest {

    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private BoxService boxService;

    @Test
    public void testCreateSuccessful() {
        // Given
        Box before = new Box("Garching");
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
        String location = "Garching";
        Box box1 = new Box(location);

        when(boxRepository.findByLocation(location)).thenReturn(Optional.of(box1));

        // When
        Box before = new Box(location);

        // Then
        assertThatThrownBy(() -> boxService.createBox(before)).isInstanceOf(BoxAlreadyExistsException.class);
        verify(boxRepository).findByLocation(location);
    }

    @Test
    public void testGetAll() {
        // Given
        Box box1 = new Box("Garching");
        Box box2 = new Box("Ismaning");
        Box box3 = new Box("Schwabing");

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
        String location1 = "Garching";
        Box box1 = new Box(location1);

        String location2 = "Schwabing";

        when(boxRepository.findByLocation(location1)).thenReturn(Optional.of(box1));
        when(boxRepository.findByLocation(location2)).thenReturn(Optional.empty());

        // When
        Box result = boxService.findByLocation(location1);

        // Then
        assertThat(result).isEqualTo(box1);
        assertThatThrownBy(() -> boxService.findByLocation(location2)).isInstanceOf(BoxNotFoundException.class);
        verify(boxRepository).findByLocation(location1);
        verify(boxRepository).findByLocation(location2);
    }

    @Test
    public void testDelete() {
        // Given
        Box box1 = new Box("Garching");

        //When
        boxService.delete(box1);

        //Then
        verify(boxRepository).delete(box1);
    }
}
