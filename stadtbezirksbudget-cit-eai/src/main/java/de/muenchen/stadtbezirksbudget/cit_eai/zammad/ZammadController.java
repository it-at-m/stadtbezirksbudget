package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import de.muenchen.stadtbezirksbudget.cit_eai.security.Authorities;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.model.CreateTicketDTOV2;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for handling Zammad ticket operations.
 */
@RequestMapping("/zammad")
@RestController
@RequiredArgsConstructor
public class ZammadController {
    private final ZammadAPIService zammadAPIService;

    /**
     * Endpoint to create a new Zammad ticket.
     *
     * @param createTicketDTOV2 the DTO containing ticket creation details
     * @param lhmextid optional external ID
     * @param userid optional user ID
     * @return a confirmation message with the created ticket ID
     */
    @PreAuthorize(Authorities.ZAMMAD_CREATE_TICKET)
    @PostMapping("/createTicket")
    public Mono<ResponseEntity<String>> createTicket(@RequestBody final CreateTicketDTOV2 createTicketDTOV2,
            @RequestParam(required = false) final String lhmextid,
            @RequestParam(required = false) final String userid) {
        return zammadAPIService.createTicket(createTicketDTOV2, lhmextid, userid, List.of())
                .map(ticket -> ResponseEntity.status(HttpStatus.CREATED).body("Ticket created with ID: " + ticket.getId()));
    }
}
