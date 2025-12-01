package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.api.TicketsApi;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateUserAndTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.TicketInternal;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.UserAndTicketResponseDTO;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.AbstractResource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

class ZammadAPIServiceTest {
    private TicketsApi ticketsApi;
    private ZammadAPIService service;

    private static final String EXTERNAL_ID = "ext-1";

    private final AbstractResource resource = new AbstractResource() {
        @NonNull @Override
        public String getDescription() {
            return "test abstract resource";
        }

        @NonNull @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream("hello".getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String getFilename() {
            return "test.txt";
        }
    };

    @BeforeEach
    void setUp() {
        ticketsApi = Mockito.mock(TicketsApi.class);
        service = new ZammadAPIService(ticketsApi);
    }

    @Nested
    class CreateTicket {

        private CreateTicketDTOV2 generateCreateTicketDTOV2() {
            return new CreateTicketDTOV2()
                    .title("T")
                    .anliegenart("a")
                    .vertrauensniveau("1")
                    .group("g");
        }

        @Test
        void testCreateTicketReturnsCorrectTicket() {
            final CreateTicketDTOV2 dto = generateCreateTicketDTOV2();

            final TicketInternal ticket = new TicketInternal().id("42").title("Test");

            Mockito.when(ticketsApi.createNewTicket(any(CreateTicketDTOV2.class), eq(EXTERNAL_ID), eq(null), any()))
                    .thenReturn(Mono.just(ticket));

            final TicketInternal result = service.createTicket(dto, EXTERNAL_ID, null, Collections.emptyList()).block();

            assertThat(result).isNotNull();
            assertThat(result).isSameAs(ticket);
        }

        @Test
        void testCreateTicketWithIdReturnsCorrectTicket() {
            final CreateTicketDTOV2 dto = generateCreateTicketDTOV2();

            final TicketInternal ticket = new TicketInternal().id("42").title("T");

            Mockito.when(ticketsApi.createNewTicket(any(CreateTicketDTOV2.class), eq(null), eq("user-1"), any()))
                    .thenReturn(Mono.just(ticket));

            final TicketInternal result = service.createTicket(dto, null, "user-1", Collections.emptyList()).block();

            assertThat(result).isNotNull();
            assertThat(result).isSameAs(ticket);
        }

        @Test
        void testCreateTicketWithAttachmentsReturnsCorrectResponse() {
            final CreateTicketDTOV2 dto = generateCreateTicketDTOV2();

            final TicketInternal ticket = new TicketInternal().id("42").title("T");

            Mockito.when(ticketsApi.createNewTicket(any(CreateTicketDTOV2.class), eq(EXTERNAL_ID), eq(null), eq(List.of(resource))))
                    .thenReturn(Mono.just(ticket));

            final TicketInternal result = service.createTicket(dto, EXTERNAL_ID, null, List.of(resource)).block();

            assertThat(result).isNotNull();
            assertThat(result).isSameAs(ticket);

            Mockito.verify(ticketsApi).createNewTicket(
                    any(CreateTicketDTOV2.class),
                    eq(EXTERNAL_ID),
                    eq(null),
                    argThat((List<AbstractResource> list) -> list != null && list.equals(List.of(resource))));
        }

        @Test
        void testCreateTicketWithAPIExceptionThrowsZammadAPIException() {
            final CreateTicketDTOV2 dto = generateCreateTicketDTOV2();

            Mockito.when(ticketsApi.createNewTicket(any(CreateTicketDTOV2.class), eq(EXTERNAL_ID), eq(null), any()))
                    .thenReturn(Mono.error(new WebClientResponseException("fail", 500, "ERR", null, null, null)));

            final ZammadAPIException exception = assertThrows(ZammadAPIException.class,
                    () -> service.createTicket(dto, EXTERNAL_ID, null, Collections.emptyList()).block());
            assertThat(exception.getStatusCode()).isEqualTo(500);
        }

        @Test
        void testCreateTicketWithMissingIdsThrowsIllegalArgumentException() {
            final CreateTicketDTOV2 dto = generateCreateTicketDTOV2();

            assertThrows(IllegalArgumentException.class, () -> service.createTicket(dto, null, null, Collections.emptyList()).block());
        }

        @Test
        void testCreateTicketWithNullDtoThrowsNullPointerException() {
            assertThrows(NullPointerException.class, () -> service.createTicket(null, EXTERNAL_ID, null, Collections.emptyList()).block());
        }

        @Test
        void testCreateTicketWithNullAttachmentsThrowsNullPointerException() {
            final CreateTicketDTOV2 dto = generateCreateTicketDTOV2();
            assertThrows(NullPointerException.class, () -> service.createTicket(dto, EXTERNAL_ID, null, null).block());
        }

    }

    @Nested
    class CreateUserAndTicket {

        private CreateUserAndTicketDTOV2 generateCreateUserAndTicketDTOV2() {
            final CreateUserAndTicketDTOV2 dto = new CreateUserAndTicketDTOV2();
            dto.setCreateTicketDTO(new CreateTicketDTOV2()
                    .title("T")
                    .anliegenart("a")
                    .vertrauensniveau("1")
                    .group("g"));
            return dto;
        }

        @Test
        void testCreateUserAndTicketReturnsResponse() {
            final CreateUserAndTicketDTOV2 dto = generateCreateUserAndTicketDTOV2();

            final UserAndTicketResponseDTO resp = new UserAndTicketResponseDTO();
            final TicketInternal ticket = new TicketInternal().id("42").title("T");
            resp.setTicket(ticket);

            Mockito.when(ticketsApi.createNewTicketWithUser(any(CreateUserAndTicketDTOV2.class), any()))
                    .thenReturn(Mono.just(resp));

            final UserAndTicketResponseDTO result = service.createUserAndTicket(dto, Collections.emptyList()).block();

            assertThat(result).isNotNull();
            assertThat(result).isSameAs(resp);
        }

        @Test
        void testCreateUserAndTicketWithMissingRequiredFieldsThrowsIllegalArgumentException() {
            final CreateUserAndTicketDTOV2 dto = generateCreateUserAndTicketDTOV2();
            dto.setCreateTicketDTO(new CreateTicketDTOV2());

            assertThrows(IllegalArgumentException.class, () -> service.createUserAndTicket(dto, Collections.emptyList()).block());
        }

        @Test
        void testCreateUserAndTicketWithNullDtoThrowsNullPointerException() {
            assertThrows(NullPointerException.class, () -> service.createUserAndTicket(null, Collections.emptyList()).block());
        }

        @Test
        void testCreateUserAndTicketWithNullAttachmentsThrowsNullPointerException() {
            final CreateUserAndTicketDTOV2 dto = new CreateUserAndTicketDTOV2();
            assertThrows(NullPointerException.class, () -> service.createUserAndTicket(dto, null).block());
        }

        @Test
        void testCreateUserAndTicketWithNullCreateTicketDTOThrowsIllegalArgumentException() {
            final CreateUserAndTicketDTOV2 dto = new CreateUserAndTicketDTOV2();
            dto.setCreateTicketDTO(null);

            assertThrows(IllegalArgumentException.class, () -> service.createUserAndTicket(dto, Collections.emptyList()).block());
        }

        @Test
        void testCreateUserAndTicketWithAttachmentsReturnsResponse() {
            final CreateUserAndTicketDTOV2 dto = generateCreateUserAndTicketDTOV2();

            final UserAndTicketResponseDTO resp = new UserAndTicketResponseDTO();
            Mockito.when(ticketsApi.createNewTicketWithUser(any(CreateUserAndTicketDTOV2.class), eq(List.of(resource))))
                    .thenReturn(Mono.just(resp));

            final UserAndTicketResponseDTO result = service.createUserAndTicket(dto, List.of(resource)).block();

            assertThat(result).isNotNull();
            assertThat(result).isSameAs(resp);

            Mockito.verify(ticketsApi).createNewTicketWithUser(
                    any(CreateUserAndTicketDTOV2.class),
                    argThat((List<AbstractResource> list) -> list != null && list.equals(List.of(resource))));
        }

        @Test
        void testCreateUserAndTicketWithApiErrorThrowsZammadAPIException() {
            final CreateUserAndTicketDTOV2 dto = generateCreateUserAndTicketDTOV2();

            Mockito.when(ticketsApi.createNewTicketWithUser(any(CreateUserAndTicketDTOV2.class), any()))
                    .thenReturn(Mono.error(new WebClientResponseException("fail", 500, "ERR", null, null, null)));

            final ZammadAPIException exception = assertThrows(ZammadAPIException.class,
                    () -> service.createUserAndTicket(dto, Collections.emptyList()).block());
            assertThat(exception.getStatusCode()).isEqualTo(500);
        }
    }
}
