package de.muenchen.stadtbezirksbudget.backend.configuration.filter;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.mockito.ArgumentMatchers.eq;
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

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));
    private static final String ANTRAG_ENDPOINT_URL = "/antrag";
    private static final String EXPECTED_CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";
    private CacheControlFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
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
    class DoFilterInternal {

        @Test
        void testAddsCacheControlHeaderWhenNotPresent() throws ServletException, IOException {
            when(response.getHeader(HttpHeaders.CACHE_CONTROL)).thenReturn(null);
            filter.doFilterInternal(request, response, filterChain);
            verify(response).addHeader(
                    HttpHeaders.CACHE_CONTROL,
                    EXPECTED_CACHE_CONTROL_HEADER_VALUES);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void testAddsCacheControlHeaderWhenBlank() throws ServletException, IOException {
            when(response.getHeader(HttpHeaders.CACHE_CONTROL)).thenReturn("   ");
            filter.doFilterInternal(request, response, filterChain);
            verify(response).addHeader(
                    HttpHeaders.CACHE_CONTROL,
                    EXPECTED_CACHE_CONTROL_HEADER_VALUES);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void testDoesNotOverrideExistingCacheControlHeader() throws ServletException, IOException {
            when(response.getHeader(HttpHeaders.CACHE_CONTROL)).thenReturn("max-age=3600");
            filter.doFilterInternal(request, response, filterChain);
            verify(response, never()).addHeader(eq(HttpHeaders.CACHE_CONTROL), anyString());
            verify(filterChain).doFilter(request, response);
        }
    }

}
