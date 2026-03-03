package de.muenchen.stadtbezirksbudget.backend.configuration.frontend;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.IntegrationTestConfiguration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class FrontendConfigIntegrationTest {

    @Nested
    @AutoConfigureMockMvc
    @SpringBootTest(
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
            properties = {
                    "frontend.config.zammad-base-url=https://zammad.example.local",
                    "frontend.config.eakte-base-url=https://eakte.example.local"
            }
    )
    @Import(IntegrationTestConfiguration.class)
    class GetFrontendConfig {
        @Autowired
        private MockMvc mockMvc;

        @Test
        void testReturnsZammadBaseUrl() throws Exception {
            mockMvc
                    .perform(get("/frontend-config")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.zammadBaseUrl").value("https://zammad.example.local"));
        }

        @Test
        void testReturnsEakteBaseUrl() throws Exception {
            mockMvc
                    .perform(get("/frontend-config")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.eakteBaseUrl").value("https://eakte.example.local"));
        }
    }

    @Nested
    @AutoConfigureMockMvc
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @Import(IntegrationTestConfiguration.class)
    class GetFrontendConfigDefault {
        @Autowired
        private MockMvc mockMvc;

        @Test
        void testReturnsZammadBaseUrl() throws Exception {
            mockMvc
                    .perform(get("/frontend-config")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.zammadBaseUrl").value("https://zammad.muenchen.de"));
        }

        @Test
        void testReturnsEakteBaseUrl() throws Exception {
            mockMvc
                    .perform(get("/frontend-config")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.eakteBaseUrl").value("https://akte.muenchen.de"));
        }
    }
}
