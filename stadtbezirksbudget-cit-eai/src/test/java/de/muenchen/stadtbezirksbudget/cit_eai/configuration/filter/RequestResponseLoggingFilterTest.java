package de.muenchen.stadtbezirksbudget.cit_eai.configuration.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.stadtbezirksbudget.cit_eai.configuration.security.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

public class RequestResponseLoggingFilterTest {

    private static final String TEST_ENDPOINT_URL = "/test";

    private RequestResponseLoggingFilter filter;
    private SecurityProperties securityProperties;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private FilterChain mockFilterChain;

    @BeforeEach
    void setUp() {
        securityProperties = mock(SecurityProperties.class);
        filter = new RequestResponseLoggingFilter(securityProperties);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockFilterChain = mock(FilterChain.class);
    }

    @Nested
    class CheckForLogging {
        @Test
        void testFilterDelegatesWithoutErrorForAllMode() throws IOException, ServletException {

            when(securityProperties.getLoggingMode()).thenReturn(RequestResponseLoggingFilter.LoggingMode.ALL);
            when(mockRequest.getMethod()).thenReturn(HttpMethod.GET.name());
            when(mockRequest.getRequestURI()).thenReturn(TEST_ENDPOINT_URL);
            when(mockResponse.getStatus()).thenReturn(200);
            filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
            verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        }

        @Test
        void testFilterDelegatesWithoutErrorForNoneMode() throws IOException, ServletException {
            when(securityProperties.getLoggingMode()).thenReturn(RequestResponseLoggingFilter.LoggingMode.NONE);
            when(mockRequest.getMethod()).thenReturn(HttpMethod.POST.name());
            when(mockRequest.getRequestURI()).thenReturn(TEST_ENDPOINT_URL);
            when(mockResponse.getStatus()).thenReturn(200);
            filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
            verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        }

        @Test
        void testFilterDelegatesWithoutErrorForChangingMode() throws IOException, ServletException {
            when(securityProperties.getLoggingMode()).thenReturn(RequestResponseLoggingFilter.LoggingMode.CHANGING);
            when(mockRequest.getMethod()).thenReturn(HttpMethod.POST.name());
            when(mockRequest.getRequestURI()).thenReturn(TEST_ENDPOINT_URL);
            when(mockResponse.getStatus()).thenReturn(200);
            filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
            verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        }

        @Test
        void testFilterDelegatesWithoutErrorForChangingModeWithErrorStatus() throws IOException, ServletException {
            when(securityProperties.getLoggingMode()).thenReturn(RequestResponseLoggingFilter.LoggingMode.CHANGING);
            when(mockRequest.getMethod()).thenReturn(HttpMethod.POST.name());
            when(mockRequest.getRequestURI()).thenReturn(TEST_ENDPOINT_URL);
            when(mockResponse.getStatus()).thenReturn(500);
            filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
            verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        }
    }
}
