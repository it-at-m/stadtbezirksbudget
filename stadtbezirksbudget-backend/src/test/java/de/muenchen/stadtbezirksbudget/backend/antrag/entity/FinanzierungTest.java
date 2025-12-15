package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc
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
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;

    private Finanzierung createFinanzierung(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungsmittelBetrag) {
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
                .finanzierung(finanzierung)
                .build()).toList();

        final List<Finanzierungsmittel> finanzierungsmittel = finanzierungsmittelBetrag.stream().map(betrag -> Finanzierungsmittel.builder()
                .kategorie(Kategorie.EIGENMITTEL)
                .betrag(betrag)
                .direktoriumNotiz("Notiz")
                .finanzierung(finanzierung)
                .build()).toList();

        finanzierung.setVoraussichtlicheAusgaben(voraussichtlicheAusgaben);
        finanzierung.setFinanzierungsmittel(finanzierungsmittel);

        final Finanzierung savedFinanzierung = finanzierungRepository.save(finanzierung);
        voraussichtlicheAusgabeRepository.saveAll(voraussichtlicheAusgaben);
        finanzierungsmittelRepository.saveAll(finanzierungsmittel);

        entityManager.flush();
        entityManager.clear();

        return savedFinanzierung;
    }

    @Nested
    class Art {
        @Test
        void testAusgabenMoreThan5000WithoutFinanzierungsmittelReturnsFEHL() {
            final Finanzierung finanzierung = createFinanzierung(
                    List.of(BigDecimal.valueOf(5000), BigDecimal.ONE),
                    List.of(BigDecimal.ZERO));
            final Finanzierung loaded = finanzierungRepository.findById(finanzierung.getId()).orElseThrow();
            assertThat(loaded.getArt()).isEqualTo(FinanzierungArt.FEHL);
        }

        @Test
        void testAusgabenLessThan5000WithoutFinanzierungsmittelReturnsFEST() {
            final Finanzierung finanzierung = createFinanzierung(
                    List.of(BigDecimal.valueOf(5000)),
                    List.of(BigDecimal.ZERO));
            final Finanzierung loaded = finanzierungRepository.findById(finanzierung.getId()).orElseThrow();
            assertThat(loaded.getArt()).isEqualTo(FinanzierungArt.FEST);
        }

        @Test
        void testAusgabenMoreThan5000WithFinanzierungsmittelReturnsFEHL() {
            final Finanzierung finanzierung = createFinanzierung(
                    List.of(BigDecimal.valueOf(5000), BigDecimal.ONE),
                    List.of(BigDecimal.ZERO, BigDecimal.ONE));
            final Finanzierung loaded = finanzierungRepository.findById(finanzierung.getId()).orElseThrow();
            assertThat(loaded.getArt()).isEqualTo(FinanzierungArt.FEHL);
        }

        @Test
        void testAusgabenLessThan5000WithFinanzierungsmittelReturnsFEHL() {
            final Finanzierung finanzierung = createFinanzierung(
                    List.of(BigDecimal.valueOf(5000)),
                    List.of(BigDecimal.ONE));
            final Finanzierung loaded = finanzierungRepository.findById(finanzierung.getId()).orElseThrow();
            assertThat(loaded.getArt()).isEqualTo(FinanzierungArt.FEHL);
        }
    }
}
