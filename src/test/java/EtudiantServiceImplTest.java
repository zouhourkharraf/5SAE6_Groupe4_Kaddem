
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MySQLContainer;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

class EtudiantServiceImplTest {

    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("root");

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    @BeforeAll
    public static void setUp() {
        mysqlContainer.start();
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }

    @BeforeEach
    void setUpService() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllEtudiants() {
        List<Etudiant> etudiants = List.of(new Etudiant("John", "Doe"), new Etudiant("Jane", "Doe"));
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testAddEtudiant() {
        Etudiant etudiant = new Etudiant("John", "Doe");
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);
        assertNotNull(result);
        assertEquals("John", result.getNomE());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testUpdateEtudiant() {
        Etudiant etudiant = new Etudiant("John", "Doe");
        etudiant.setIdEtudiant(1);
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.updateEtudiant(etudiant);
        assertNotNull(result);
        assertEquals(1, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testRetrieveEtudiant() {
        Etudiant etudiant = new Etudiant("John", "Doe");
        etudiant.setIdEtudiant(1);
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1);
        assertNotNull(result);
        assertEquals(1, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1);
    }

    @Test
    void testAssignEtudiantToDepartement() {
        Etudiant etudiant = new Etudiant("John", "Doe");
        etudiant.setIdEtudiant(1);
        Departement departement = new Departement();
        departement.setIdDepart(2);

        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(2)).thenReturn(Optional.of(departement));

        etudiantService.assignEtudiantToDepartement(1, 2);

        verify(etudiantRepository, times(1)).save(etudiant);
        assertEquals(departement, etudiant.getDepartement());
    }

    @Test
    void testAddAndAssignEtudiantToEquipeAndContract() {
        // Arrange
        Etudiant etudiant = new Etudiant("John", "Doe");
        etudiant.setIdEtudiant(1);

        Contrat contrat = new Contrat();

        // Initialize the equipe's etudiants collection
        Equipe equipe = new Equipe();
        equipe.setEtudiants(new HashSet<>()); // Assuming 'etudiants' is a Set

        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        when(equipeRepository.findById(2)).thenReturn(Optional.of(equipe));

        // Act
        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, 1, 2);

        // Assert
        verify(contratRepository, times(1)).findById(1);
        verify(equipeRepository, times(1)).findById(2);
        assertEquals(etudiant, contrat.getEtudiant());
        assertTrue(equipe.getEtudiants().contains(etudiant));
        verify(etudiantRepository, never()).save(etudiant); // No save needed if using @Transactional
    }

}
