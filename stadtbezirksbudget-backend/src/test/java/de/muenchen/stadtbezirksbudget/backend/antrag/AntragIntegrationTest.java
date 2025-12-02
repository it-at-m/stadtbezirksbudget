package de.muenchen.stadtbezirksbudget.backend.antrag;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AdresseRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BankverbindungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BearbeitungsstandRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ProjektRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ZahlungsempfaengerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
class AntragIntegrationTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);

    private final List<Antrag> antragList = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private AdresseRepository adresseRepository;
    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private ZahlungsempfaengerRepository antragstellerRepository;
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
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        final AntragTestDataBuilder antragTestDataBuilder = new AntragTestDataBuilder(antragRepository, adresseRepository,
                finanzierungRepository, antragstellerRepository, projektRepository, bearbeitungsstandRepository, bankverbindungRepository,
                finanzierungsmittelRepository, voraussichtlicheAusgabeRepository);
        antragList.addAll(antragTestDataBuilder.initializeAntragList(100));
        entityManager.flush();
        entityManager.clear();
    }

    @Nested
    class GetAntragSummaryPage {

        @Test
        void testGivenPageAndSizeThenReturnPageOfAntragsdaten() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "3")
                            .param("size", "20")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(20)))
                    .andExpect(jsonPath("$.page.totalElements", is(100)))
                    .andExpect(jsonPath("$.page.size", is(20)))
                    .andExpect(jsonPath("$.page.number", is(3)))
                    .andExpect(jsonPath("$.page.totalPages", is(5)));
        }

        @Test
        void testGivenNoParamsThenReturnDefaultPage() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.page.totalElements", is(100)))
                    .andExpect(jsonPath("$.page.size", is(10)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(10)));
        }

        @Test
        void testGivenEmptyDatabaseThenReturnEmptyPage() throws Exception {
            antragRepository.deleteAll();

            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.page.totalElements", is(0)))
                    .andExpect(jsonPath("$.page.size", is(10)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(0)));
        }

        @Test
        void testGivenAllSizeThenReturnUnpaged() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("size", "-1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(100)))
                    .andExpect(jsonPath("$.page.totalElements", is(100)))
                    .andExpect(jsonPath("$.page.size", is(100)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(1)));
        }

        @Test
        void testFilterByStatus() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("status", Status.EINGEGANGEN.name())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // 18 status values, i = 100, EINGEGANGEN is the first value in the enum -> ceil(100/18)=6
                    .andExpect(jsonPath("$.content", hasSize(6)))
                    .andExpect(jsonPath("$.content[0].status", is(Status.EINGEGANGEN.name())));
        }

        @Test
        void testFilterByMultipleStatus() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "20")
                            .param("status", Status.ABGELEHNT_NICHT_FOERDERFAEHIG.name() + "," + Status.ABGELEHNT_NICHT_ZUSTAENDIG.name())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // 18 status values, i = 100, ABGELEHNT-values are the last ones in the enum -> 2*floor(100/18)=10
                    .andExpect(jsonPath("$.content", hasSize(10)));
        }

        @Test
        void testFilterByBezirksausschussNr() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("bezirksausschussNr", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // 25 district committee numbers, i = 100 -> 100/25=4
                    .andExpect(jsonPath("$.content", hasSize(4)))
                    .andExpect(jsonPath("$.content[0].bezirksausschussNr", is(1)));
        }

        @Test
        void testFilterByMultipleBezirksausschussNr() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("bezirksausschussNr", "16, 25")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // 25 district committee numbers, i = 100 -> 2*100/25=8
                    .andExpect(jsonPath("$.content", hasSize(8)));
        }

        @Test
        void testFilterByEingangDatumVonBisBounds() throws Exception {
            // AktualisierungsDaten are identical to Eingangsdaten (including implementation), which is why it is not explicitly tested again (avoiding redundant code).
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("eingangDatumVon", "2009-12-31T00:01:01")
                            .param("eingangDatumBis", "2010-01-10T00:00:00")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Exactly the boundaries of two dates
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].eingangDatum", is("2010-01-01T00:00:00")));
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("eingangDatumVon", "2009-12-30T02:02:00")
                            .param("eingangDatumBis", "2009-12-31T00:01:00")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Only 1 item in this time period: eingangDatumVon-boundary
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].eingangDatum", is("2009-12-30T02:02:00")));
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("eingangDatumVon", "2009-12-31T01:02:00")
                            .param("eingangDatumBis", "2010-01-01T00:00:00")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Only 1 item in this time period: eingangDatumBis-boundary
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].eingangDatum", is("2010-01-01T00:00:00")));
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("eingangDatumVon", "2009-12-30T02:03:00")
                            .param("eingangDatumBis", "2009-12-31T00:01:00")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // No data in the specified period
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }

        @Test
        void testFilterByFirstAntragstellerName() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "20")
                            .param("antragstellerName", "Max Mustermann 0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Antragsteller-name repeats every 10 names -> 100/10=10
                    .andExpect(jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.content[0].antragstellerName", is("Max Mustermann 0")));
        }

        @Test
        void testFilterByLastAntragstellerName() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "20")
                            .param("antragstellerName", "Max Mustermann 9")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Antragsteller-name repeats every 10 names -> 100/10=10
                    .andExpect(jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.content[0].antragstellerName", is("Max Mustermann 9")));
        }

        @Test
        void testFilterByProjektTitel() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("projektTitel", "Projekt XYZ 0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Project title repeats every 10 names -> 100/10=10
                    .andExpect(jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.content[0].projektTitel", is("Projekt XYZ 0")));
        }

        @Test
        void testFilterByBeantragtesBudgetVonBis() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("beantragtesBudgetVon", "1000")
                            .param("beantragtesBudgetBis", "5000")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Values of requested budget: 1000, 2000, ..., 10_0000, i.e. 5 elements up to 5000.
                    .andExpect(jsonPath("$.content", hasSize(5)));
        }

        @Test
        void testFilterByIstFehlbetrag() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "100")
                            .param("istFehlbetrag", "true")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // Exactly half of the elments have istFehlbetrag=true
                    .andExpect(jsonPath("$.content", hasSize(50)));
        }

        @Test
        void testFilterByAktualisierungArt() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "40")
                            .param("aktualisierungArt", AktualisierungArt.FACHANWENDUNG.name())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    // 3 Aktualisierungsdaten, i = 100 -> 100/3=33 remainder 1, because FACHANWENDUNG is first value -> 34
                    .andExpect(jsonPath("$.content", hasSize(34)))
                    .andExpect(jsonPath("$.content[0].aktualisierung", is(AktualisierungArt.FACHANWENDUNG.name())));
        }

        @Test
        void testFilterWithAllOptions() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .param("status", Status.EINGEGANGEN.name())
                            .param("bezirksausschussNr", "1")
                            .param("eingangDatumVon", "2010-01-01T00:00:00")
                            .param("eingangDatumBis", "2010-01-10T23:59:59")
                            .param("antragstellerName", "Max Mustermann 0")
                            .param("projektTitel", "Projekt XYZ 0")
                            .param("beantragtesBudgetVon", "1000")
                            .param("beantragtesBudgetBis", "5000")
                            .param("istFehlbetrag", "true")
                            .param("aktualisierungArt", AktualisierungArt.FACHANWENDUNG.name())
                            .param("aktualisierungDatumVon", "2010-01-01T00:00:00")
                            .param("aktualisierungDatumBis", "2010-01-10T23:59:59")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(1)));
        }

        @Test
        void testFilterWithHalfOptionsSet1() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "100")
                            .param("status", Status.EINGEGANGEN.name() + "," + Status.ABGESCHLOSSEN.name())
                            .param("bezirksausschussNr", "1,11,22")
                            .param("eingangDatumVon", "2009-01-01T00:00:00")
                            .param("eingangDatumBis", "2010-01-10T23:59:59")
                            .param("antragstellerName", "Max Mustermann 0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)));
        }

        @Test
        void testFilterWithHalfOptionsSet2() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "100")
                            .param("projektTitel", "Projekt XYZ 0")
                            .param("beantragtesBudgetVon", "1000")
                            .param("beantragtesBudgetBis", "90000")
                            .param("istFehlbetrag", "true")
                            .param("aktualisierungArt", AktualisierungArt.FACHANWENDUNG.name() + "," + AktualisierungArt.ZAMMAD.name())
                            .param("aktualisierungDatumVon", "2009-01-01T00:00:00")
                            .param("aktualisierungDatumBis", "2010-01-10T23:59:59")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(6)));
        }

        @Test
        void testFilterWithEmptyListsReturnsDefaultPage() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "100")
                            .param("status", "")
                            .param("bezirksausschussNr", "")
                            .param("aktualisierungArt", "")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(100)));
        }
    }

    @Nested
    class GetFilterOptions {

        @Test
        void testGetFilterOptionsReturnsAlphabeticallySortedLists() throws Exception {
            mockMvc
                    .perform(get("/antrag/filterOptions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.antragstellerNamen").isArray())
                    .andExpect(jsonPath("$.projektTitel").isArray())
                    .andExpect(jsonPath("$.antragstellerNamen", hasSize(10)))
                    .andExpect(jsonPath("$.projektTitel", hasSize(10)))
                    .andExpect(jsonPath("$.antragstellerNamen[0]", is("Max Mustermann 0")))
                    .andExpect(jsonPath("$.antragstellerNamen[1]", is("Max Mustermann 1")))
                    .andExpect(jsonPath("$.antragstellerNamen[2]", is("Max Mustermann 2")))
                    .andExpect(jsonPath("$.antragstellerNamen[3]", is("Max Mustermann 3")))
                    .andExpect(jsonPath("$.antragstellerNamen[4]", is("Max Mustermann 4")))
                    .andExpect(jsonPath("$.antragstellerNamen[5]", is("Max Mustermann 5")))
                    .andExpect(jsonPath("$.antragstellerNamen[6]", is("Max Mustermann 6")))
                    .andExpect(jsonPath("$.antragstellerNamen[7]", is("Max Mustermann 7")))
                    .andExpect(jsonPath("$.antragstellerNamen[8]", is("Max Mustermann 8")))
                    .andExpect(jsonPath("$.antragstellerNamen[9]", is("Max Mustermann 9")))
                    .andExpect(jsonPath("$.projektTitel[0]", is("Projekt XYZ 0")))
                    .andExpect(jsonPath("$.projektTitel[1]", is("Projekt XYZ 1")))
                    .andExpect(jsonPath("$.projektTitel[2]", is("Projekt XYZ 2")))
                    .andExpect(jsonPath("$.projektTitel[3]", is("Projekt XYZ 3")))
                    .andExpect(jsonPath("$.projektTitel[4]", is("Projekt XYZ 4")))
                    .andExpect(jsonPath("$.projektTitel[5]", is("Projekt XYZ 5")))
                    .andExpect(jsonPath("$.projektTitel[6]", is("Projekt XYZ 6")))
                    .andExpect(jsonPath("$.projektTitel[7]", is("Projekt XYZ 7")))
                    .andExpect(jsonPath("$.projektTitel[8]", is("Projekt XYZ 8")))
                    .andExpect(jsonPath("$.projektTitel[9]", is("Projekt XYZ 9")));
        }
    }

    @Nested
    class UpdateAntragStatus {

        @Test
        void testUpdateAntragStatusSuccessfully() throws Exception {
            final UUID antragId = antragList.getFirst().getId();
            final AntragStatusUpdateDTO dto = new AntragStatusUpdateDTO(Status.AUSZAHLUNG);

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNoContent());

            final Antrag updated = antragRepository.findById(antragId).orElseThrow();
            assertThat(updated.getBearbeitungsstand().getStatus()).isEqualTo(Status.AUSZAHLUNG);
        }

        @Test
        void testUpdateAntragStatusNotFound() throws Exception {
            final UUID randomId = UUID.randomUUID();
            final AntragStatusUpdateDTO dto = new AntragStatusUpdateDTO(Status.AUSZAHLUNG);

            mockMvc
                    .perform(patch("/antrag/" + randomId + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateAntragStatusNoBody() throws Exception {
            final UUID antragId = antragList.getFirst().getId();

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUpdateAntragStatusIdempotency() throws Exception {
            final UUID antragId = antragList.getFirst().getId();
            final AntragStatusUpdateDTO dto = new AntragStatusUpdateDTO(Status.VOLLSTAENDIG);

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNoContent());

            final Antrag firstUpdate = antragRepository.findById(antragId).orElseThrow();
            assertThat(firstUpdate.getBearbeitungsstand().getStatus()).isEqualTo(Status.VOLLSTAENDIG);

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNoContent());

            final Antrag secondUpdate = antragRepository.findById(antragId).orElseThrow();
            assertThat(secondUpdate.getBearbeitungsstand().getStatus()).isEqualTo(Status.VOLLSTAENDIG);
        }

        @Test
        void testUpdateAntragStatusInvalidStatus() throws Exception {
            final UUID antragId = antragList.getFirst().getId();
            final String invalidDto = "{\"status\":\"INVALID_STATUS\"}";

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidDto))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUpdateAntragStatusNullStatus() throws Exception {
            final UUID antragId = antragList.getFirst().getId();
            final String nullStatusDto = "{\"status\":null}";

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(nullStatusDto))
                    .andExpect(status().isBadRequest());
        }
    }
}
