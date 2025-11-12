package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import java.io.Serial;
import java.nio.charset.Charset;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ZammadEAIException extends WebClientResponseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ZammadEAIException(final WebClientResponseException e, final String message) {
        super(e.getStatusCode().value(), message, e.getHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset(), e.getRequest());
    }

    public ZammadEAIException(final String message){
        super(0, message, null, null, null, null);
    }

}
