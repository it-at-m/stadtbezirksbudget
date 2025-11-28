package de.muenchen.stadtbezirksbudget.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class AuthUtilsTest {

    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    class GetUsername {

        @Test
        void testJwtAuthTokenReturnsUsername() {
            final Jwt jwt = mock(Jwt.class);
            final Map<String, Object> attributes = new HashMap<>();
            attributes.put("preferred_username", "testUser");
            when(jwt.getClaims()).thenReturn(attributes);
            final Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
            final JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt, authorities);
            when(securityContext.getAuthentication()).thenReturn(jwtAuth);
            final String username = AuthUtils.getUsername();

            assertThat(username).isEqualTo("testUser");
        }

        @Test
        void testUsernamePasswordAuthReturnsUsername() {
            final UsernamePasswordAuthenticationToken usernameAuth = new UsernamePasswordAuthenticationToken("username", "password");
            when(securityContext.getAuthentication()).thenReturn(usernameAuth);
            final String username = AuthUtils.getUsername();

            assertThat(username).isEqualTo("username");
        }

        @Test
        void testNoAuthReturnsUnauthenticated() {
            when(securityContext.getAuthentication()).thenReturn(null);
            final String username = AuthUtils.getUsername();

            assertThat(username).isEqualTo(AuthUtils.NAME_UNAUTHENTICATED_USER);
        }
    }
}
