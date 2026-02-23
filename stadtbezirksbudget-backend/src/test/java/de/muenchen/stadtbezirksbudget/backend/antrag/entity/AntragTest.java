package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.integration.AntragBuilder;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AndererZuwendungsantragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class AntragTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;
    @Autowired
    private AndererZuwendungsantragRepository andereZuwendungsantragRepository;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository,
                andereZuwendungsantragRepository);
        entityManager.flush();
        entityManager.clear();
    }

    private Antrag createAntrag(final List<BigDecimal> betraege) {
        if (betraege == null) {
            return antragBuilder.build();
        }
        final List<AndererZuwendungsantrag> andereZuwendungsantraege = betraege.stream()
                .map(betrag -> AndererZuwendungsantrag.builder().betrag(betrag).stelle("A").antragsdatum(LocalDate.now()).build())
                .collect(Collectors.toList());
        return antragBuilder.andererZuwendungsantrag(andereZuwendungsantraege).build();
    }

    @Nested
    class ZuwendungDritterBeantragt {
        private static Arguments args(final List<BigDecimal> betraege, final boolean expectedResult) {
            return Arguments.of(betraege, expectedResult);
        }

        private static Stream<Arguments> zuwendungDritterBeantragtTestData() {
            return Stream.of(
                    args(List.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(2000)), true),
                    args(List.of(BigDecimal.ZERO), true),
                    args(List.of(), false));
        }

        @ParameterizedTest
        @MethodSource("zuwendungDritterBeantragtTestData")
        void testZuwendungDritterBeantragt(final List<BigDecimal> betraege, final boolean expectedResult) {
            final Antrag created = createAntrag(betraege);
            entityManager.flush();
            entityManager.clear();
            final Antrag loaded = antragRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.isIstZuwendungDritterBeantragt()).isEqualTo(expectedResult);
        }
    }

    @Nested
    class SummeAndereZuwendungsantraege {
        private static Arguments args(final List<BigDecimal> betraege, final BigDecimal expectedSumme) {
            return Arguments.of(betraege, expectedSumme);
        }

        private static Stream<Arguments> summeAndereZuwendungsantraegeTestData() {
            return Stream.of(
                    args(List.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(0)), BigDecimal.valueOf(1000)),
                    args(List.of(BigDecimal.valueOf(500), BigDecimal.valueOf(1500)), BigDecimal.valueOf(2000)),
                    args(List.of(BigDecimal.valueOf(0)), BigDecimal.valueOf(0)),
                    args(List.of(), BigDecimal.valueOf(0)));
        }

        @ParameterizedTest
        @MethodSource("summeAndereZuwendungsantraegeTestData")
        void testSummeAndereZuwendungsantraege(final List<BigDecimal> betraege, final BigDecimal expectedSumme) {
            final Antrag created = createAntrag(betraege);
            entityManager.flush();
            entityManager.clear();
            final Antrag loaded = antragRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.getSummeAndereZuwendungsantraege()).isEqualByComparingTo(expectedSumme);
        }
    }

    @Nested
    class BeschlussStatusFormula {
        private static Arguments args(final BigDecimal beantragtesBudget, final BigDecimal bewilligteFoerderung, final BeschlussStatus expectedStatus) {
            return Arguments.of(beantragtesBudget, bewilligteFoerderung, expectedStatus);
        }

        private static Stream<Arguments> antragStatusTestData() {
            return Stream.of(
                    args(BigDecimal.valueOf(1000), null, BeschlussStatus.LEER),
                    args(BigDecimal.valueOf(1000), BigDecimal.ZERO, BeschlussStatus.ABGELEHNT),
                    args(BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), BeschlussStatus.BEWILLIGT),
                    args(BigDecimal.valueOf(1000), BigDecimal.valueOf(500), BeschlussStatus.TEILBEWILLIGT),
                    args(BigDecimal.valueOf(1000), BigDecimal.valueOf(1500), BeschlussStatus.TEILBEWILLIGT));
        }

        @ParameterizedTest
        @MethodSource("antragStatusTestData")
        void testAntragStatus(final BigDecimal beantragtesBudget, final BigDecimal bewilligteFoerderung, final BeschlussStatus expectedStatus) {
            final Antrag created = antragBuilder
                    .beantragtesBudget(beantragtesBudget)
                    .bewilligteFoerderung(bewilligteFoerderung)
                    .build();
            entityManager.flush();
            entityManager.clear();
            final Antrag loaded = antragRepository.findById(created.getId()).orElseThrow();
            final BeschlussStatus status = loaded.getBeschlussStatus();
            assertThat(status).isEqualByComparingTo(expectedStatus);
        }
    }
}
