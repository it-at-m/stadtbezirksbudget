package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class ZammadEAIException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String responseBody;

    public ZammadEAIException(final WebClientResponseException e, final String message) {
        super(message, e);
        this.statusCode = e.getStatusCode().value();
        this.responseBody = e.getResponseBodyAsString();
    }

    public ZammadEAIException(final WebClientResponseException e) {
        super(e.getMessage());
        this.statusCode = e.getStatusCode().value();
        this.responseBody = e.getResponseBodyAsString(StandardCharsets.UTF_8);
    }

    public ZammadEAIException(final String message) {
        super(message);
        this.statusCode = -1;
        this.responseBody = "";
    }

    @Override
    public String toString() {
        return "ZammadEAIException{" +
                "statusCode=" + statusCode +
                ", message='" + getMessage() + '\'' +
                ", responseBody='" + responseBody + '\'' +
                "} " + super.toString();
    }
}
