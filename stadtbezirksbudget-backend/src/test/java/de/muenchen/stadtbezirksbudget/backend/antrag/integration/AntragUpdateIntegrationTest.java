package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
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
class AntragUpdateIntegrationTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);

    private final List<Antrag> antragList = new ArrayList<>();

    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragList.clear();
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository);
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
