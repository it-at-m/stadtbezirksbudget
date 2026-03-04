package de.muenchen.stadtbezirksbudget.backend.antrag;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muenchen.stadtbezirksbudget.backend.IntegrationTestConfiguration;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AdresseDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AndererZuwendungsantragDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragAllgemeinDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragAntragstellerDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragBankverbindungDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragBezirksinformationenDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragFinanzierungDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragProjektDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragVertretungsberechtigterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragVerwendungsnachweisDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragZahlungDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.FinanzierungsmittelDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.VoraussichtlicheAusgabeDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.integration.AntragBuilder;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AndererZuwendungsantragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@Import(IntegrationTestConfiguration.class)
@SuppressWarnings({ "PMD.CouplingBetweenObjects" })
public class AntragDetailsMappingTest {

    @PersistenceContext
    private EntityManager entityManager;
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
    private AntragMapperImpl antragMapper;
    private AntragBuilder antragBuilder;

    @BeforeEach
    public void setUp() {
        antragBuilder = new AntragBuilder(antragRepository, finanzierungRepository, voraussichtlicheAusgabeRepository, finanzierungsmittelRepository,
                andereZuwendungsantragRepository);
    }

    private Antrag loadAntrag(final AntragBuilder antrag) {
        entityManager.flush();
        entityManager.clear();
        return antragRepository.findById(antrag.build().getId()).orElseThrow();
    }

