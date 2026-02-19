package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragIntegrationTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);

    private final List<Antrag> antragList = new ArrayList<>();

    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;
    @Autowired
    private MockMvc mockMvc;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragList.clear();
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository);
    }

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
                    .andExpect(jsonPath("$.allgemein.projektTitel", is(antrag.getProjekt().getTitel())))
                    .andExpect(jsonPath("$.allgemein.eingangDatum", is(antrag.getEingangDatum().toString())))
                    .andExpect(jsonPath("$.allgemein.antragstellerName", is(antrag.getAntragsteller().getName())))
                    .andExpect(jsonPath("$.allgemein.beantragtesBudget").value(antrag.getFinanzierung().getBeantragtesBudget().toPlainString()))
                    .andExpect(jsonPath("$.allgemein.rubrik", is("Rubrik")))
                    .andExpect(jsonPath("$.allgemein.status", is(antrag.getBearbeitungsstand().getStatus().name())))
                    .andExpect(jsonPath("$.allgemein.zammadNr", is(antrag.getZammadTicketNr())))
                    .andExpect(jsonPath("$.allgemein.aktenzeichen", is(antrag.getAktenzeichen())))
                    .andExpect(jsonPath("$.allgemein.istGegendert", is(false)))
                    .andExpect(jsonPath("$.allgemein.eakteCooAdresse", is(antrag.getEakteCooAdresse())))
                    .andExpect(jsonPath("$.allgemein.anmerkungen", is(antrag.getBearbeitungsstand().getAnmerkungen())));
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
