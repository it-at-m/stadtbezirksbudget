package de.muenchen.stadtbezirksbudget.cit_eai.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class GetUsername {

        @Test
        void testJwtAuthTokenReturnsUsername() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(Map.of("preferred_username", "testUser"));
            final JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt, Collections.emptyList());
            when(securityContext.getAuthentication()).thenReturn(jwtAuth);
            final String username = AuthUtils.getUsername();

            assertThat(username).isEqualTo("testUser");
        }

        @Test
        void testJwtAuthTokenWithMissingClaimReturnsDefault() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(Collections.emptyMap());
            final JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt, Collections.emptyList());
            when(securityContext.getAuthentication()).thenReturn(jwtAuth);
            final String username = AuthUtils.getUsername();
            assertThat(username).isNull();
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
