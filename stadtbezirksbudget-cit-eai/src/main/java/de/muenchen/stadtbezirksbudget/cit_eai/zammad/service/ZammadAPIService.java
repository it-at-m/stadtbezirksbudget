package de.muenchen.stadtbezirksbudget.cit_eai.zammad.service;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.ZammadEAIException;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.api.TicketsApi;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateUserAndTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.TicketInternal;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.UserAndTicketResponseDTO;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Service that creates tickets in Zammad using the generated {@link TicketsApi} client.
 * This service performs lightweight validation of the provided DTO.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZammadAPIService {

    private final TicketsApi ticketsApi;

    /**
     * Creates a ticket in Zammad. Either {@code lhmextid} or {@code userid} must be provided.
     * <p>
     * Required fields in {@code createTicketDTOV2} are: {@code title}, {@code anliegenart},
     * {@code vertrauensniveau}, {@code group}.
     *
     * @param createTicketDTOV2 DTO carrying the ticket details; required fields: title, anliegenart,
     *            vertrauensniveau
     * @param lhmextid External identifier of the ticket creator, may be {@code null}
     * @param userid Internal user id of the ticket creator, may be {@code null}
     * @param attachments List of attachments to add to the ticket, maybe empty
     * @return The created {@link TicketInternal}
     * @throws IllegalArgumentException If both {@code lhmextid} and {@code userid} are {@code null} or
     *             if required fields in {@code createTicketDTOV2} are missing
     * @throws ZammadEAIException If the remote Zammad API responds with an error
     */
    public Mono<TicketInternal> createTicket(final CreateTicketDTOV2 createTicketDTOV2, @Nullable final String lhmextid, @Nullable final String userid,
            final List<AbstractResource> attachments) {
        validateCreateTicketDTO(createTicketDTOV2);

        if (lhmextid == null && userid == null) {
            return Mono.error(new IllegalArgumentException("Either lhmextid or userid must be provided"));
        }

        log.info("Attempting to create ticket in Zammad with title: {}", createTicketDTOV2.getTitle());

        return ticketsApi.createNewTicket(createTicketDTOV2, lhmextid, userid, attachments)
                .doOnSuccess(ticket -> {
                    log.info("Successfully created ticket in Zammad with ID: {}", ticket.getId());
                })
                .onErrorMap(WebClientResponseException.class, e -> new ZammadEAIException(e, "Failed to create ticket in Zammad"));
    }

    /**
     * Creates a ticket and a user in zammad.
     * <p>
     * Required fields in {@link CreateUserAndTicketDTOV2} are: {@code createTicketDTO.title},
     * {@code createTicketDTO.anliegenart},
     * {@code createTicketDTO.vertrauensniveau}, {@code createTicketDTO.group}.
     *
     * @param createUserAndTicketDTOV2 DTO carrying the ticket and user details
     * @param attachments List of attachments to add to the ticket, maybe empty
     * @return The created ticket and user as {@link UserAndTicketResponseDTO}
     * @throws NullPointerException If createUserAndTicketDTOV2 or attachment list is null
     * @throws ZammadEAIException If request failed
     */
    public Mono<UserAndTicketResponseDTO> createUserAndTicket(final CreateUserAndTicketDTOV2 createUserAndTicketDTOV2,
            final List<AbstractResource> attachments) {
        Objects.requireNonNull(createUserAndTicketDTOV2);
        Objects.requireNonNull(attachments);
        validateCreateTicketDTO(createUserAndTicketDTOV2.getCreateTicketDTO());

        log.info("Attempting to create ticket and user in Zammad");
        return ticketsApi.createNewTicketWithUser(createUserAndTicketDTOV2, attachments)
                .switchIfEmpty(Mono.error(new ZammadEAIException("Unable to create ticket and user")))
                .onErrorMap(WebClientResponseException.class, e -> new ZammadEAIException(e, "Failed to create ticket and user"));
    }

    private void validateCreateTicketDTO(final CreateTicketDTOV2 createTicketDTOV2) {
        Objects.requireNonNull(createTicketDTOV2);
        requireNonBlank(createTicketDTOV2.getTitle(), "title");
        requireNonBlank(createTicketDTOV2.getAnliegenart(), "anliegenart");
        requireNonBlank(createTicketDTOV2.getVertrauensniveau(), "vertrauensniveau");
        requireNonBlank(createTicketDTOV2.getGroup(), "group");
    }

    private void requireNonBlank(final String value, final String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ticket " + fieldName + " must not be null or blank");
        }
    }
}
