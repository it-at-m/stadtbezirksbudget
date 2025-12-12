package de.muenchen.stadtbezirksbudget.backend.antrag;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.integration.AntragBuilder;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AdresseRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragstellerRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BankverbindungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BearbeitungsstandRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ProjektRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
class FilteringIntegrationTest {
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
    @PersistenceContext
    private EntityManager entityManager;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragBuilder = new AntragBuilder(antragRepository, adresseRepository,
                finanzierungRepository, antragstellerRepository, projektRepository, bearbeitungsstandRepository, bankverbindungRepository,
                finanzierungsmittelRepository, voraussichtlicheAusgabeRepository);
    }

    @Test
    void testFilterByStatus() throws Exception {
        antragBuilder.setStatus(Status.EINGEGANGEN)
                .build();
        antragBuilder.setStatus(Status.EINGEGANGEN)
                .build();
        antragBuilder.setStatus(Status.ABGESCHLOSSEN)
                .build();
        antragBuilder.setStatus(Status.ABGELEHNT_NICHT_FOERDERFAEHIG)
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("status", Status.EINGEGANGEN.name() + "," + Status.ABGESCHLOSSEN.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].status", is(Status.EINGEGANGEN.name())))
                .andExpect(jsonPath("$.content[1].status", is(Status.EINGEGANGEN.name())))
                .andExpect(jsonPath("$.content[2].status", is(Status.ABGESCHLOSSEN.name())));
    }

    @Test
    void testFilterByBezirksausschussNr() throws Exception {
        antragBuilder.setBezirksausschussNr(1)
                .build();
        antragBuilder.setBezirksausschussNr(1)
                .build();
        antragBuilder.setBezirksausschussNr(25)
                .build();
        antragBuilder.setBezirksausschussNr(7)
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("bezirksausschussNr", "1,25")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].bezirksausschussNr", is(1)))
                .andExpect(jsonPath("$.content[1].bezirksausschussNr", is(1)))
                .andExpect(jsonPath("$.content[2].bezirksausschussNr", is(25)));
    }

    @Test
    void testFilterDatumVonBisBounds() throws Exception {
        antragBuilder.setEingangDatum(LocalDateTime.of(2009, 12, 31, 1, 0))
                .build();
        antragBuilder.setEingangDatum(LocalDateTime.of(2009, 12, 31, 1, 1))
                .build();
        antragBuilder.setEingangDatum(LocalDateTime.of(2010, 1, 1, 0, 0))
                .build();
        antragBuilder.setEingangDatum(LocalDateTime.of(2010, 1, 2, 0, 0))
                .build();
        antragBuilder.setEingangDatum(LocalDateTime.of(2010, 1, 2, 0, 1))
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("eingangDatumVon", "2009-12-31T01:01:00")
                        .param("eingangDatumBis", "2010-01-02T00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].eingangDatum", is("2009-12-31T01:01:00")))
                .andExpect(jsonPath("$.content[1].eingangDatum", is("2010-01-01T00:00:00")))
                .andExpect(jsonPath("$.content[2].eingangDatum", is("2010-01-02T00:00:00")));
    }

    @Test
    void testFilterByAntragstellerName() throws Exception {
        antragBuilder.setAntragstellerName("Max Mustermann 0")
                .build();
        antragBuilder.setAntragstellerName("Max Mustermann 0")
                .build();
        antragBuilder.setAntragstellerName("Max Mustermann 9")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("antragstellerName", "Max Mustermann 0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].antragstellerName", is("Max Mustermann 0")))
                .andExpect(jsonPath("$.content[1].antragstellerName", is("Max Mustermann 0")));
    }

    @Test
    void testFilterByProjektTitel() throws Exception {
        antragBuilder.setProjektTitel("Projekt XYZ 0")
                .build();
        antragBuilder.setProjektTitel("Projekt XYZ 0")
                .build();
        antragBuilder.setProjektTitel("Projekt XYZ 9")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("projektTitel", "Projekt XYZ 0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].projektTitel", is("Projekt XYZ 0")))
                .andExpect(jsonPath("$.content[1].projektTitel", is("Projekt XYZ 0")));
    }

    @Test
    void testFilterByBeantragtesBudgetVonBis() throws Exception {
        antragBuilder.setBeantragtesBudget(BigDecimal.valueOf(999))
                .build();
        antragBuilder.setBeantragtesBudget(BigDecimal.valueOf(1000))
                .build();
        antragBuilder.setBeantragtesBudget(BigDecimal.valueOf(3000))
                .build();
        antragBuilder.setBeantragtesBudget(BigDecimal.valueOf(3000))
                .build();
        antragBuilder.setBeantragtesBudget(BigDecimal.valueOf(5000))
                .build();
        antragBuilder.setBeantragtesBudget(BigDecimal.valueOf(5001))
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("beantragtesBudgetVon", "1000")
                        .param("beantragtesBudgetBis", "5000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].beantragtesBudget", is(1000)))
                .andExpect(jsonPath("$.content[1].beantragtesBudget", is(3000)))
                .andExpect(jsonPath("$.content[2].beantragtesBudget", is(3000)))
                .andExpect(jsonPath("$.content[3].beantragtesBudget", is(5000)));
    }

    @Test
    void testFilterByIstFehlbetrag() throws Exception {
        antragBuilder.setIstFehlbetrag(true)
                .build();
        antragBuilder.setIstFehlbetrag(true)
                .build();
        antragBuilder.setIstFehlbetrag(false)
                .build();
        antragBuilder.setIstFehlbetrag(false)
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("istFehlbetrag", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));

        mockMvc
                .perform(get("/antrag")
                        .param("istFehlbetrag", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void testFilterByAktualisierungArt() throws Exception {
        antragBuilder.setAktualisierungArt(AktualisierungArt.FACHANWENDUNG)
                .build();
        antragBuilder.setAktualisierungArt(AktualisierungArt.FACHANWENDUNG)
                .build();
        antragBuilder.setAktualisierungArt(AktualisierungArt.ZAMMAD)
                .build();
        antragBuilder.setAktualisierungArt(AktualisierungArt.E_AKTE)
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("aktualisierungArt", AktualisierungArt.FACHANWENDUNG.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void testFilterByStatusAndBezirksausschussNr() throws Exception {
        antragBuilder.setStatus(Status.EINGEGANGEN)
                .setBezirksausschussNr(1)
                .build();
        antragBuilder.setStatus(Status.EINGEGANGEN)
                .setBezirksausschussNr(1)
                .build();
        antragBuilder.setStatus(Status.ABGESCHLOSSEN)
                .setBezirksausschussNr(25)
                .build();
        antragBuilder.setStatus(Status.ABGELEHNT_NICHT_FOERDERFAEHIG)
                .setBezirksausschussNr(7)
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("status", Status.EINGEGANGEN.name())
                        .param("bezirksausschussNr", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].status", is(Status.EINGEGANGEN.name())))
                .andExpect(jsonPath("$.content[0].bezirksausschussNr", is(1)))
                .andExpect(jsonPath("$.content[1].status", is(Status.EINGEGANGEN.name())))
                .andExpect(jsonPath("$.content[1].bezirksausschussNr", is(1)));
    }

    @Test
    void testFilterByStatusAndDatumVonBis() throws Exception {
        antragBuilder.setStatus(Status.EINGEGANGEN)
                .setEingangDatum(LocalDateTime.of(2009, 12, 31, 1, 0))
                .build();
        antragBuilder.setStatus(Status.EINGEGANGEN)
                .setEingangDatum(LocalDateTime.of(2009, 12, 31, 1, 1))
                .build();
        antragBuilder.setStatus(Status.ABGESCHLOSSEN)
                .setEingangDatum(LocalDateTime.of(2010, 12, 1, 0, 0))
                .build();
        antragBuilder.setStatus(Status.ABGELEHNT_NICHT_FOERDERFAEHIG)
                .setEingangDatum(LocalDateTime.of(2010, 1, 2, 0, 0))
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("status", Status.EINGEGANGEN.name())
                        .param("eingangDatumVon", "2009-12-31T01:00:00")
                        .param("eingangDatumBis", "2009-12-31T01:01:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].status", is(Status.EINGEGANGEN.name())))
                .andExpect(jsonPath("$.content[0].eingangDatum", is("2009-12-31T01:00:00")))
                .andExpect(jsonPath("$.content[1].status", is(Status.EINGEGANGEN.name())))
                .andExpect(jsonPath("$.content[1].eingangDatum", is("2009-12-31T01:01:00")));
    }

    @Test
    void testFilterByBezirksausschussNrAndAntragstellerName() throws Exception {
        antragBuilder.setBezirksausschussNr(1)
                .setAntragstellerName("Max Mustermann 0")
                .build();
        antragBuilder.setBezirksausschussNr(1)
                .setAntragstellerName("Max Mustermann 0")
                .build();
        antragBuilder.setBezirksausschussNr(25)
                .setAntragstellerName("Max Mustermann 9")
                .build();
        antragBuilder.setBezirksausschussNr(7)
                .setAntragstellerName("Max Mustermann 0")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("bezirksausschussNr", "1")
                        .param("antragstellerName", "Max Mustermann 0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].bezirksausschussNr", is(1)))
                .andExpect(jsonPath("$.content[0].antragstellerName", is("Max Mustermann 0")))
                .andExpect(jsonPath("$.content[1].bezirksausschussNr", is(1)))
                .andExpect(jsonPath("$.content[1].antragstellerName", is("Max Mustermann 0")));
    }

    @Test
    void testFilterByProjektTitelAndBeantragtesBudgetVonBis() throws Exception {
        antragBuilder.setProjektTitel("Projekt XYZ 0")
                .setBeantragtesBudget(BigDecimal.valueOf(999))
                .build();
        antragBuilder.setProjektTitel("Projekt XYZ 0")
                .setBeantragtesBudget(BigDecimal.valueOf(1000))
                .build();
        antragBuilder.setProjektTitel("Projekt XYZ 0")
                .setBeantragtesBudget(BigDecimal.valueOf(3000))
                .build();
        antragBuilder.setProjektTitel("Projekt XYZ 9")
                .setBeantragtesBudget(BigDecimal.valueOf(3000))
                .build();
        antragBuilder.setProjektTitel("Projekt XYZ 0")
                .setBeantragtesBudget(BigDecimal.valueOf(5000))
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("projektTitel", "Projekt XYZ 0")
                        .param("beantragtesBudgetVon", "1000")
                        .param("beantragtesBudgetBis", "5000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].projektTitel", is("Projekt XYZ 0")))
                .andExpect(jsonPath("$.content[0].beantragtesBudget", is(1000)))
                .andExpect(jsonPath("$.content[1].projektTitel", is("Projekt XYZ 0")))
                .andExpect(jsonPath("$.content[1].beantragtesBudget", is(3000)))
                .andExpect(jsonPath("$.content[2].projektTitel", is("Projekt XYZ 0")))
                .andExpect(jsonPath("$.content[2].beantragtesBudget", is(5000)));
    }

    @Test
    void testFilterByIstFehlbetragAndAktualisierungArt() throws Exception {
        antragBuilder.setIstFehlbetrag(true)
                .setAktualisierungArt(AktualisierungArt.FACHANWENDUNG)
                .build();
        antragBuilder.setIstFehlbetrag(true)
                .setAktualisierungArt(AktualisierungArt.FACHANWENDUNG)
                .build();
        antragBuilder.setIstFehlbetrag(false)
                .setAktualisierungArt(AktualisierungArt.ZAMMAD)
                .build();
        antragBuilder.setIstFehlbetrag(false)
                .setAktualisierungArt(AktualisierungArt.E_AKTE)
                .build();
        entityManager.flush();
        entityManager.clear();

        mockMvc
                .perform(get("/antrag")
                        .param("istFehlbetrag", "true")
                        .param("aktualisierungArt", AktualisierungArt.FACHANWENDUNG.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].istFehlbetrag", is(true)))
                .andExpect(jsonPath("$.content[0].aktualisierung", is(AktualisierungArt.FACHANWENDUNG.name())))
                .andExpect(jsonPath("$.content[1].istFehlbetrag", is(true)))
                .andExpect(jsonPath("$.content[1].aktualisierung", is(AktualisierungArt.FACHANWENDUNG.name())));
    }

    @Test
    void testFilterWithEmptyListsReturnsDefaultPage() throws Exception {
        antragBuilder.build();
        antragBuilder.build();
        antragBuilder.build();

        mockMvc
                .perform(get("/antrag")
                        .param("status", "")
                        .param("bezirksausschussNr", "")
                        .param("aktualisierungArt", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void testMalformedDateFormatInEingangDatumVon() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("eingangDatumVon", "2009-12-3100:01:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidEnumValueInStatusFilter() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("status", "INVALID_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidEnumValueInAktualisierungArtFilter() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("aktualisierungArt", "INVALID_ART")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
