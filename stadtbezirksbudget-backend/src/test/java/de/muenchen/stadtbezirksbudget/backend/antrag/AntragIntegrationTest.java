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
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AdresseRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BankverbindungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BearbeitungsstandRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ProjektRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ZahlungsempfaengerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    private AntragTestDataBuilder antragTestDataBuilder;

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
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        antragTestDataBuilder = new AntragTestDataBuilder(antragRepository, adresseRepository,
                finanzierungRepository, antragstellerRepository, projektRepository, bearbeitungsstandRepository, bankverbindungRepository);
        for (int i = 0; i < 100; i++) {
            antragList.add(antragTestDataBuilder.initializeAntrag());
        }
    }

    @AfterEach
    public void tearDown() {
        antragRepository.deleteAll();
        antragList.clear();
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
        @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
        void testGivenSortAscThenReturnSortedResults() throws Exception {
            antragRepository.deleteAll();
            antragList.clear();
            for (int i = 0; i < 3; i++) {
                final Antrag antrag = antragTestDataBuilder.initializeAntrag();
                antrag.getBearbeitungsstand().setStatus(Status.VOLLSTAENDIG);
                if (i == 1) {
                    antrag.getBearbeitungsstand().setStatus(Status.EINGEGANGEN);
                }
                antragList.add(antrag);
                antragRepository.save(antrag);
                bearbeitungsstandRepository.save(antrag.getBearbeitungsstand());
            }
            mockMvc
                    .perform(get("/antrag")
                            .param("sortBy", "status")
                            .param("sortDirection", "ASC")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content[0].id", is(antragList.get(1).getId().toString())));
        }

        @Test
        void testGivenSortDescThenReturnSortedResults() throws Exception {
            antragRepository.deleteAll();
            antragList.clear();
            for (int i = 0; i < 3; i++) {
                final Antrag antrag = antragTestDataBuilder.initializeAntrag();
                antrag.setZammadTicketNr(String.valueOf(i));
                antragList.add(antrag);
                antragRepository.save(antrag);
            }
            mockMvc
                    .perform(get("/antrag")
                            .param("sortBy", "zammadNr")
                            .param("sortDirection", "DESC")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content[0].id", is(antragList.get(2).getId().toString())));
        }

        @Test
        void testGivenSortUnpagedThenReturnSortedResults() throws Exception {
            antragRepository.deleteAll();
            antragList.clear();
            for (int i = 0; i < 3; i++) {
                final Antrag antrag = antragTestDataBuilder.initializeAntrag();
                antrag.getProjekt().setTitel(String.valueOf(i));
                antragList.add(antrag);
                antragRepository.save(antrag);
                projektRepository.save(antrag.getProjekt());
            }
            mockMvc
                    .perform(get("/antrag")
                            .param("size", "-1")
                            .param("sortBy", "projektTitel")
                            .param("sortDirection", "ASC")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content[0].id", is(antragList.getFirst().getId().toString())));
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
