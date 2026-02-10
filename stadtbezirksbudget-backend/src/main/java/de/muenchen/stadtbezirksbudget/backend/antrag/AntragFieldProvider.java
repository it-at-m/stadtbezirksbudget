package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bankverbindung_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bezirksinformationen_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Vertretungsberechtigter;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Vertretungsberechtigter_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Verwendungsnachweis_;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Zahlung_;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import java.util.stream.Stream;

/**
 * A provider that lists the search fields for the {@link Antrag} entity.
 */
public final class AntragFieldProvider {
    private final Root<Antrag> root;
    private final Join<Antrag, Vertretungsberechtigter> vertretungsberechtigter;
    private final Join<Vertretungsberechtigter, Adresse> vertretungsberechtigterAdresse;

    /**
     * Constructor for AntragFieldProvider.
     *
     * @param root The root entity representing the Antrag.
     */
    private AntragFieldProvider(final @NotNull Root<Antrag> root) {
        this.root = root;
        this.vertretungsberechtigter = vertretungsberechtigterJoin();
        this.vertretungsberechtigterAdresse = vertretungsberechtigterAdresseJoin();
    }

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
                root.get(Antrag_.projekt).get(Projekt_.rubrik),
                root.get(Antrag_.antragsteller).get(Antragsteller_.name),
                root.get(Antrag_.antragsteller).get(Antragsteller_.zielsetzung),
                root.get(Antrag_.antragsteller).get(Antragsteller_.telefonNr),
                root.get(Antrag_.antragsteller).get(Antragsteller_.email),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.strasseHausnummer),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.ort),
                root.get(Antrag_.antragsteller).get(Antragsteller_.adresse).get(Adresse_.postleitzahl),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.geldinstitut),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.iban),
                root.get(Antrag_.bankverbindung).get(Bankverbindung_.bic),
                root.get(Antrag_.bezirksinformationen).get(Bezirksinformationen_.risNr),
                root.get(Antrag_.zahlung).get(Zahlung_.anlageAV),
                root.get(Antrag_.zahlung).get(Zahlung_.anlageNr),
                root.get(Antrag_.zahlung).get(Zahlung_.bestellung),
                root.get(Antrag_.zahlung).get(Zahlung_.fiBelegNr),
                root.get(Antrag_.zahlung).get(Zahlung_.kreditor),
                root.get(Antrag_.zahlung).get(Zahlung_.rechnungNr),
                root.get(Antrag_.verwendungsnachweis).get(Verwendungsnachweis_.status),
                vertretungsberechtigter.get(Vertretungsberechtigter_.nachname),
                vertretungsberechtigter.get(Vertretungsberechtigter_.vorname),
                vertretungsberechtigter.get(Vertretungsberechtigter_.mobilNr),
                vertretungsberechtigter.get(Vertretungsberechtigter_.telefonNr),
                vertretungsberechtigter.get(Vertretungsberechtigter_.email),
                vertretungsberechtigterAdresse.get(Adresse_.strasseHausnummer),
                vertretungsberechtigterAdresse.get(Adresse_.ort),
                vertretungsberechtigterAdresse.get(Adresse_.postleitzahl));
    }

    private Join<Antrag, Vertretungsberechtigter> vertretungsberechtigterJoin() {
        return root.join(Antrag_.vertretungsberechtigter, JoinType.LEFT);
    }

    private Join<Vertretungsberechtigter, Adresse> vertretungsberechtigterAdresseJoin() {
        return vertretungsberechtigter.join(Vertretungsberechtigter_.adresse, JoinType.LEFT);
    }
}
