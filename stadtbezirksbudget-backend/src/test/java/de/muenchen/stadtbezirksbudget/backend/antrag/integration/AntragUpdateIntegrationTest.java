package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragReferenceUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragUpdateIntegrationTest extends AntragBaseIntegrationTest {

    private final List<Antrag> antragList = new ArrayList<>();

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

    @Nested
    class UpdateAntragReference {

        @Test
        void testUpdateReferenceNotFound() throws Exception {
            final UUID randomId = UUID.randomUUID();
            final AntragReferenceUpdateDTO dto = new AntragReferenceUpdateDTO("COO.6804.7915.3.3210877");

            mockMvc
                    .perform(patch("/antrag/" + randomId + "/reference")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateReferenceCooAdresse() throws Exception {
            antragList.add(antragBuilder
                    .eakteCooAdresse("COO.6804.7915.3.3210800")
                    .build());
            final UUID antragId = antragList.getFirst().getId();
            final AntragReferenceUpdateDTO dto = new AntragReferenceUpdateDTO("COO.6804.7915.3.3210877");

            mockMvc
                    .perform(patch("/antrag/" + antragId + "/reference")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNoContent());

            final Antrag updated = antragRepository.findById(antragId).orElseThrow();
            assertThat(updated.getEakteCooAdresse()).isEqualTo("COO.6804.7915.3.3210877");
        }
    }
}
