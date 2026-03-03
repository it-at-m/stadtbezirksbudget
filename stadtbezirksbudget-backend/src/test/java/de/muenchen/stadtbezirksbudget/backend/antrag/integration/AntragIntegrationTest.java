package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.antrag.AntragMapper;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragIntegrationTest extends AntragBaseIntegrationTest {
    private final List<Antrag> antragList = new ArrayList<>();

    @Autowired
    private AntragMapper antragMapper;

    @Nested
    class GetAntragSummaryPage {

        @Test
        void testGivenPageAndSizeThenReturnPageOfAntragsdaten() throws Exception {
            for (int i = 0; i < 6; i++) {
                antragBuilder.build();
            }

            mockMvc
                    .perform(get("/antrag")
                            .param("page", "2")
                            .param("size", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.page.totalElements", is(6)))
                    .andExpect(jsonPath("$.page.size", is(2)))
                    .andExpect(jsonPath("$.page.number", is(2)))
                    .andExpect(jsonPath("$.page.totalPages", is(3)));
        }

        @Test
        void testGivenNoParamsThenReturnDefaultPage() throws Exception {
            for (int i = 0; i < 11; i++) {
                antragBuilder.build();
            }

            mockMvc
                    .perform(get("/antrag")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.page.totalElements", is(11)))
                    .andExpect(jsonPath("$.page.size", is(10)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(2)));
        }

        @Test
        void testGivenEmptyDatabaseThenReturnEmptyPage() throws Exception {
            mockMvc
                    .perform(get("/antrag")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.page.totalElements", is(0)))
                    .andExpect(jsonPath("$.page.size", is(10)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(0)));
        }

        @Test
        void testGivenAllSizeThenReturnUnpaged() throws Exception {
            for (int i = 0; i < 11; i++) {
                antragBuilder.build();
            }

            mockMvc
                    .perform(get("/antrag")
                            .param("size", "-1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(11)))
                    .andExpect(jsonPath("$.page.totalElements", is(11)))
                    .andExpect(jsonPath("$.page.size", is(11)))
                    .andExpect(jsonPath("$.page.number", is(0)))
                    .andExpect(jsonPath("$.page.totalPages", is(1)));
        }
    }

    @Nested
    class GetDetails {

        @Test
        void testGetDetails() throws Exception {
            final Antrag antrag = antragBuilder.build();

            mockMvc
                    .perform(get("/antrag/" + antrag.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(antragMapper.toDetailsDTO(antrag))));
        }

        @Test
        void testGetDetailsNotExisting() throws Exception {
            mockMvc
                    .perform(get("/antrag/80000000-0000-0000-0000-000000000013")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetFilterOptions {
        @Test
        void testGetFilterOptionsReturnsAlphabeticallySortedLists() throws Exception {
            antragList.add(antragBuilder
                    .antragstellerName("Alex")
                    .projektTitel("Zeltlager")
                    .build());
            antragList.add(antragBuilder
                    .antragstellerName("Alina")
                    .projektTitel("Wochenmarkt")
                    .build());
            antragList.add(antragBuilder
                    .antragstellerName("Musterfrau")
                    .projektTitel("Aktionswoche")
                    .build());
            antragList.add(antragBuilder
                    .antragstellerName("Musterfrau")
                    .projektTitel("Weihnachtsmarkt")
                    .build());
            final List<String> sortedAntragstellerNamen = antragList.stream()
                    .map(Antrag::getAntragsteller)
                    .map(Antragsteller::getName)
                    .sorted()
                    .toList();
            final List<String> sortedProjektTitel = antragList.stream()
                    .map(Antrag::getProjekt)
                    .map(Projekt::getTitel)
                    .sorted()
                    .toList();

            mockMvc
                    .perform(get("/antrag/filterOptions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.antragstellerNamen").isArray())
                    .andExpect(jsonPath("$.projektTitel").isArray())
                    .andExpect(jsonPath("$.antragstellerNamen", hasSize(3)))
                    .andExpect(jsonPath("$.projektTitel", hasSize(4)))
                    .andExpect(jsonPath("$.antragstellerNamen[0]", is(sortedAntragstellerNamen.getFirst())))
                    .andExpect(jsonPath("$.antragstellerNamen[2]", is(sortedAntragstellerNamen.get(2))))
                    .andExpect(jsonPath("$.projektTitel[0]", is(sortedProjektTitel.getFirst())))
                    .andExpect(jsonPath("$.projektTitel[3]", is(sortedProjektTitel.get(3))));
        }
    }
}
