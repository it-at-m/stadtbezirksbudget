package de.muenchen.stadtbezirksbudget.cit_eai.zammad.service;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.ZammadEAIException;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.api.TicketsApi;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.model.CreateTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.model.TicketInternal;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Service that creates tickets in Zammad using the generated {@link TicketsApi} client.
 * This service performs lightweight validation of the provided DTO.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZammadTicketService {

    private final TicketsApi ticketsApi;

    /**
     * Creates a ticket in Zammad. Either {@code lhmextid} or {@code userid} must be provided.
     * <p>
     * Required fields in {@code createTicketDTOV2} are: {@code title}, {@code anliegenart},
     * {@code vertrauensniveau}, {@code group}.
     *
     * @param createTicketDTOV2 DTO carrying the ticket details; required fields: title, anliegenart,
     *            vertrauensniveau
     * @param lhmextid external identifier of the ticket creator, may be {@code null}
     * @param userid internal user id of the ticket creator, may be {@code null}
     * @param attachments optional list of attachments to add to the ticket
     * @return the created {@link TicketInternal}
     * @throws IllegalArgumentException if both {@code lhmextid} and {@code userid} are {@code null} or
     *             if required fields in {@code createTicketDTOV2} are missing
     * @throws WebClientResponseException if the remote Zammad API responds with an error
     */
    public TicketInternal createTicket(final CreateTicketDTOV2 createTicketDTOV2, @Nullable final String lhmextid, @Nullable final String userid,
            final List<AbstractResource> attachments) {
        validateCreateTicketDTO(createTicketDTOV2);

        if (lhmextid == null && userid == null) {
            throw new IllegalArgumentException("Either lhmextid or userid must be provided");
        }

        log.info("Attempting to create ticket in Zammad with title: {}", createTicketDTOV2.getTitle());

        final TicketInternal ticket;
        try {
            ticket = ticketsApi.createNewTicket(createTicketDTOV2, lhmextid, userid, attachments).block();
        } catch (final WebClientResponseException e) {
            log.error("Error creating ticket in Zammad: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ZammadEAIException(e, "Failed to create ticket in Zammad");
        }
        if (ticket != null) {
            log.info("Ticket created with id {}", ticket.getId());
        }

        return ticket;
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
