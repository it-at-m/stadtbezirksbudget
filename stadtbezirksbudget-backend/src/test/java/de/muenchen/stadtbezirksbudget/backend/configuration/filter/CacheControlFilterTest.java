package de.muenchen.stadtbezirksbudget.backend.configuration.filter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class CacheControlFilterTest {

    private static final String EXPECTED_CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";
    private CacheControlFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

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
