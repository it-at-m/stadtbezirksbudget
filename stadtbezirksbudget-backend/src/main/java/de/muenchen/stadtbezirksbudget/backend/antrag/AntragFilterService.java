package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AntragFilterService {

    public Predicate filterIssues(final AntragFilterDTO filter, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(
                filterBezirksausschussNr(filter.bezirksausschussNr(), root, criteriaBuilder),
                filterStatus(filter.status(), root, criteriaBuilder),
                filterEingangsdatumVon(filter.eingangDatumVon(), root, criteriaBuilder),
                filterEingangsdatumBis(filter.eingangDatumBis(), root, criteriaBuilder),
                filterAntragstellerName(filter.antragstellerName(), root, criteriaBuilder),
                filterProjektTitel(filter.projektTitel(), root, criteriaBuilder),
                filterBeantragtesBudgetVon(filter.beantragtesBudgetVon(), root, criteriaBuilder),
                filterBeantragtesBudgetBis(filter.beantragtesBudgetBis(), root, criteriaBuilder),
                filterIstFehlbetrag(filter.istFehlbetrag(), root, criteriaBuilder),
                filterAktualisierungArt(filter.aktualisierungArt(), root, criteriaBuilder),
                filterAktualisierungDatumVon(filter.aktualisierungDatumVon(), root, criteriaBuilder),
                filterAktualisierungDatumBis(filter.aktualisierungDatumBis(), root, criteriaBuilder));
    }

    private Predicate filterStatus(final List<Status> statusList, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (statusList != null && !statusList.isEmpty())
                ? root.get("bearbeitungsstand").get("status").in(statusList)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterBezirksausschussNr(final List<Integer> bezirksausschussNrList, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (bezirksausschussNrList != null && !bezirksausschussNrList.isEmpty())
                ? root.get("bezirksausschussNr").in(bezirksausschussNrList)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterEingangsdatumVon(final LocalDateTime vonDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (vonDatum != null)
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("eingangDatum"), vonDatum)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterEingangsdatumBis(final LocalDateTime bisDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (bisDatum != null)
                ? criteriaBuilder.lessThanOrEqualTo(root.get("eingangDatum"), bisDatum)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterAntragstellerName(final String antragstellerName, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (antragstellerName != null && !antragstellerName.isEmpty())
                ? criteriaBuilder.equal(root.get("antragsteller").get("name"), "%" + antragstellerName + "%")
                : criteriaBuilder.conjunction();
    }

    private Predicate filterProjektTitel(final String projektTitel, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (projektTitel != null && !projektTitel.isEmpty())
                ? criteriaBuilder.equal(root.get("projekt").get("titel"), "%" + projektTitel + "%")
                : criteriaBuilder.conjunction();
    }

    private Predicate filterBeantragtesBudgetVon(final BigDecimal beantragtesBudgetVon, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (beantragtesBudgetVon != null)
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("finanzierung").get("beantragtesBudget"), beantragtesBudgetVon)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterBeantragtesBudgetBis(final BigDecimal beantragtesBudgetBis, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (beantragtesBudgetBis != null)
                ? criteriaBuilder.lessThanOrEqualTo(root.get("finanzierung").get("beantragtesBudget"), beantragtesBudgetBis)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterIstFehlbetrag(final Boolean istFehlbetrag, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (istFehlbetrag != null)
                ? criteriaBuilder.equal(root.get("finanzierung").get("istFehlbetrag"), istFehlbetrag)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterAktualisierungArt(final List<AktualisierungArt> aktualisierungArtList, final Root<Antrag> root,
            final CriteriaBuilder criteriaBuilder) {
        return (aktualisierungArtList != null && !aktualisierungArtList.isEmpty())
                ? root.get("aktualisierungArt").in(aktualisierungArtList)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterAktualisierungDatumVon(final LocalDateTime vonDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (vonDatum != null)
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("aktualisierungDatum"), vonDatum)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterAktualisierungDatumBis(final LocalDateTime bisDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (bisDatum != null)
                ? criteriaBuilder.lessThanOrEqualTo(root.get("aktualisierungDatum"), bisDatum)
                : criteriaBuilder.conjunction();
    }
}
