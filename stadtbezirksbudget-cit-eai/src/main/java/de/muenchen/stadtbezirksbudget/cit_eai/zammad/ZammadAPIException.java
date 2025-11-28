package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import java.io.Serial;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Exception representing errors that occur when interacting with the Zammad EAI.
 */
@Getter
public class ZammadAPIException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int statusCode;

    /**
     * Creates a new ZammadEAIException with the given WebClientResponseException and message.
     *
     * @param e the WebClientResponseException that caused this exception
     * @param message the detail message
     */
    public ZammadAPIException(final WebClientResponseException e, final String message) {
        super(message, e);
        this.statusCode = e.getStatusCode().value();
    }

    /**
     * Creates a new ZammadEAIException with the given WebClientResponseException.
     *
     * @param e the WebClientResponseException that caused this exception
     */
    public ZammadAPIException(final WebClientResponseException e) {
        super(e.getMessage(), e);
        this.statusCode = e.getStatusCode().value();
    }

    /**
     * Creates a new ZammadEAIException with the given message. The status code is set to -1.
     *
     * @param message the detail message
     */
    public ZammadAPIException(final String message) {
        super(message);
        this.statusCode = -1;
    }

    /**
     * Returns a string representation of the ZammadEAIException.
     *
     * @return a string representation of the ZammadEAIException
     */
    @Override
    public String toString() {
        return "ZammadEAIException{" +
                "statusCode=" + statusCode +
                ", message='" + getMessage() + '\'' +
                "} " + super.toString();
    }
}
