package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.*;

import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;


    private List<Etudiant> etudiants;

    @BeforeEach
    public void setup() {
        // Création d'une liste d'étudiants pour le test réussi
        etudiants = Arrays.asList(new Etudiant("Alice", "Smith"), new Etudiant("Bob", "Johnson"));
    }

    @Test
    void contextLoads() {
        // Vérifie que le contexte se charge correctement
    }

    // ******************** Tester la méthode RetrieveAllEtudiantsSuccess **
// Test réussi
@Test
public void testRetrieveAllEtudiants_Success() {
    // Créer une liste d'étudiants fictifs
    Etudiant etudiant1 = new Etudiant("John", "Doe");
    Etudiant etudiant2 = new Etudiant("Jane", "Doe");

    List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);

    // Simuler le comportement du repository
    when(etudiantRepository.findAll()).thenReturn(etudiants);

    // Appeler la méthode à tester
    List<Etudiant> retrievedEtudiants = etudiantService.retrieveAllEtudiants();

    // Vérifier les assertions
    assertEquals(2, retrievedEtudiants.size());
    assertEquals("John", retrievedEtudiants.get(0).getNomE());
    assertEquals("Jane", retrievedEtudiants.get(1).getNomE());
}
//Test échoué
@Test
public void testRetrieveAllEtudiants_EmptyList() {
    // Simuler le comportement du repository retournant une liste vide
    when(etudiantRepository.findAll()).thenReturn(Collections.emptyList());

    // Appeler la méthode à tester
    List<Etudiant> retrievedEtudiants = etudiantService.retrieveAllEtudiants();

    // Vérifier que la liste retournée est vide
    assertEquals(0, retrievedEtudiants.size());
}


// ******************** Tester la méthode addEtudiant
//Test réussi
@Test
public void testAddEtudiant_Success() {
    // Créer un étudiant fictif
    Etudiant etudiant = new Etudiant("Alice", "Smith");

    // Simuler le comportement du repository pour retourner l'étudiant ajouté
    when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

    // Appeler la méthode à tester
    Etudiant addedEtudiant = etudiantService.addEtudiant(etudiant);

    // Vérifier les assertions
    assertEquals("Alice", addedEtudiant.getNomE());
    assertEquals("Smith", addedEtudiant.getPrenomE());
}

//Test échoué
@Test
public void testAddEtudiant_Failure() {
    // Créer un étudiant fictif
    Etudiant etudiant = new Etudiant("Alice", "Smith");

    // Simuler une exception lors de l'enregistrement
    when(etudiantRepository.save(etudiant)).thenThrow(new RuntimeException("Erreur d'ajout"));

    // Appeler la méthode à tester et vérifier que l'exception est levée
    assertThrows(RuntimeException.class, () -> {
        etudiantService.addEtudiant(etudiant);
    });
}


// ******************** Tester la méthode updateEtudiant
//Test réussi
@Test
public void testUpdateEtudiant_Success() {
    // Créer un étudiant fictif avec un ID
    Etudiant etudiant = new Etudiant(1, "Alice", "Smith", Option.GAMIX);

    // Simuler le comportement du repository pour retourner l'étudiant mis à jour
    when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

    // Appeler la méthode à tester
    Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiant);

    // Vérifier les assertions
    assertEquals("Alice", updatedEtudiant.getNomE());
    assertEquals("Smith", updatedEtudiant.getPrenomE());
}
//Test échoué
@Test
public void testUpdateEtudiant_Failure() {
    // Créer un étudiant fictif avec un ID
    Etudiant etudiant = new Etudiant(1, "Alice", "Smith",Option.GAMIX);

    // Simuler une exception lors de la mise à jour
    when(etudiantRepository.save(etudiant)).thenThrow(new RuntimeException("Erreur de mise à jour"));

    // Appeler la méthode à tester et vérifier que l'exception est levée
    assertThrows(RuntimeException.class, () -> {
        etudiantService.updateEtudiant(etudiant);
    });
}


