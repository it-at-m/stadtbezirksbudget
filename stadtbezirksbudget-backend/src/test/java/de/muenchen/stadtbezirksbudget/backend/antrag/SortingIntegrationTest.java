package de.muenchen.stadtbezirksbudget.backend.antrag;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_AKTENZEICHEN;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_AKTUALISIERUNG_ART;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_BEANTRAGTES_BUDGET;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_BEZIRKSAUSSCHUSS_NR;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_DATUM;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_IST_FEHLBETRAG;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_STATUS;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.DEFAULT_ZAMMAD_NR;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.getDefaultAntragstellerName;
import static de.muenchen.stadtbezirksbudget.backend.antrag.AntragTestDataBuilder.getDefaultProjektTitel;
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
class SortingIntegrationTest {
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

    private AntragTestDataBuilder antragTestDataBuilder;

    @BeforeEach
    public void setUp() {
        antragTestDataBuilder = new AntragTestDataBuilder(antragRepository, adresseRepository,
                finanzierungRepository, antragstellerRepository, projektRepository, bearbeitungsstandRepository, bankverbindungRepository,
                finanzierungsmittelRepository, voraussichtlicheAusgabeRepository);
    }

    @Test
    void testGivenSortAscThenReturnSortedResults() throws Exception {
        antragTestDataBuilder.initializeAntrag(Status.VOLLSTAENDIG, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(),
                getDefaultProjektTitel(), DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, DEFAULT_ZAMMAD_NR,
                DEFAULT_AKTENZEICHEN);
        antragTestDataBuilder.initializeAntrag(Status.VOLLSTAENDIG, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(),
                getDefaultProjektTitel(), DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, DEFAULT_ZAMMAD_NR,
                DEFAULT_AKTENZEICHEN);
        final Antrag antrag = antragTestDataBuilder.initializeAntrag(Status.EINGEGANGEN, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM,
                getDefaultAntragstellerName(), getDefaultProjektTitel(), DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART,
                DEFAULT_DATUM, DEFAULT_ZAMMAD_NR, DEFAULT_AKTENZEICHEN);

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
        antragTestDataBuilder.initializeAntrag(DEFAULT_STATUS, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(),
                getDefaultProjektTitel(), DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, "1",
                DEFAULT_AKTENZEICHEN);
        antragTestDataBuilder.initializeAntrag(DEFAULT_STATUS, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(),
                getDefaultProjektTitel(), DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, "2",
                DEFAULT_AKTENZEICHEN);
        final Antrag antrag = antragTestDataBuilder.initializeAntrag(DEFAULT_STATUS, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(),
                getDefaultProjektTitel(), DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, "3",
                DEFAULT_AKTENZEICHEN);

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
        antragTestDataBuilder.initializeAntrag(DEFAULT_STATUS, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(), "2",
                DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, DEFAULT_ZAMMAD_NR, DEFAULT_AKTENZEICHEN);
        final Antrag antrag = antragTestDataBuilder.initializeAntrag(DEFAULT_STATUS, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(),
                "1",
                DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, DEFAULT_ZAMMAD_NR, DEFAULT_AKTENZEICHEN);
        antragTestDataBuilder.initializeAntrag(DEFAULT_STATUS, DEFAULT_BEZIRKSAUSSCHUSS_NR, DEFAULT_DATUM, getDefaultAntragstellerName(), "3",
                DEFAULT_BEANTRAGTES_BUDGET, DEFAULT_IST_FEHLBETRAG, DEFAULT_AKTUALISIERUNG_ART, DEFAULT_DATUM, DEFAULT_ZAMMAD_NR, DEFAULT_AKTENZEICHEN);

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
