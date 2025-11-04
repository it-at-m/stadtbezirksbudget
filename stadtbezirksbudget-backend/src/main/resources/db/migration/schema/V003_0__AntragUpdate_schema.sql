ALTER TABLE antrag
    ADD aktenzeichen VARCHAR(255);

ALTER TABLE antrag
    ADD aktualisierung_art VARCHAR(255);

ALTER TABLE antrag
    ADD aktualisierung_datum TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE antrag
    RENAME COLUMN eingangsdatum to eingang_datum;

ALTER TABLE antrag
    ALTER COLUMN eingang_datum type TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE antrag
    ADD zammad_ticket_nr VARCHAR(255);

ALTER TABLE finanzierung
    ADD beantragtes_budget DECIMAL(19,2);

ALTER TABLE finanzierung
    ADD begruendung_eigenmittel VARCHAR(255);

ALTER TABLE finanzierung
    ADD kosten_anmerkung VARCHAR(255);

ALTER TABLE finanzierung
    ADD summe_ausgaben DECIMAL(19,2);

ALTER TABLE finanzierung
    ADD summe_finanzierungsmittel DECIMAL(19,2);

ALTER TABLE projekt
    ADD frist_bruch_begruendung VARCHAR(255);

ALTER TABLE projekt
    ADD ist_start_frist BOOLEAN;

ALTER TABLE finanzierung
    RENAME COLUMN ist_einladungs_foerderhinweis to ist_einladung_foerderhinweis;

ALTER TABLE finanzierungsmittel
    ALTER COLUMN betrag type DECIMAL(19,2);

ALTER TABLE voraussichtliche_ausgabe
    ALTER COLUMN betrag type DECIMAL(19,2);

ALTER TABLE finanzierung
    ALTER COLUMN bewilligter_zuschuss type DECIMAL(19,2);
