package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import java.io.Serial;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Exception representing errors that occur when interacting with the Zammad EAI API.
 */
@Getter
public class ZammadAPIException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Status code indicating that the status code is unknown.
     */
    public static final int STATUS_CODE_UNKNOWN = -1;

    private final int statusCode;

    /**
     * Creates a new {@link ZammadAPIException} with the given {@link WebClientResponseException} and
     * message.
     *
     * @param e the {@link WebClientResponseException} that caused this exception
     * @param message the detail message
     */
    public ZammadAPIException(final WebClientResponseException e, final String message) {
        super(message, e);
        this.statusCode = e.getStatusCode().value();
    }

    /**
     * Creates a new {@link ZammadAPIException} with the given {@link WebClientResponseException}.
     *
     * @param e the {@link WebClientResponseException} that caused this exception
     */
    public ZammadAPIException(final WebClientResponseException e) {
        super(e.getMessage(), e);
        this.statusCode = e.getStatusCode().value();
    }

    /**
     * Creates a new {@link ZammadAPIException} with the given message. The status code is set to
     * {@link #STATUS_CODE_UNKNOWN}.
     *
     * @param message the detail message
     */
    public ZammadAPIException(final String message) {
        super(message);
        this.statusCode = STATUS_CODE_UNKNOWN;
    }

    /**
     * Returns a string representation of the {@link ZammadAPIException}.
     *
     * @return a string representation of the {@link ZammadAPIException}
     */
    @Override
    public String toString() {
        return "ZammadAPIException{" +
                "statusCode=" + statusCode +
                ", message='" + getMessage() + "'" +
                "} " + super.toString();
    }
}