// ******************** Tester la méthode retrieveEtudiant
//Test réussi
@Test
public void testRetrieveEtudiant_Success() {
    // Créer un étudiant fictif
    Etudiant etudiant = new Etudiant(1, "Alice", "Smith",Option.GAMIX);

    // Simuler le comportement du repository pour retourner l'étudiant
    when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

    // Appeler la méthode à tester
    Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(1);

    // Vérifier les assertions
    assertEquals("Alice", retrievedEtudiant.getNomE());
    assertEquals("Smith", retrievedEtudiant.getPrenomE());
}
//Test échoué
@Test
public void testRetrieveEtudiant_Failure() {
    // Simuler le comportement du repository pour retourner un Optional vide
    when(etudiantRepository.findById(1)).thenReturn(Optional.empty());

    // Appeler la méthode à tester et vérifier que cela lance une exception
    assertThrows(NoSuchElementException.class, () -> {
        etudiantService.retrieveEtudiant(1);
    });
}


// ******************** Tester la méthode removeEtudiant
//Test réussi
@Test
public void testRemoveEtudiant_Success() {
    // Créer un étudiant fictif
    Etudiant etudiant = new Etudiant(1, "Alice", "Smith",Option.GAMIX);

    // Simuler le comportement du repository pour retourner l'étudiant
    when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

    // Appeler la méthode à tester
    etudiantService.removeEtudiant(1);

    // Vérifier que le repository a appelé delete sur l'étudiant
    verify(etudiantRepository).delete(etudiant);
}
//Test échoué
@Test
public void testRemoveEtudiant_Failure() {
    // Simuler le comportement du repository pour retourner un Optional vide
    when(etudiantRepository.findById(1)).thenReturn(Optional.empty());

    // Appeler la méthode à tester et vérifier que cela lance une exception
    assertThrows(NoSuchElementException.class, () -> {
        etudiantService.removeEtudiant(1);
    });
}


// ******************** Tester la méthode assignEtudiantToDepartement
//Test réussi
@Test
public void testAssignEtudiantToDepartement_Success() {
    // Simuler un étudiant et un département qui existent
    Etudiant etudiant = new Etudiant();
    etudiant.setIdEtudiant(1);
    Departement departement = new Departement();
    departement.setIdDepart(2);

    // Configurer les mocks pour retourner les entités
    when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
    when(departementRepository.findById(2)).thenReturn(Optional.of(departement));

    // Appeler la méthode à tester
    etudiantService.assignEtudiantToDepartement(1, 2);

    // Vérifier que le département a été correctement assigné à l'étudiant
    assertEquals(departement, etudiant.getDepartement());
    verify(etudiantRepository).save(etudiant); // Vérifier que la méthode save a été appelée
}

//Test échoué
@Test
public void testAssignEtudiantToDepartement_Failure() {
    // ID d'étudiant qui n'existe pas
    Integer etudiantId = 999;
    // ID d'un département valide
    Integer departementId = 1;

    // Vérifier que NoSuchElementException est levée
    assertThrows(NoSuchElementException.class, () -> {
        etudiantService.assignEtudiantToDepartement(etudiantId, departementId);
    });
}


// ******************** Tester la méthode addAndAssignEtudiantToEquipeAndContract
//Test réussi

//Test échoué **

// ******************** Tester la méthode getEtudiantsByDepartement
//Test réussi
@Test
public void testGetEtudiantsByDepartement_Success() {
    // Configuration du mock pour retourner la liste d'étudiants
    Integer validDepartementId = 1;
    when(etudiantRepository.findEtudiantsByDepartement_IdDepart(validDepartementId)).thenReturn(etudiants);

    // Appel de la méthode de service
    List<Etudiant> result = etudiantService.getEtudiantsByDepartement(validDepartementId);

    // Vérifications des résultats
    assertNotNull(result, "La liste des étudiants ne devrait pas être null");
    assertEquals(2, result.size(), "La liste devrait contenir deux étudiants");
    assertEquals(etudiants, result, "La liste retournée devrait correspondre à la liste d'étudiants prévue");
}
//Test échoué




}
