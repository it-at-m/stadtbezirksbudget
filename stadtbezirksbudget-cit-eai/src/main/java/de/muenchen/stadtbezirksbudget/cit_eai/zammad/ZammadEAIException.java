package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import java.io.Serial;
import java.nio.charset.Charset;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ZammadEAIException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String message;
    private final String responseBody;

    public ZammadEAIException(final WebClientResponseException e, final String message) {
        super(message, e);
        this.statusCode = e.getStatusCode().value();
        this.responseBody = e.getResponseBodyAsString();
        this.message = message;
    }

    public ZammadEAIException(final WebClientResponseException e) {
        super(e.getMessage());
        this.statusCode = e.getStatusCode().value();
        this.responseBody = e.getResponseBodyAsString(Charset.defaultCharset());
        this.message = e.getMessage();
    }

    public ZammadEAIException(final String message) {
        super(message);
        this.statusCode = -1;
        this.message = message;
        this.responseBody = "";
    }

    @Override
    public String toString() {
        return "ZammadEAIException{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", responseBody='" + responseBody + '\'' +
                "} " + super.toString();
    }
}