    @Nested
    class Allgemein {
        @Test
        void testAntragDetailsAllgemein() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragAllgemeinDTO expected = new AntragAllgemeinDTO(
                    antrag.getEingangDatum(),
                    antrag.getBearbeitungsstand().getStatus(),
                    antrag.getZammadTicketNr(),
                    antrag.getAktenzeichen(),
                    antrag.getEakteCooAdresse(),
                    antrag.isIstGegendert(),
                    antrag.getBearbeitungsstand().getAnmerkungen(),
                    antrag.getBeschlussStatus(),
                    antrag.isIstZuwendungDritterBeantragt(),
                    antrag.getSummeAndereZuwendungsantraege(),
                    antragMapper.andererZuwendungsantragListToAndererZuwendungsantragDTOList(antrag.getAndereZuwendungsantraege()));
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).allgemein());
        }

        @Test
        void testAndererZuwendungsantragListToAndererZuwendungsantragDTOList() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final List<AndererZuwendungsantragDTO> expected = antrag.getAndereZuwendungsantraege().stream().map(
                    az -> new AndererZuwendungsantragDTO(az.getAntragsdatum(), az.getStelle(), az.getBetrag(), az.getStatus())).toList();
            assertEquals(expected, antragMapper.andererZuwendungsantragListToAndererZuwendungsantragDTOList(antrag.getAndereZuwendungsantraege()));
        }
    }

    @Nested
    class Finanzierung {
        @Test
        void testAntragDetailsFinanzierung() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragFinanzierungDTO expected = new AntragFinanzierungDTO(
                    antrag.getFinanzierung().getBeantragtesBudget(),
                    antrag.getFinanzierung().isIstPersonVorsteuerabzugsberechtigt(),
                    antrag.getFinanzierung().isIstProjektVorsteuerabzugsberechtigt(),
                    antragMapper.voraussichtlicheAusgabeListToVoraussichtlicheAusgabeDTOList(antrag.getFinanzierung().getVoraussichtlicheAusgaben()),
                    antrag.getFinanzierung().getGesamtkosten(),
                    antrag.getFinanzierung().getKostenAnmerkung(),
                    antragMapper.finanzierungsmittelListToFinanzierungsmittelDTOList(antrag.getFinanzierung().getFinanzierungsmittel()),
                    antrag.getFinanzierung().isIstZuwenigEigenmittel(),
                    antrag.getFinanzierung().getBegruendungEigenmittel(),
                    antrag.getFinanzierung().getGesamtmittel(),
                    antrag.getFinanzierung().isIstEinladungFoerderhinweis(),
                    antrag.getFinanzierung().isIstWebsiteFoerderhinweis(),
                    antrag.getFinanzierung().isIstSonstigerFoerderhinweis(),
                    antrag.getFinanzierung().getSonstigeFoerderhinweise());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).finanzierung());
        }

        @Test
        void testFinanzierungsmittelListToFinanzierungsmittelDTOList() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final List<FinanzierungsmittelDTO> expected = antrag.getFinanzierung().getFinanzierungsmittel().stream().map(
                    fm -> new FinanzierungsmittelDTO(fm.getKategorie(), fm.getBetrag(), fm.getDirektoriumNotiz())).toList();
            assertEquals(expected, antragMapper.finanzierungsmittelListToFinanzierungsmittelDTOList(antrag.getFinanzierung().getFinanzierungsmittel()));
        }

        @Test
        void testAndererZuwendungsantragListToAndererZuwendungsantragDTOList() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final List<VoraussichtlicheAusgabeDTO> expected = antrag.getFinanzierung().getVoraussichtlicheAusgaben().stream().map(
                    va -> new VoraussichtlicheAusgabeDTO(va.getKategorie(), va.getBetrag(), va.getDirektoriumNotiz())).toList();
            assertEquals(expected,
                    antragMapper.voraussichtlicheAusgabeListToVoraussichtlicheAusgabeDTOList(antrag.getFinanzierung().getVoraussichtlicheAusgaben()));
        }
    }

    @Nested
    class Antragsteller {
        @Test
        void testAntragDetailsAntragsteller() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragAntragstellerDTO expected = new AntragAntragstellerDTO(
                    antrag.getAntragsteller().getVorname(),
                    antrag.getAntragsteller().getName(),
                    antrag.getAntragsteller().getTelefonNr(),
                    antrag.getAntragsteller().getEmail(),
                    antrag.getAntragsteller().getZielsetzung(),
                    antrag.getAntragsteller().getRechtsform(),
                    antragMapper.adresseToAdresseDTO(antrag.getAntragsteller().getAdresse()));
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).antragsteller());
        }
    }

    @Nested
    class Bankverbindung {
        @Test
        void testAntragDetailsBankverbindung() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragBankverbindungDTO expected = new AntragBankverbindungDTO(
                    antrag.getBankverbindung().isIstVonVertretungsberechtigtem(),
                    antrag.getBankverbindung().getGeldinstitut(),
                    antrag.getBankverbindung().getIban(),
                    antrag.getBankverbindung().getBic());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).bankverbindung());
        }
    }

    @Nested
    class Bezirksinformationen {
        @Test
        void testAntragDetailsBezirksausschuss() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragBezirksinformationenDTO expected = new AntragBezirksinformationenDTO(
                    antrag.getBezirksinformationen().getAusschussNr(),
                    antrag.getBezirksinformationen().getSitzungDatum(),
                    antrag.getBezirksinformationen().getRisNr(),
                    antrag.getBezirksinformationen().getBewilligteFoerderung(),
                    antrag.getBezirksinformationen().getBescheidDatum());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).bezirksinformationen());
        }
    }

    @Nested
    class Projekt {
        @Test
        void testAntragDetailsProjekt() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragProjektDTO expected = new AntragProjektDTO(
                    antrag.getProjekt().getTitel(),
                    antrag.getProjekt().getBeschreibung(),
                    antrag.getProjekt().getRubrik(),
                    antrag.getProjekt().getStart(),
                    antrag.getProjekt().getEnde(),
                    antrag.getProjekt().getFristBruchBegruendung());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).projekt());
        }
    }

    @Nested
    class Vertretungsberechtigter {
        @Test
        void testAntragDetailsVertretungsberechtigter() {
            final Antrag antrag = loadAntrag(antragBuilder.addVertretungsberechtigter());
            final AntragVertretungsberechtigterDTO expected = new AntragVertretungsberechtigterDTO(
                    antrag.getVertretungsberechtigter().getNachname(),
                    antrag.getVertretungsberechtigter().getVorname(),
                    antragMapper.adresseToAdresseDTO(antrag.getVertretungsberechtigter().getAdresse()),
                    antrag.getVertretungsberechtigter().getEmail(),
                    antrag.getVertretungsberechtigter().getTelefonNr(),
                    antrag.getVertretungsberechtigter().getMobilNr());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).vertretungsberechtigter());
        }

        @Test
        void testAntragDetailsVertretungsberechtigterNull() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragVertretungsberechtigterDTO expected = null;
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).vertretungsberechtigter());
        }
    }

    @Nested
    class Verwendungsnachweis {
        @Test
        void testAntragDetailsVerwendungsnachweis() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragVerwendungsnachweisDTO expected = new AntragVerwendungsnachweisDTO(
                    antrag.getVerwendungsnachweis().getBetrag(),
                    antrag.getVerwendungsnachweis().getStatus(),
                    antrag.getVerwendungsnachweis().getPruefungBetrag(),
                    antrag.getVerwendungsnachweis().getBuchungsDatum(),
                    antrag.getVerwendungsnachweis().getSapEingangsdatum());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).verwendungsnachweis());
        }
    }

    @Nested
    class Zahlung {
        @Test
        void testAntragDetailsZahlung() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AntragZahlungDTO expected = new AntragZahlungDTO(
                    antrag.getZahlung().getAuszahlungBetrag(),
                    antrag.getZahlung().getAuszahlungDatum(),
                    antrag.getZahlung().getAnlageAv(),
                    antrag.getZahlung().getAnlageNr(),
                    antrag.getZahlung().getKreditor(),
                    antrag.getZahlung().getRechnungNr(),
                    antrag.getZahlung().getFiBelegNr(),
                    antrag.getZahlung().getBestellung());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).zahlung());
        }
    }

    @Nested
    class Adresse {
        @Test
        void testAdresse() {
            final Antrag antrag = loadAntrag(antragBuilder);
            final AdresseDTO expected = new AdresseDTO(
                    antrag.getAntragsteller().getAdresse().getStrasseHausnummer(),
                    antrag.getAntragsteller().getAdresse().getOrt(),
                    antrag.getAntragsteller().getAdresse().getPostleitzahl(),
                    antrag.getAntragsteller().getAdresse().getWeitereAngaben());
            assertEquals(expected, antragMapper.toDetailsDTO(antrag).antragsteller().adresse());
        }
    }
}
