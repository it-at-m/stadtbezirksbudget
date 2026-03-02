package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragSortingIntegrationTest extends AntragBaseIntegrationTest {
    @Test
    void testGivenSortAscThenReturnSortedResults() throws Exception {
        antragBuilder.status(Status.VOLLSTAENDIG)
                .build();
        antragBuilder.status(Status.VOLLSTAENDIG)
                .build();
        final Antrag antrag = antragBuilder.status(Status.EINGEGANGEN)
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "status")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testGivenSortDescThenReturnSortedResults() throws Exception {
        antragBuilder.zammadNr("1")
                .build();
        antragBuilder.zammadNr("2")
                .build();
        final Antrag antrag = antragBuilder.zammadNr("3")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "zammadNr")
                        .param("sortDirection", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testGivenSortUnpagedThenReturnSortedResults() throws Exception {
        antragBuilder.projektTitel("2")
                .build();
        antragBuilder.projektTitel("3")
                .build();
        final Antrag antrag = antragBuilder.projektTitel("1")
                .build();

        mockMvc
                .perform(get("/antrag")
                        .param("size", "-1")
                        .param("sortBy", "projektTitel")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testGivenInvalidSortByThenReturnBadRequest() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "invalidField")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGivenInvalidSortDirectionThenReturnBadRequest() throws Exception {
        mockMvc
                .perform(get("/antrag")
                        .param("sortBy", "beantragtesBudget")
                        .param("sortDirection", "INVALID_DIRECTION")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
