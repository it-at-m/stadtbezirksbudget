package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import java.io.Serial;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class ZammadEAIException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int statusCode;

    public ZammadEAIException(final WebClientResponseException e, final String message) {
        super(message, e);
        this.statusCode = e.getStatusCode().value();
    }

    public ZammadEAIException(final WebClientResponseException e) {
        super(e.getMessage(), e);
        this.statusCode = e.getStatusCode().value();
    }

    public ZammadEAIException(final String message) {
        super(message);
        this.statusCode = -1;
    }

    @Override
    public String toString() {
        return "ZammadEAIException{" +
                "statusCode=" + statusCode +
                ", message='" + getMessage() + '\'' +
                "} " + super.toString();
    }
}
