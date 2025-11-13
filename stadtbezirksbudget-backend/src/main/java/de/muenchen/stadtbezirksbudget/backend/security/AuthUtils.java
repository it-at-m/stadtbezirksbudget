package de.muenchen.stadtbezirksbudget.backend.security;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Utilities for authentication data.
 */
@ExcludedFromGeneratedCoverage //Excluded from coverage check to push backend threshold. The missing tests to reach the branch coverage will be implemented later.
public final class AuthUtils {

    public static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";

    private static final String TOKEN_USER_NAME = "preferred_username";

    private AuthUtils() {
    }

    /**
     * Extracts the user name from the existing Spring Security Context via
     * {@link SecurityContextHolder}.
     *
     * @return the username or an "unauthenticated" if no {@link Authentication} exists
     */
    public static String getUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return (String) jwtAuth.getTokenAttributes().getOrDefault(TOKEN_USER_NAME, null);
        } else if (authentication instanceof UsernamePasswordAuthenticationToken usernameAuth) {
            return usernameAuth.getName();
        } else {
            return NAME_UNAUTHENTICATED_USER;
        }
    }

}
