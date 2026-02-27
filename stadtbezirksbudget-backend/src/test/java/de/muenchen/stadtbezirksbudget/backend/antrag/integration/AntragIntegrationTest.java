package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AndererZuwendungsantragDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.FinanzierungsmittelDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.VoraussichtlicheAusgabeDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AndererZuwendungsantragRepository;
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
    private AndererZuwendungsantragRepository andereZuwendungsantragRepository;
    @Autowired
    private MockMvc mockMvc;

    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragList.clear();
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository,
                andereZuwendungsantragRepository);
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
        private static String safeToString(final Object obj) {
            return (obj == null) ? null : obj.toString();
        }

        @Test
        void testGetDetailsWithoutVertretungsberechtigter() throws Exception {
            final Antrag antrag = antragBuilder.build();
            ObjectMapper objectMapper = new ObjectMapper();

            mockMvc
                    .perform(get("/antrag/" + antrag.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.allgemein.projektTitel", is(antrag.getProjekt().getTitel())))
                    .andExpect(jsonPath("$.allgemein.eingangDatum", is(safeToString(antrag.getEingangDatum()))))
                    .andExpect(jsonPath("$.allgemein.antragstellerName", is(antrag.getAntragsteller().getName())))
                    .andExpect(jsonPath("$.allgemein.beantragtesBudget").value(antrag.getFinanzierung().getBeantragtesBudget()))
                    .andExpect(jsonPath("$.allgemein.rubrik", is(antrag.getProjekt().getRubrik())))
                    .andExpect(jsonPath("$.allgemein.status", is(antrag.getBearbeitungsstand().getStatus().name())))
                    .andExpect(jsonPath("$.allgemein.zammadNr", is(antrag.getZammadTicketNr())))
                    .andExpect(jsonPath("$.allgemein.aktenzeichen", is(antrag.getAktenzeichen())))
                    .andExpect(jsonPath("$.allgemein.istGegendert", is(antrag.isIstGegendert())))
                    .andExpect(jsonPath("$.allgemein.eakteCooAdresse", is(antrag.getEakteCooAdresse())))
                    .andExpect(jsonPath("$.allgemein.anmerkungen", is(antrag.getBearbeitungsstand().getAnmerkungen())))

                    .andExpect(jsonPath("$.antragsteller.rechtsform", is(antrag.getAntragsteller().getRechtsform().name())))
                    .andExpect(jsonPath("$.antragsteller.strasseHausnummer", is(antrag.getAntragsteller().getAdresse().getStrasseHausnummer())))
                    .andExpect(jsonPath("$.antragsteller.ort", is(antrag.getAntragsteller().getAdresse().getOrt())))
                    .andExpect(jsonPath("$.antragsteller.postleitzahl", is(antrag.getAntragsteller().getAdresse().getPostleitzahl())))
                    .andExpect(jsonPath("$.antragsteller.weitereAngaben", is(antrag.getAntragsteller().getAdresse().getWeitereAngaben())))
                    .andExpect(jsonPath("$.antragsteller.email", is(antrag.getAntragsteller().getEmail())))
                    .andExpect(jsonPath("$.antragsteller.telefonNr", is(antrag.getAntragsteller().getTelefonNr())))
                    .andExpect(jsonPath("$.antragsteller.zielsetzung", is(antrag.getAntragsteller().getZielsetzung())))

                    .andExpect(jsonPath("$.bankverbindung.istVonVertretungsberechtigtem", is(antrag.getBankverbindung().isIstVonVertretungsberechtigtem())))
                    .andExpect(jsonPath("$.bankverbindung.geldinstitut", is(antrag.getBankverbindung().getGeldinstitut())))
                    .andExpect(jsonPath("$.bankverbindung.iban", is(antrag.getBankverbindung().getIban())))
                    .andExpect(jsonPath("$.bankverbindung.bic", is(antrag.getBankverbindung().getBic())))

                    .andExpect(jsonPath("$.bezirksinformationen.ausschussNr", is(antrag.getBezirksinformationen().getAusschussNr())))
                    .andExpect(jsonPath("$.bezirksinformationen.sitzungDatum", is(safeToString(antrag.getBezirksinformationen().getSitzungDatum()))))
                    .andExpect(jsonPath("$.bezirksinformationen.risNr", is(antrag.getBezirksinformationen().getRisNr())))
                    .andExpect(jsonPath("$.bezirksinformationen.beschlussStatus", is(safeToString(antrag.getBeschlussStatus()))))
                    .andExpect(jsonPath("$.bezirksinformationen.bewilligteFoerderung",
                            is(safeToString(antrag.getBezirksinformationen().getBewilligteFoerderung()))))
                    .andExpect(jsonPath("$.bezirksinformationen.bescheidDatum", is(safeToString(antrag.getBezirksinformationen().getBescheidDatum()))))

                    .andExpect(
                            jsonPath("$.finanzierung.istPersonVorsteuerabzugsberechtigt", is(antrag.getFinanzierung().isIstPersonVorsteuerabzugsberechtigt())))
                    .andExpect(jsonPath("$.finanzierung.istProjektVorsteuerabzugsberechtigt",
                            is(antrag.getFinanzierung().isIstProjektVorsteuerabzugsberechtigt())))
                    .andExpect(jsonPath("$.finanzierung.gesamtkosten", is(safeToString(antrag.getFinanzierung().getGesamtkosten()))))
                    .andExpect(jsonPath("$.finanzierung.kostenAnmerkung", is(antrag.getFinanzierung().getKostenAnmerkung())))
                    .andExpect(jsonPath("$.finanzierung.istZuwendungDritterBeantragt", is(antrag.isIstZuwendungDritterBeantragt())))
                    .andExpect(jsonPath("$.finanzierung.summeAndereZuwendungsantraege", is(safeToString(antrag.getSummeAndereZuwendungsantraege()))))
                    .andExpect(jsonPath("$.finanzierung.istZuwenigEigenmittel", is(antrag.getFinanzierung().isIstZuwenigEigenmittel())))
                    .andExpect(jsonPath("$.finanzierung.begruendungEigenmittel", is(antrag.getFinanzierung().getBegruendungEigenmittel())))
                    .andExpect(jsonPath("$.finanzierung.gesamtmittel", is(safeToString(antrag.getFinanzierung().getGesamtmittel()))))
                    .andExpect(jsonPath("$.finanzierung.istEinladungFoerderhinweis", is(antrag.getFinanzierung().isIstEinladungFoerderhinweis())))
                    .andExpect(jsonPath("$.finanzierung.istWebsiteFoerderhinweis", is(antrag.getFinanzierung().isIstWebsiteFoerderhinweis())))
                    .andExpect(jsonPath("$.finanzierung.istSonstigerFoerderhinweis", is(antrag.getFinanzierung().isIstSonstigerFoerderhinweis())))
                    .andExpect(jsonPath("$.finanzierung.sonstigeFoerderhinweise", is(antrag.getFinanzierung().getSonstigeFoerderhinweise())))

                    .andExpect(jsonPath("$.finanzierung.voraussichtlicheAusgaben").isArray())
                    .andExpect(content().json("{\"finanzierung\":{\"voraussichtlicheAusgaben\":" + objectMapper.writeValueAsString(
                            antrag.getFinanzierung().getVoraussichtlicheAusgaben().stream()
                                    .map(va -> new VoraussichtlicheAusgabeDTO(va.getKategorie(), va.getBetrag(), va.getDirektoriumNotiz())).toList()
                    ) + "}}"))
                    .andExpect(jsonPath("$.finanzierung.finanzierungsmittel").isArray())
                    .andExpect(content().json("{\"finanzierung\":{\"finanzierungsmittel\":" + objectMapper.writeValueAsString(
                            antrag.getFinanzierung().getFinanzierungsmittel().stream()
                                    .map(f -> new FinanzierungsmittelDTO(f.getKategorie(), f.getBetrag(), f.getDirektoriumNotiz())).toList()
                    ) + "}}"))
                    .andExpect(jsonPath("$.finanzierung.andereZuwendungsantraege").isArray())
                    .andExpect(content().json("{\"finanzierung\":{\"andereZuwendungsantraege\":" + objectMapper.writeValueAsString(
                            antrag.getAndereZuwendungsantraege().stream()
                                    .map(az -> new AndererZuwendungsantragDTO(az.getAntragsdatum(), az.getStelle(), az.getBetrag(), az.getStatus())).toList()
                    ) + "}}"))

                    .andExpect(jsonPath("$.projekt.beschreibung", is(antrag.getProjekt().getBeschreibung())))
                    .andExpect(jsonPath("$.projekt.start", is(safeToString(antrag.getProjekt().getStart()))))
                    .andExpect(jsonPath("$.projekt.ende", is(safeToString(antrag.getProjekt().getEnde()))))
                    .andExpect(jsonPath("$.projekt.fristBruchBegruendung", is(antrag.getProjekt().getFristBruchBegruendung())))

                    .andExpect(jsonPath("$.vertretungsberechtigter.nachname").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.vorname").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.strasseHausnummer").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.ort").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.postleitzahl").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.weitereAngaben").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.email").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.telefonNr").value(is((Object) null)))
                    .andExpect(jsonPath("$.vertretungsberechtigter.mobilNr").value(is((Object) null)))

                    .andExpect(jsonPath("$.zahlung.auszahlungBetrag", is(safeToString(antrag.getZahlung().getAuszahlungBetrag()))))
                    .andExpect(jsonPath("$.zahlung.auszahlungDatum", is(safeToString(antrag.getZahlung().getAuszahlungDatum()))))
                    .andExpect(jsonPath("$.zahlung.anlageAv", is(antrag.getZahlung().getAnlageAv())))
                    .andExpect(jsonPath("$.zahlung.anlageNr", is(antrag.getZahlung().getAnlageNr())))
                    .andExpect(jsonPath("$.zahlung.kreditor", is(antrag.getZahlung().getKreditor())))
                    .andExpect(jsonPath("$.zahlung.rechnungNr", is(antrag.getZahlung().getRechnungNr())))
                    .andExpect(jsonPath("$.zahlung.fiBelegNr", is(antrag.getZahlung().getFiBelegNr())))
                    .andExpect(jsonPath("$.zahlung.bestellung", is(antrag.getZahlung().getBestellung())));
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
