package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt_;
import de.muenchen.stadtbezirksbudget.backend.common.InvalidSortPropertyException;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Maps external sort parameters to internal entity fields for Antrag.
 */
@Component
public class AntragSortMapper {
    private static final Map<String, String> SORT_MAPPING = Map.ofEntries(
            Map.entry("status", path(Antrag_.bearbeitungsstand, Bearbeitungsstand_.status)),
            Map.entry("zammadNr", path(Antrag_.zammadTicketNr)),
            Map.entry("aktenzeichen", path(Antrag_.aktenzeichen)),
            Map.entry("bezirksausschussNr", path(Antrag_.bezirksausschussNr)),
            Map.entry("eingangDatum", path(Antrag_.eingangDatum)),
            Map.entry("antragstellerName", path(Antrag_.antragsteller, Antragsteller_.name)),
            Map.entry("projektTitel", path(Antrag_.projekt, Projekt_.titel)),
            Map.entry("beantragtesBudget", path(Antrag_.finanzierung, Finanzierung_.beantragtesBudget)),
            Map.entry("finanzierungArt", path(Antrag_.finanzierung, Finanzierung_.art)),
            Map.entry("aktualisierung", path(Antrag_.aktualisierungArt)),
            Map.entry("aktualisierungDatum", path(Antrag_.aktualisierungDatum)));

    private static String path(final SingularAttribute<?, ?>... attributes) {
        return Arrays.stream(attributes)
                .map(SingularAttribute::getName)
                .collect(Collectors.joining("."));
    }

    /**
     * Maps external sort parameters to internal entity fields.
     *
     * @param sortBy the external sort parameter
     * @param sortDirection the direction of sorting
     * @return a Sort object for Spring Data queries
     */
    public Sort map(final String sortBy, final Sort.Direction sortDirection) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.unsorted();
        }
        final String internalField = SORT_MAPPING.get(sortBy);
        if (internalField == null) {
            throw new InvalidSortPropertyException(sortBy);
        }
        return Sort.by(sortDirection, internalField);
    }
}
