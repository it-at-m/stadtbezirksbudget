package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AndererZuwendungsantragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
abstract class AntragBaseIntegrationTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);
    @Autowired
    protected AntragRepository antragRepository;
    @Autowired
    protected FinanzierungRepository finanzierungRepository;
    @Autowired
    protected VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    protected FinanzierungsmittelRepository finanzierungsmittelRepository;
    @Autowired
    protected AndererZuwendungsantragRepository andereZuwendungsantragRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @PersistenceContext
    protected EntityManager entityManager;

    protected AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository,
                andereZuwendungsantragRepository);
    }

}
