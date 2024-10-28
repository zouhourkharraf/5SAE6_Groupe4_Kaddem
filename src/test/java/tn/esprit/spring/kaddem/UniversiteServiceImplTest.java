package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {KaddemApplication.class})
@SpringBootTest
class UniversiteServiceImplTest {

    @Mock
    UniversiteRepository universiteRepository;

    @Mock
    DepartementRepository departementRepository;

    @InjectMocks
    UniversiteServiceImpl universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllUniversites() {
        List<Universite> universites = Arrays.asList(new Universite(), new Universite());
        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.retrieveAllUniversites();

        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testAddUniversite() {
        Universite u = new Universite();
        when(universiteRepository.save(u)).thenReturn(u);

        Universite result = universiteService.addUniversite(u);

        assertNotNull(result);
        verify(universiteRepository, times(1)).save(u);
    }

    @Test
    void testUpdateUniversite() {
        Universite u = new Universite();
        when(universiteRepository.save(u)).thenReturn(u);

        Universite result = universiteService.updateUniversite(u);

        assertNotNull(result);
        verify(universiteRepository, times(1)).save(u);
    }

    @Test
    void testRetrieveUniversite() {
        Universite u = new Universite();
        when(universiteRepository.findById(1)).thenReturn(Optional.of(u));

        Universite result = universiteService.retrieveUniversite(1);

        assertNotNull(result);
        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUniversite() {
        Universite u = new Universite();
        when(universiteRepository.findById(1)).thenReturn(Optional.of(u));

        universiteService.deleteUniversite(1);

        verify(universiteRepository, times(1)).delete(u);
    }

    @Test
    void testAssignUniversiteToDepartement() {
        Universite u = new Universite();
        Departement d = new Departement();
        u.setDepartements(new HashSet<>());

        when(universiteRepository.findById(1)).thenReturn(Optional.of(u));
        when(departementRepository.findById(1)).thenReturn(Optional.of(d));

        universiteService.assignUniversiteToDepartement(1, 1);

        assertTrue(u.getDepartements().contains(d));
        verify(universiteRepository, times(1)).save(u);
    }

    @Test
    void testRetrieveDepartementsByUniversite() {
        Universite u = new Universite();
        Set<Departement> departements = new HashSet<>(Arrays.asList(new Departement(), new Departement()));
        u.setDepartements(departements);

        when(universiteRepository.findById(1)).thenReturn(Optional.of(u));

        Set<Departement> result = universiteService.retrieveDepartementsByUniversite(1);

        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findById(1);
    }
}
