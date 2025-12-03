package de.muenchen.stadtbezirksbudget.backend.configuration.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cache.Cache;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

@Deprecated
@ExtendWith(MockitoExtension.class)
class UserInfoAuthoritiesConverterTest {
    private static final String TEST_SUBJECT = "test-subject";
    private static final String TEST_TOKEN_VALUE = "test-token";
    private static final String USER_INFO_URI = "http://localhost/userinfo";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String AUTHORITIES = "authorities";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Cache cache;
    private UserInfoAuthoritiesConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserInfoAuthoritiesConverter(USER_INFO_URI, restTemplate, cache);
    }

    @Nested
    class Convert {
        @Test
        void testConvertWithAuthorities() {
            // Setup
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
            when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);

            final Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(AUTHORITIES, new String[] { ROLE_USER, ROLE_ADMIN });
            when(restTemplate.exchange(eq(USER_INFO_URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(responseMap, HttpStatus.OK));

            // Call
            final Collection<GrantedAuthority> authorities = converter.convert(jwt);

            // Assert
            assert authorities != null;
            assertEquals(2, authorities.size());
            assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_USER)));
            assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));
        }

        @Test
        void testConvertNoAuthorities() {
            // Setup
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
            when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);

            final Map<String, Object> responseMap = new HashMap<>();
            when(restTemplate.exchange(eq(USER_INFO_URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(responseMap, HttpStatus.OK));

            // Call
            final Collection<GrantedAuthority> authorities = converter.convert(jwt);

            // Assert
            assert authorities != null;
            assertTrue(authorities.isEmpty());
        }

        @Test
        void testConvertCacheHit() {
            // Setup
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
            final Collection<GrantedAuthority> cachedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER));
            when(cache.get(eq(TEST_SUBJECT))).thenReturn(() -> cachedAuthorities);
            final Collection<GrantedAuthority> authorities1 = converter.convert(jwt);
            assert authorities1 != null;
            assertEquals(1, authorities1.size());
            assertTrue(authorities1.contains(new SimpleGrantedAuthority(ROLE_USER)));
            verifyNoInteractions(restTemplate);
        }

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        void testMapWithoutAuthorities() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn("123");
            when(cache.get("123")).thenReturn(null);
            final Map<String, Object> responseMap = Map.of("no-authorities-here", "x");
            when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(responseMap);
            final Collection<GrantedAuthority> result = converter.convert(jwt);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        void testNullResponseFromUserInfo() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn("234");
            when(cache.get("234")).thenReturn(null);
            when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);
            final Collection<GrantedAuthority> result = converter.convert(jwt);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCacheHitWithNullValue() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn("abc");
            final Cache.ValueWrapper wrapper = mock(Cache.ValueWrapper.class);
            when(wrapper.get()).thenReturn(null);
            when(cache.get("abc")).thenReturn(wrapper);
            final Collection<GrantedAuthority> result = converter.convert(jwt);
            assertNull(result);
        }

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        void testAsAuthoritiesWithNonCollection() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn("sub1");
            when(cache.get("sub1")).thenReturn(null);
            final Map<String, Object> response = Map.of(AUTHORITIES, "not-a-collection");
            when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);
            final Collection<GrantedAuthority> result = converter.convert(jwt);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        void testAsAuthoritiesWithMixedCollection() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn("sub2");
            when(cache.get("sub2")).thenReturn(null);
            final List<Object> mixed = List.of(123, true, 4.56);
            final Map<String, Object> response = Map.of(AUTHORITIES, mixed);
            when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);
            final Collection<GrantedAuthority> result = converter.convert(jwt);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void testConvertWithException() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
            when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);
            when(cache.get(TEST_SUBJECT)).thenReturn(null);
            when(restTemplate.exchange(eq(USER_INFO_URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenThrow(new RuntimeException("Some error"));
            final Collection<GrantedAuthority> authorities2 = converter.convert(jwt);
            assertNotNull(authorities2);
            assertTrue(authorities2.isEmpty());
        }

        @Test
        void testConvertWithCollectionAuthorities() {
            final Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
            when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);
            final Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(AUTHORITIES, List.of("ROLE_USER", "ROLE_ADMIN")); // Simulate returning a collection
            when(restTemplate.exchange(eq(USER_INFO_URI), eq(HttpMethod.GET), any(), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(responseMap, HttpStatus.OK));
            final Collection<GrantedAuthority> authorities3 = converter.convert(jwt);
            assertNotNull(authorities3);
            assertEquals(2, authorities3.size());
            assertTrue(authorities3.contains(new SimpleGrantedAuthority("ROLE_USER")));
            assertTrue(authorities3.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
    }
}
