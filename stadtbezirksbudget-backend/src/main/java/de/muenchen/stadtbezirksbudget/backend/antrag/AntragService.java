package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FilterOptionsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantragStatus;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bankverbindung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Rechtsform;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Vertretungsberechtigter;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.common.NameView;
import de.muenchen.stadtbezirksbudget.backend.common.NotFoundException;
import de.muenchen.stadtbezirksbudget.backend.common.TitelView;
import de.muenchen.stadtbezirksbudget.backend.kafka.KafkaDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service to handle business logic related to Antrag entities.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AntragService {
    private final AntragRepository antragRepository;
    private final AntragFilterService antragFilterService;
    private final FinanzierungRepository finanzierungRepository;

    /**
     * Retrieves a paginated filtered list of Antrag entities.
     *
     * @param pageable pagination information
     * @param antragFilterDTO filter information
     * @return a page of Antrag entities
     */
    public Page<Antrag> getAntragPage(final Pageable pageable, final AntragFilterDTO antragFilterDTO) {
        log.info("Get antrag page with pageable {} and filterDTO {}", pageable, antragFilterDTO);
        return antragRepository.findAll(
                (root, query, criteriaBuilder) -> antragFilterService.filterAntrag(antragFilterDTO, root, criteriaBuilder),
                pageable);
    }

    /**
     * Retrieves a single Antrag by its id.
     *
     * @param id id of the Antrag to get
     * @return the antrag with the id
     * @throws NotFoundException if no Antrag was found for the given id
     */
    public Antrag getAntrag(final UUID id) {
        log.info("Get antrag with id {}", id);
        return antragRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Could not find antrag with id %s", id)));
    }

    public Antrag createFromKafka(UUID id, KafkaDTO kafkaDTO) {
        Antrag antrag = antragRepository.findById(id).orElseGet(() -> {
            Antrag newAntrag = new Antrag();
            newAntrag.setId(id);
            return newAntrag;
        });

        antrag.setBezirksausschussNr(kafkaDTO.bezirksausschussNr());

        antrag.setEingangDatum(LocalDateTime.of(2026, 2, 5, 15, 30));

        antrag.setIstPersonVorsteuerabzugsberechtigt(false);

        antrag.setZammadTicketNr("12345");

        antrag.setAktualisierungDatum(LocalDateTime.of(2026, 2, 5, 15, 30));

        antrag.setAktenzeichen("2026-00-00/01234");

        antrag.setAktualisierungArt(AktualisierungArt.FACHANWENDUNG);

        Bearbeitungsstand musterBearbeitungsstand = new Bearbeitungsstand();
        musterBearbeitungsstand.setAnmerkungen("Muster Anmerkung");
        musterBearbeitungsstand.setIstMittelabruf(false);
        musterBearbeitungsstand.setStatus(Status.EINGEGANGEN);
        antrag.setBearbeitungsstand(musterBearbeitungsstand);

        VoraussichtlicheAusgabe musterAusgabe = VoraussichtlicheAusgabe.builder()
                .kategorie("Muster Kategorie")
                .betrag(new BigDecimal(5000))
                .direktoriumNotiz("Muster Notiz")
                .build();
        Finanzierungsmittel musterFinanzierungsmittel = Finanzierungsmittel.builder()
                .kategorie(Kategorie.EIGENMITTEL)
                .betrag(new BigDecimal(3000))
                .direktoriumNotiz("Muster Notiz")
                .build();

        Finanzierung musterFinanzierung = new Finanzierung();
        musterFinanzierung.setIstProjektVorsteuerabzugsberechtigt(false);
        musterFinanzierung.setKostenAnmerkung("Muster Anmerkung");
        musterFinanzierung.setBegruendungEigenmittel("Muster Begründung");
        musterFinanzierung.setBeantragtesBudget(new BigDecimal("1500.00"));
        musterFinanzierung.setIstEinladungFoerderhinweis(true);
        musterFinanzierung.setIstWebsiteFoerderhinweis(true);
        musterFinanzierung.setIstSonstigerFoerderhinweis(true);
        musterFinanzierung.setSonstigeFoerderhinweise("Muster Förderhinweis");
        musterFinanzierung.setBewilligterZuschuss(new BigDecimal("1500.00"));
        musterFinanzierung.setVoraussichtlicheAusgaben(List.of(musterAusgabe));
        musterFinanzierung.setFinanzierungsmittel(List.of(musterFinanzierungsmittel));

        Finanzierung savedFinanzierung = finanzierungRepository.save(musterFinanzierung);
        
        antrag.setFinanzierung(savedFinanzierung);

        Projekt musterProjekt = new Projekt();
        musterProjekt.setTitel("Muster Projekt");
        musterProjekt.setStart(LocalDate.of(2026, 5, 5));
        musterProjekt.setFristBruchBegruendung("Muster Begründung");
        musterProjekt.setEnde(LocalDate.of(2026, 5, 5));
        musterProjekt.setBeschreibung("Muster Beschreibung");
        antrag.setProjekt(musterProjekt);

        Adresse musterAdresse = new Adresse();
        musterAdresse.setStrasseHausnummer("12");
        musterAdresse.setOrt("Musterstadt");
        musterAdresse.setPostleitzahl("12345");
        musterAdresse.setWeitereAngaben("Etage 4");

        Antragsteller fkAntragsteller = new Antragsteller();
        fkAntragsteller.setName(kafkaDTO.nameAntragsteller());
        fkAntragsteller.setEmail("max.mustermann@muster.de");
        fkAntragsteller.setTelefonNr("01234567");
        fkAntragsteller.setRechtsform(Rechtsform.NATUERLICHE_PERSON);
        fkAntragsteller.setZielsetzung("Muster Zielsetzung");
        fkAntragsteller.setAdresse(musterAdresse);
        antrag.setAntragsteller(fkAntragsteller);

        Bankverbindung musterBankverbindung = new Bankverbindung();
        musterBankverbindung.setIstVonVertretungsberechtigtem(false);
        musterBankverbindung.setGeldinstitut(kafkaDTO.geldinstitut());
        musterBankverbindung.setIban("DE123456789101112131415");
        musterBankverbindung.setBic("STABUMUEXXX");
        antrag.setBankverbindung(musterBankverbindung);

        Vertretungsberechtigter musterVertretungsberechtigter = new Vertretungsberechtigter();
        musterVertretungsberechtigter.setNachname("Musterfrau");
        musterVertretungsberechtigter.setVorname("Erika");
        musterVertretungsberechtigter.setTelefonNr("01234567");
        musterVertretungsberechtigter.setEmail("erika.musterfrau@muster.de");
        musterVertretungsberechtigter.setAdresse(musterAdresse);
        musterVertretungsberechtigter.setMobilNr("01234567");
        antrag.setVertretungsberechtigter(musterVertretungsberechtigter);

        AndererZuwendungsantrag musterAndererZuwendungsantrag = AndererZuwendungsantrag.builder()
                .antragsdatum(LocalDate.of(2026, 1, 5))
                .stelle("KULT")
                .betrag(new BigDecimal(2000))
                .status(AndererZuwendungsantragStatus.BEANTRAGT)
                .build();

        antrag.setAndereZuwendungsantraege(List.of(musterAndererZuwendungsantrag));

        return antragRepository.save(antrag);
    }

    /**
     * Retrieves all Antragsteller names and Projekt titles.
     *
     * @return a FilterOptionsDTO containing lists of Antragsteller names and Projekt titles
     */
    public FilterOptionsDTO getFilterOptions() {
        log.info("Get FilterOptions");
        final List<String> antragstellerNameList = antragRepository.findDistinctByAntragsteller_nameIsNotNullOrderByAntragsteller_nameAsc().stream()
                .map(NameView::getAntragsteller_Name).toList();
        final List<String> projektTitelList = antragRepository.findDistinctByProjekt_titelIsNotNullOrderByProjekt_titelAsc().stream()
                .map(TitelView::getProjekt_Titel).toList();
        return new FilterOptionsDTO(antragstellerNameList, projektTitelList);
    }

    /**
     * Updates the status of an Antrag.
     *
     * @param id the ID of the Antrag to update
     * @param statusUpdateDTO the DTO containing the new status
     */
    public void updateAntragStatus(final UUID id, final AntragStatusUpdateDTO statusUpdateDTO) {
        log.info("Update status of antrag with id {} to {}", id, statusUpdateDTO.status());
        final Antrag antrag = antragRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Antrag with id " + id + " not found"));
        antrag.getBearbeitungsstand().setStatus(statusUpdateDTO.status());
        antragRepository.save(antrag);
    }

}
