package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FinanzierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class AntragFilterService {
    /**
     * Builds a JPA Criteria Predicate that combines all non-null/non-empty filters from the provided
     * DTO.
     * Each filter is combined with AND logic. Null or empty filter values are treated as "no filter"
     * (returning conjunction/true).
     *
     * @param filter the filter DTO containing filter criteria; must not be null
     * @param root the JPA Criteria root for Antrag entity
     * @param criteriaBuilder the JPA Criteria builder
     * @return a Predicate combining all active filters with AND logic
     */
    public Predicate filterAntrag(@NotNull final AntragFilterDTO filter, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(
                filterSearch(filter.search(), root, criteriaBuilder),
                filterBezirksausschussNr(filter.bezirksausschussNr(), root, criteriaBuilder),
                filterStatus(filter.status(), root, criteriaBuilder),
                filterEingangsdatumVon(filter.eingangDatumVon(), root, criteriaBuilder),
                filterEingangsdatumBis(filter.eingangDatumBis(), root, criteriaBuilder),
                filterAntragstellerName(filter.antragstellerName(), root, criteriaBuilder),
                filterProjektTitel(filter.projektTitel(), root, criteriaBuilder),
                filterBeantragtesBudgetVon(filter.beantragtesBudgetVon(), root, criteriaBuilder),
                filterBeantragtesBudgetBis(filter.beantragtesBudgetBis(), root, criteriaBuilder),
                filterFinanzierungArt(filter.finanzierungArt(), root, criteriaBuilder),
                filterAktualisierungArt(filter.aktualisierungArt(), root, criteriaBuilder),
                filterAktualisierungDatumVon(filter.aktualisierungDatumVon(), root, criteriaBuilder),
                filterAktualisierungDatumBis(filter.aktualisierungDatumBis(), root, criteriaBuilder));
    }

    private Predicate filterSearch(final String search, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        if (search == null || search.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        final String escapedSearch = search.toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        final String likePattern = "%" + escapedSearch + "%";
        return criteriaBuilder.or(AntragFieldProvider.getSearchFields(root)
                .map(field -> criteriaBuilder.like(criteriaBuilder.lower(field), likePattern, '\\'))
                .toArray(Predicate[]::new));
    }

    private Predicate filterStatus(final List<Status> statusList, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (statusList != null && !statusList.isEmpty())
                ? root.get(Antrag_.bearbeitungsstand).get(Bearbeitungsstand_.status).in(statusList)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterBezirksausschussNr(final List<Integer> bezirksausschussNrList, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (bezirksausschussNrList != null && !bezirksausschussNrList.isEmpty())
                ? root.get(Antrag_.bezirksausschussNr).in(bezirksausschussNrList)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterDatumVon(final LocalDateTime vonDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder,
            final SingularAttribute<Antrag, LocalDateTime> attribute) {
        return (vonDatum != null)
                ? criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), vonDatum)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterDatumBis(final LocalDateTime bisDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder,
            final SingularAttribute<Antrag, LocalDateTime> attribute) {
        return (bisDatum != null)
                ? criteriaBuilder.lessThanOrEqualTo(root.get(attribute), bisDatum)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterEingangsdatumVon(final LocalDateTime vonDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return filterDatumVon(vonDatum, root, criteriaBuilder, Antrag_.eingangDatum);
    }

    private Predicate filterEingangsdatumBis(final LocalDateTime bisDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return filterDatumBis(bisDatum, root, criteriaBuilder, Antrag_.eingangDatum);
    }

    private Predicate filterAktualisierungDatumVon(final LocalDateTime vonDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return filterDatumVon(vonDatum, root, criteriaBuilder, Antrag_.aktualisierungDatum);
    }

    private Predicate filterAktualisierungDatumBis(final LocalDateTime bisDatum, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return filterDatumBis(bisDatum, root, criteriaBuilder, Antrag_.aktualisierungDatum);
    }

    private Predicate filterAntragstellerName(final String antragstellerName, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (antragstellerName != null && !antragstellerName.isEmpty())
                ? criteriaBuilder.equal(root.get(Antrag_.antragsteller).get(Antragsteller_.name), antragstellerName)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterProjektTitel(final String projektTitel, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (projektTitel != null && !projektTitel.isEmpty())
                ? criteriaBuilder.equal(root.get(Antrag_.projekt).get(Projekt_.titel), projektTitel)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterBeantragtesBudgetVon(final BigDecimal beantragtesBudgetVon, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (beantragtesBudgetVon != null)
                ? criteriaBuilder.greaterThanOrEqualTo(root.get(Antrag_.finanzierung).get(Finanzierung_.beantragtesBudget), beantragtesBudgetVon)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterBeantragtesBudgetBis(final BigDecimal beantragtesBudgetBis, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (beantragtesBudgetBis != null)
                ? criteriaBuilder.lessThanOrEqualTo(root.get(Antrag_.finanzierung).get(Finanzierung_.beantragtesBudget), beantragtesBudgetBis)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterFinanzierungArt(final FinanzierungArt finanzierungArt, final Root<Antrag> root, final CriteriaBuilder criteriaBuilder) {
        return (finanzierungArt != null)
                ? criteriaBuilder.equal(root.get(Antrag_.finanzierung).get(Finanzierung_.art), finanzierungArt)
                : criteriaBuilder.conjunction();
    }

    private Predicate filterAktualisierungArt(final List<AktualisierungArt> aktualisierungArtList, final Root<Antrag> root,
            final CriteriaBuilder criteriaBuilder) {
        return (aktualisierungArtList != null && !aktualisierungArtList.isEmpty())
                ? root.get(Antrag_.aktualisierungArt).in(aktualisierungArtList)
                : criteriaBuilder.conjunction();
    }
}