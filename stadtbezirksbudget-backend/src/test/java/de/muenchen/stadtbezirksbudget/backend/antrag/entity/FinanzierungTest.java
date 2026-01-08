package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.integration.AntragBuilder;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
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
@SuppressWarnings({ "PMD.ShortClassName" })
class FinanzierungTest {
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
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;

    private Finanzierung createFinanzierung(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final Antrag antrag) {
        final Finanzierung finanzierung = Finanzierung.builder()
                .istProjektVorsteuerabzugsberechtigt(false)
                .kostenAnmerkung("Kosten Anmerkung")
                .summeAusgaben(BigDecimal.ZERO)
                .summeFinanzierungsmittel(BigDecimal.ZERO)
                .begruendungEigenmittel("Begründung")
                .beantragtesBudget(BigDecimal.ZERO)
                .istEinladungFoerderhinweis(false)
                .istWebsiteFoerderhinweis(false)
                .istSonstigerFoerderhinweis(false)
                .sonstigeFoerderhinweise("")
                .bewilligterZuschuss(BigDecimal.ZERO)
                .build();

        final List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben = ausgaben.stream().map(betrag -> VoraussichtlicheAusgabe.builder()
                .kategorie("Kategorie")
                .betrag(betrag)
                .direktoriumNotiz("Notiz")
                .antrag(antrag)
                .build()).toList();

        final List<Finanzierungsmittel> finanzierungsmittel = finanzierungen.stream().map(betrag -> Finanzierungsmittel.builder()
                .kategorie(Kategorie.EIGENMITTEL)
                .betrag(betrag)
                .direktoriumNotiz("Notiz")
                .antrag(antrag)
                .build()).toList();

        finanzierung.setVoraussichtlicheAusgaben(voraussichtlicheAusgaben);
        finanzierung.setFinanzierungsmittel(finanzierungsmittel);

        return finanzierung;
    }

    @Nested
    class Art {
        private AntragBuilder antragBuilder;

        private static Arguments args(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final FinanzierungArt art) {
            return Arguments.of(ausgaben, finanzierungen, art);
        }

        private static Stream<Arguments> artTestData() {
            return Stream.of(
                    args(List.of(BigDecimal.valueOf(5000), BigDecimal.ONE),
                            List.of(BigDecimal.ZERO),
                            FinanzierungArt.FEHL),
                    args(List.of(BigDecimal.valueOf(5000)),
                            List.of(BigDecimal.ZERO),
                            FinanzierungArt.FEST),
                    args(List.of(BigDecimal.valueOf(5000), BigDecimal.ONE),
                            List.of(BigDecimal.ZERO, BigDecimal.ONE),
                            FinanzierungArt.FEHL),
                    args(List.of(BigDecimal.valueOf(5000)),
                            List.of(BigDecimal.ONE),
                            FinanzierungArt.FEHL));
        }

        @BeforeEach
        public void setUp() {
            antragBuilder = new AntragBuilder(antragRepository, finanzierungsmittelRepository, voraussichtlicheAusgabeRepository);
        }

        @ParameterizedTest
        @MethodSource("artTestData")
        void testArtDetermination(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final FinanzierungArt expectedArt) {
            Antrag antrag = antragBuilder.getAntrag();
            antrag.setFinanzierung(createFinanzierung(ausgaben, finanzierungen, antrag));
            antrag = antragRepository.save(antrag);
            entityManager.flush();
            entityManager.clear();
            assertThat(antragRepository.findById(antrag.getId()).orElseThrow().getFinanzierung().getArt()).isEqualTo(expectedArt);
        }
    }
}
