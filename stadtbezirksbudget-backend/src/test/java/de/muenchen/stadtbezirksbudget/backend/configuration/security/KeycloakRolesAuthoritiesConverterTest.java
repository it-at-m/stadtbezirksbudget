package de.muenchen.stadtbezirksbudget.backend.configuration.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesAuthoritiesConverterTest {
    private static final String TEST_CLIENT = "test-client";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";

    @Mock
    private SecurityProperties securityProperties;
    @InjectMocks
    private KeycloakRolesAuthoritiesConverter converter;

    @BeforeEach
    void setUp() {
        when(securityProperties.getClientId()).thenReturn(TEST_CLIENT);
    }

    @Nested
    class Convert {

        @Test
        void testConvertWithRoles() {
            final Map<String, Object> resourceAccessClaim = new HashMap<>();
            resourceAccessClaim.put(TEST_CLIENT, Map.of("roles", List.of("admin", "user")));
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM)).thenReturn(resourceAccessClaim);
            final Collection<GrantedAuthority> authorities = converter.convert(jwt);
            assertNotNull(authorities);
            assertEquals(2, authorities.size());
            assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_admin")));
            assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_user")));
        }

        @Test
        void testConvertWithoutRoles() {
            final Map<String, Object> resourceAccessClaim = new HashMap<>();
            resourceAccessClaim.put(TEST_CLIENT, Collections.emptyMap());
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM)).thenReturn(resourceAccessClaim);
            final Collection<GrantedAuthority> authorities = converter.convert(jwt);
            assertNotNull(authorities);
            assertTrue(authorities.isEmpty());
        }

        @Test
        void testConvertClientNotInResourceAccess() {
            final Map<String, Object> resourceAccessClaim = new HashMap<>();
            resourceAccessClaim.put("other-client", Map.of("roles", List.of("admin")));
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM)).thenReturn(resourceAccessClaim);
            final Collection<GrantedAuthority> authorities = converter.convert(jwt);
            assertNotNull(authorities);
            assertTrue(authorities.isEmpty());
        }

        @Test
        void testConvertNullClaims() {
            final Jwt jwt = mock(Jwt.class);
            final Collection<GrantedAuthority> authorities = converter.convert(jwt);
            assertNotNull(authorities);
            assertTrue(authorities.isEmpty());
        }

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        void testConvertNullJwt() {
            final Collection<GrantedAuthority> authorities = converter.convert(null);
            assertNotNull(authorities);
            assertTrue(authorities.isEmpty());
        }
    }
}
