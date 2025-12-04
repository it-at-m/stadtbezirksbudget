package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.api.TicketsApi;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateUserAndTicketDTOV2;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.TicketInternal;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.UserAndTicketResponseDTO;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.NonNull;
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
     *            vertrauensniveau, group
     * @param lhmextid External identifier of the ticket creator, may be {@code null}
     * @param userid Internal user id of the ticket creator, may be {@code null}
     * @param attachments List of attachments to add to the ticket, maybe empty
     * @return A Mono emitting the created {@link TicketInternal}
     * @throws IllegalArgumentException If both {@code lhmextid} and {@code userid} are {@code null} or
     *             if required fields in {@code createTicketDTOV2} are missing
     * @throws ZammadAPIException If the remote Zammad API responds with an error
     * @throws NullPointerException If {@code createTicketDTOV2} or {@code attachments} is {@code null}
     */
    public Mono<TicketInternal> createTicket(@NonNull final CreateTicketDTOV2 createTicketDTOV2, @Nullable final String lhmextid, @Nullable final String userid,
            @NonNull final List<AbstractResource> attachments) {
        validateCreateTicketDTO(createTicketDTOV2);

        if (lhmextid == null && userid == null) {
            throw new IllegalArgumentException("Either lhmextid or userid must be provided");
        }

        log.info("Attempting to create ticket in Zammad");

        return ticketsApi.createNewTicket(createTicketDTOV2, lhmextid, userid, attachments)
                .switchIfEmpty(Mono.error(new ZammadAPIException("Could not create ticket in Zammad")))
                .flatMap(response -> {
                    if (response == null || response.getId() == null) {
                        return Mono.error(new ZammadAPIException("Ticket or ticket id is null in response"));
                    }
                    return Mono.just(response);
                })
                .doOnSuccess(response -> {
                    log.info("Successfully created ticket in Zammad with ID: {}", response.getId());
                })
                .onErrorMap(WebClientResponseException.class, e -> new ZammadAPIException(e, "Failed to create ticket in Zammad"));
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
     * @return A Mono emitting the created ticket and user as {@link UserAndTicketResponseDTO}
     * @throws NullPointerException If createUserAndTicketDTOV2 or attachment list is null
     * @throws IllegalArgumentException If required fields in
     *             createUserAndTicketDTOV2.getCreateTicketDTO() are null or blank
     * @throws ZammadAPIException If request failed
     */
    public Mono<UserAndTicketResponseDTO> createUserAndTicket(@NonNull final CreateUserAndTicketDTOV2 createUserAndTicketDTOV2,
            @NonNull final List<AbstractResource> attachments) {
        validateCreateUserAndTicketDTO(createUserAndTicketDTOV2);

        log.info("Attempting to create ticket and user in Zammad");
        return ticketsApi.createNewTicketWithUser(createUserAndTicketDTOV2, attachments)
                .switchIfEmpty(Mono.error(new ZammadAPIException("Unable to create ticket and user")))
                .flatMap(response -> {
                    if (response == null || response.getTicket() == null || response.getTicket().getId() == null) {
                        return Mono.error(new ZammadAPIException("Ticket or ticket id is null in response"));
                    }
                    return Mono.just(response);
                })
                .doOnSuccess(response -> {
                    log.info("Successfully created ticket with id {} and user in Zammad", response.getTicket().getId());
                })
                .onErrorMap(WebClientResponseException.class, e -> new ZammadAPIException(e, "Failed to create ticket and user"));
    }

    private void validateCreateTicketDTO(@NonNull final CreateTicketDTOV2 createTicketDTOV2) {
        requireNonBlank(createTicketDTOV2.getTitle(), "title");
        requireNonBlank(createTicketDTOV2.getAnliegenart(), "anliegenart");
        requireNonBlank(createTicketDTOV2.getVertrauensniveau(), "vertrauensniveau");
        requireNonBlank(createTicketDTOV2.getGroup(), "group");
    }

    private void validateCreateUserAndTicketDTO(@NonNull final CreateUserAndTicketDTOV2 createUserAndTicketDTOV2) {
        final CreateTicketDTOV2 createTicketDTO = createUserAndTicketDTOV2.getCreateTicketDTO();
        if (createTicketDTO == null) {
            throw new IllegalArgumentException("createTicketDTO must not be null");
        }
        validateCreateTicketDTO(createTicketDTO);
    }

    private void requireNonBlank(final String value, final String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ticket " + fieldName + " must not be null or blank");
        }
    }
}
