package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AndererZuwendungsantragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
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
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Transactional
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragSearchIntegrationTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final ConfluentKafkaContainer KAFKA_CONTAINER = new ConfluentKafkaContainer(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_KAFKA_IMAGE));

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    private AntragBuilder antragBuilder;

    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private AndererZuwendungsantragRepository andereZuwendungsantragRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository,
                andereZuwendungsantragRepository);
    }

    @Test
    void testNullSearch() throws Exception {
        final Antrag antrag = antragBuilder.build();
        mockMvc
                .perform(get("/antrag")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testEmptySearch() throws Exception {
        final Antrag antrag = antragBuilder.build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchZammadNr() throws Exception {
        final Antrag antrag = antragBuilder.zammadNr("123000")
                .build();
        antragBuilder.zammadNr("456000")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "123000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchAktenzeichen() throws Exception {
        final Antrag antrag = antragBuilder.aktenzeichen("A-27")
                .build();
        antragBuilder.aktenzeichen("B-30")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "A-27")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchAntragstellerName() throws Exception {
        final Antrag antrag = antragBuilder.antragstellerName("Ursula Müller")
                .build();
        antragBuilder.antragstellerName("Hans Müller")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "Müller")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
        mockMvc
                .perform(get("/antrag")
                        .param("search", "Ursula")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchProjektTitel() throws Exception {
        final Antrag antrag = antragBuilder.projektTitel("Hausfest")
                .build();
        antragBuilder.projektTitel("Weihnachtsfest")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "fest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
        mockMvc
                .perform(get("/antrag")
                        .param("search", "Haus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

}
