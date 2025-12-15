package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

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
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
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
    @Autowired
    private ObjectMapper objectMapper;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragList.clear();
        antragBuilder = new AntragBuilder(antragRepository, adresseRepository,
                finanzierungRepository, antragstellerRepository, projektRepository, bearbeitungsstandRepository, bankverbindungRepository,
                finanzierungsmittelRepository, voraussichtlicheAusgabeRepository);
    }

    @Nested
    class GetAntragSummaryPage {

        @Test
        void testGivenPageAndSizeThenReturnPageOfAntragsdaten() throws Exception {
            for (int i = 0; i < 6; i++) {
                antragBuilder.build();
            }

            mockMvc
                    .perform(get("/antrag")
                            .param("page", "2")
                            .param("size", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.page.totalElements", is(6)))
                    .andExpect(jsonPath("$.page.size", is(2)))
                    .andExpect(jsonPath("$.page.number", is(2)))
                    .andExpect(jsonPath("$.page.totalPages", is(3)));
        }

        @Test
        void testGivenNoParamsThenReturnDefaultPage() throws Exception {
            for (int i = 0; i < 11; i++) {
                antragBuilder.build();
            }

            mockMvc
                    .perform(get("/antrag")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.page.totalElements", is(11)))
                    .andExpect(jsonPath("$.page.size", is(10)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(2)));
        }

        @Test
        void testGivenEmptyDatabaseThenReturnEmptyPage() throws Exception {
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
            for (int i = 0; i < 11; i++) {
                antragBuilder.build();
            }

            mockMvc
                    .perform(get("/antrag")
                            .param("size", "-1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(11)))
                    .andExpect(jsonPath("$.page.totalElements", is(11)))
                    .andExpect(jsonPath("$.page.size", is(11)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(1)));
        }
    }

    @Nested
    class GetFilterOptions {
        @Test
        void testGetFilterOptionsReturnsAlphabeticallySortedLists() throws Exception {
            antragList.add(antragBuilder
                    .antragstellerName("Alex")
                    .projektTitel("Zeltlager")
                    .build());
            antragList.add(antragBuilder
                    .antragstellerName("Alina")
                    .projektTitel("Wochenmarkt")
                    .build());
            antragList.add(antragBuilder
                    .antragstellerName("Musterfrau")
                    .projektTitel("Aktionswoche")
                    .build());
            antragList.add(antragBuilder
                    .antragstellerName("Musterfrau")
                    .projektTitel("Weihnachtsmarkt")
                    .build());
            final List<String> sortedAntragstellerNamen = antragList.stream()
                    .map(Antrag::getAntragsteller)
                    .map(Antragsteller::getName)
                    .sorted()
                    .toList();
            final List<String> sortedProjektTitel = antragList.stream()
                    .map(Antrag::getProjekt)
                    .map(Projekt::getTitel)
                    .sorted()
                    .toList();

            mockMvc
                    .perform(get("/antrag/filterOptions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.antragstellerNamen").isArray())
                    .andExpect(jsonPath("$.projektTitel").isArray())
                    .andExpect(jsonPath("$.antragstellerNamen", hasSize(3)))
                    .andExpect(jsonPath("$.projektTitel", hasSize(4)))
                    .andExpect(jsonPath("$.antragstellerNamen[0]", is(sortedAntragstellerNamen.getFirst())))
                    .andExpect(jsonPath("$.antragstellerNamen[2]", is(sortedAntragstellerNamen.get(2))))
                    .andExpect(jsonPath("$.projektTitel[0]", is(sortedProjektTitel.getFirst())))
                    .andExpect(jsonPath("$.projektTitel[3]", is(sortedProjektTitel.get(3))));
        }
    }

    @Nested
    class UpdateAntragStatus {

        @Test
        void testUpdateAntragStatusSuccessfully() throws Exception {
            antragList.add(antragBuilder
                    .status(Status.EINGEGANGEN)
                    .build());
            antragList.add(antragBuilder
                    .build());
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
            antragList.add(antragBuilder
                    .status(Status.EINGEGANGEN)
                    .build());
            antragList.add(antragBuilder
                    .build());
            final UUID antragId = antragList.getFirst().getId();

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/status")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUpdateAntragStatusIdempotency() throws Exception {
            antragList.add(antragBuilder
                    .status(Status.EINGEGANGEN)
                    .build());
            antragList.add(antragBuilder
                    .build());

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
            antragList.add(antragBuilder
                    .status(Status.EINGEGANGEN)
                    .build());
            antragList.add(antragBuilder
                    .build());

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
            antragList.add(antragBuilder
                    .status(Status.EINGEGANGEN)
                    .build());
            antragList.add(antragBuilder
                    .build());

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
