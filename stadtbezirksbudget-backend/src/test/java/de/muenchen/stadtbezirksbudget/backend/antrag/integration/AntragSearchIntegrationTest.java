package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragSearchIntegrationTest extends AntragBaseIntegrationTest {
    @Test
    void testNullSearch() throws Exception {
        final Antrag antrag = antragBuilder.build();
        mockMvc
                .perform(get("/antrag")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testEmptySearch() throws Exception {
        final Antrag antrag = antragBuilder.build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchZammadNr() throws Exception {
        final Antrag antrag = antragBuilder.zammadNr("123000")
                .build();
        antragBuilder.zammadNr("456000")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "123000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchAktenzeichen() throws Exception {
        final Antrag antrag = antragBuilder.aktenzeichen("A-27")
                .build();
        antragBuilder.aktenzeichen("B-30")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "A-27")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchAntragstellerName() throws Exception {
        final Antrag antrag = antragBuilder.antragstellerName("Ursula Müller")
                .build();
        antragBuilder.antragstellerName("Hans Müller")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "Müller")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
        mockMvc
                .perform(get("/antrag")
                        .param("search", "Ursula")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

    @Test
    void testSearchProjektTitel() throws Exception {
        final Antrag antrag = antragBuilder.projektTitel("Hausfest")
                .build();
        antragBuilder.projektTitel("Weihnachtsfest")
                .build();
        mockMvc
                .perform(get("/antrag")
                        .param("search", "fest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
        mockMvc
                .perform(get("/antrag")
                        .param("search", "Haus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(antrag.getId().toString())));
    }

}
