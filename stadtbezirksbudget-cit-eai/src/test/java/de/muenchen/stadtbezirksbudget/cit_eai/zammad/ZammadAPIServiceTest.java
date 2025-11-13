package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.api.TicketsApi;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateUserAndTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.TicketInternal;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.UserAndTicketResponseDTO;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.service.ZammadAPIService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

class ZammadAPIServiceTest {
    private TicketsApi ticketsApi;
    private ZammadAPIService service;

    @BeforeEach
    void setUp() {
        ticketsApi = Mockito.mock(TicketsApi.class);
        service = new ZammadAPIService(ticketsApi);
    }

    @Nested
    class CreateTicket {
        @Test
        void testCreateTicketReturnsCorrectTicket() {
            final CreateTicketDTOV2 dto = new CreateTicketDTOV2()
                    .title("Test")
                    .anliegenart("art")
                    .vertrauensniveau("1")
                    .group("group");

            final TicketInternal ticket = new TicketInternal().id("42").title("Test");

            Mockito.when(ticketsApi.createNewTicket(any(CreateTicketDTOV2.class), eq("ext-1"), eq(null), any()))
                    .thenReturn(Mono.just(ticket));

            final TicketInternal result = service.createTicket(dto, "ext-1", null, Collections.emptyList());

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo("42");
            assertThat(result).isSameAs(ticket);
        }

        @Test
        void testCreateTicketWithMissingIdsThrowsIllegalArgumentException() {
            final CreateTicketDTOV2 dto = new CreateTicketDTOV2().title("T").anliegenart("a").vertrauensniveau("1").group("g");

            assertThrows(IllegalArgumentException.class, () -> service.createTicket(dto, null, null, Collections.emptyList()));
        }

        @Test
        void testCreateTicketWithApiErrorThrowsZammadEAIException() {
            final CreateTicketDTOV2 dto = new CreateTicketDTOV2().title("T").anliegenart("a").vertrauensniveau("1").group("g");

            Mockito.when(ticketsApi.createNewTicket(any(CreateTicketDTOV2.class), eq("ext-1"), eq(null), any()))
                    .thenReturn(Mono.error(new WebClientResponseException("fail", 500, "ERR", null, null, null)));

            assertThrows(ZammadEAIException.class, () -> service.createTicket(dto, "ext-1", null, Collections.emptyList()));
        }
    }

    @Nested
    class CreateUserAndTicket{
        @Test
        void testCreateUserAndTicketReturnsResponse() {
            final CreateUserAndTicketDTOV2 dto = new CreateUserAndTicketDTOV2();
            dto.setCreateTicketDTO(new CreateTicketDTOV2().title("T").anliegenart("a").vertrauensniveau("1").group("g"));

            final UserAndTicketResponseDTO resp = new UserAndTicketResponseDTO();

            Mockito.when(ticketsApi.createNewTicketWithUser(any(CreateUserAndTicketDTOV2.class), any()))
                    .thenReturn(Mono.just(resp));

            final UserAndTicketResponseDTO result = service.createUserAndTicket(dto, Collections.emptyList());

            assertThat(result).isNotNull();
            assertThat(result).isSameAs(resp);
        }
    }
}
