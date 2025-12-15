package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AdresseRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragstellerRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BankverbindungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BearbeitungsstandRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ProjektRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragSortingIntegrationTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);
    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private AdresseRepository adresseRepository;
    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private AntragstellerRepository antragstellerRepository;
    @Autowired
    private ProjektRepository projektRepository;
    @Autowired
    private BearbeitungsstandRepository bearbeitungsstandRepository;
    @Autowired
    private BankverbindungRepository bankverbindungRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private MockMvc mockMvc;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragBuilder = new AntragBuilder(antragRepository, adresseRepository,
                finanzierungRepository, antragstellerRepository, projektRepository, bearbeitungsstandRepository, bankverbindungRepository,
                finanzierungsmittelRepository, voraussichtlicheAusgabeRepository);
    }

    @Test
    void testGivenSortAscThenReturnSortedResults() throws Exception {
        antragBuilder.setStatus(Status.VOLLSTAENDIG)
                .build();
        antragBuilder.setStatus(Status.VOLLSTAENDIG)
                .build();
        final Antrag antrag = antragBuilder.setStatus(Status.EINGEGANGEN)
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "status")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testGivenSortDescThenReturnSortedResults() throws Exception {
        antragBuilder.setZammadNr("1")
                .build();
        antragBuilder.setZammadNr("2")
                .build();
        final Antrag antrag = antragBuilder.setZammadNr("3")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "zammadNr")
                        .param("sortDirection", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testGivenSortUnpagedThenReturnSortedResults() throws Exception {
        antragBuilder.setProjektTitel("2")
                .build();
        antragBuilder.setProjektTitel("3")
                .build();
        final Antrag antrag = antragBuilder.setProjektTitel("1")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("size", "-1")
                        .param("sortBy", "projektTitel")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testGivenInvalidSortByThenReturnBadRequest() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "invalidField")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGivenInvalidSortDirectionThenReturnBadRequest() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "beantragtesBudget")
                        .param("sortDirection", "INVALID_DIRECTION")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
