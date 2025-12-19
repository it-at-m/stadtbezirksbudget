package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bankverbindung_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Vertretungsberechtigter;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Vertretungsberechtigter_;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import java.util.stream.Stream;

/**
 * A record that lists the search fields for the {@link Antrag} entity.
 *
 * @param root The root entity representing the Antrag.
 */
public record AntragFieldProvider(@NotNull Root<Antrag> root) {

    /**
     * Returns a stream of search fields for the {@link Antrag} entity.
     *
     * @param root The root entity representing the Antrag.
     * @return a stream of search fields.
     */
    public static Stream<Expression<String>> getSearchFields(@NotNull final Root<Antrag> root) {
        return new AntragFieldProvider(root).getSearchFields();
    }

    /**
     * Returns a stream of search fields for the {@link Antrag} entity.
     *
     * @return a stream of search fields.
     */
    public Stream<Expression<String>> getSearchFields() {
        return Stream.of(
                root.get(Antrag_.zammadTicketNr),
                root.get(Antrag_.aktenzeichen),
                root.get(Antrag_.bearbeitungsstand).get(Bearbeitungsstand_.anmerkungen),
                root.get(Antrag_.finanzierung).get(Finanzierung_.kostenAnmerkung),
                root.get(Antrag_.finanzierung).get(Finanzierung_.begruendungEigenmittel),
                root.get(Antrag_.finanzierung).get(Finanzierung_.sonstigeFoerderhinweise),
                root.get(Antrag_.projekt).get(Projekt_.titel),
                root.get(Antrag_.projekt).get(Projekt_.fristBruchBegruendung),
                root.get(Antrag_.projekt).get(Projekt_.beschreibung),
                root.get(Antrag_.antragsteller).get(Antragsteller_.name),
                root.get(Antrag_.antragsteller).get(Antragsteller_.zielsetzung),
                root.get(Antrag_.antragsteller).get(Antragsteller_.telefonNr),
                root.get(Antrag_.antragsteller).get(Antragsteller_.email),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.strasse),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.hausnummer),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.ort),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.postleitzahl),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.person),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.geldinstitut),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.iban),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.bic),
                vertretungsberechtigter().get(Vertretungsberechtigter_.nachname),
                vertretungsberechtigter().get(Vertretungsberechtigter_.vorname),
                vertretungsberechtigter().get(Vertretungsberechtigter_.mobilNr),
                vertretungsberechtigter().get(Vertretungsberechtigter_.telefonNr),
                vertretungsberechtigter().get(Vertretungsberechtigter_.email),
                vertretungsberechtigterAdresse().get(Adresse_.strasse),
                vertretungsberechtigterAdresse().get(Adresse_.hausnummer),
                vertretungsberechtigterAdresse().get(Adresse_.ort),
                vertretungsberechtigterAdresse().get(Adresse_.postleitzahl));
    }

    private Join<Antrag, Vertretungsberechtigter> vertretungsberechtigter() {
        return root.join(Antrag_.vertretungsberechtigter, JoinType.LEFT);
    }

    private Join<Vertretungsberechtigter, Adresse> vertretungsberechtigterAdresse() {
        return vertretungsberechtigter().join(Vertretungsberechtigter_.adresse, JoinType.LEFT);
    }
}
