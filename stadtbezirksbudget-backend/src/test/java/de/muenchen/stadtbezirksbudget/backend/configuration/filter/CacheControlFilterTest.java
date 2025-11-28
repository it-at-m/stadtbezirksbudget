package de.muenchen.stadtbezirksbudget.backend.configuration.filter;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.stadtbezirksbudget.backend.StadtbezirksbudgetBackend;
import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(
        classes = { StadtbezirksbudgetBackend.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class CacheControlFilterTest {

    private CacheControlFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    private static final String ENTITY_ENDPOINT_URL = "/theEntity";

    private static final String EXPECTED_CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setUp() {
        filter = new CacheControlFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Nested
    class DoFilterInternalTests {
        @Test
        void testForCacheControlHeadersForEntityEndpoint() {
            final ResponseEntity<String> response = testRestTemplate.exchange(ENTITY_ENDPOINT_URL, HttpMethod.GET, null, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getHeaders().containsKey(HttpHeaders.CACHE_CONTROL));
            assertEquals(EXPECTED_CACHE_CONTROL_HEADER_VALUES, response.getHeaders().getCacheControl());
        }

        @Test
        void testAddsCacheControlHeaderWhenNotPresent() throws ServletException, IOException {
            when(response.getHeader(HttpHeaders.CACHE_CONTROL)).thenReturn(null);
            filter.doFilterInternal(request, response, filterChain);
            verify(response).addHeader(
                    HttpHeaders.CACHE_CONTROL,
                    "no-cache, no-store, must-revalidate");
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void testAddsCacheControlHeaderWhenBlank() throws ServletException, IOException {
            when(response.getHeader(HttpHeaders.CACHE_CONTROL)).thenReturn("   ");
            filter.doFilterInternal(request, response, filterChain);
            verify(response).addHeader(
                    HttpHeaders.CACHE_CONTROL,
                    "no-cache, no-store, must-revalidate");
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void testDoesNotOverrideExistingCacheControlHeader() throws ServletException, IOException {
            when(response.getHeader(anyString())).thenReturn("max-age=3600");
            filter.doFilterInternal(request, response, filterChain);
            verify(response, never()).addHeader(anyString(), anyString());
            verify(filterChain).doFilter(request, response);
        }
    }

}
