package de.muenchen.stadtbezirksbudget.backend.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Exception for invalid sort properties. */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class InvalidSortPropertyException extends ResponseStatusException {
    /**
     * InvalidSortPropertyException constructor.
     *
     * @param field the invalid sort field
     */
    public InvalidSortPropertyException(final String field) {
        super(HttpStatus.BAD_REQUEST, "Unsupported sort field: " + field);
    }
}
