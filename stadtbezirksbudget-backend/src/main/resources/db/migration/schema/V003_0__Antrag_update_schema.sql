ALTER TABLE antrag
    RENAME COLUMN eingangsdatum to eingang_datum;

ALTER TABLE antrag
    ADD aktenzeichen VARCHAR(255),
    ADD aktualisierung_art VARCHAR(255),
    ADD aktualisierung_datum TIMESTAMP WITHOUT TIME ZONE,
    ADD zammad_ticket_nr VARCHAR(255),
    ALTER COLUMN eingang_datum type TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE finanzierung
    ADD beantragtes_budget DECIMAL(19,2),
    ADD begruendung_eigenmittel VARCHAR(255),
    ADD kosten_anmerkung VARCHAR(255),
    ADD summe_ausgaben DECIMAL(19,2),
    ADD summe_finanzierungsmittel DECIMAL(19,2),
    ALTER COLUMN bewilligter_zuschuss type DECIMAL(19,2);

ALTER TABLE finanzierung
    RENAME COLUMN ist_einladungs_foerderhinweis to ist_einladung_foerderhinweis;

ALTER TABLE projekt
    ADD frist_bruch_begruendung VARCHAR(255),
    ADD ist_start_frist BOOLEAN;

ALTER TABLE finanzierungsmittel
    ALTER COLUMN betrag type DECIMAL(19,2);

ALTER TABLE voraussichtliche_ausgabe
    ALTER COLUMN betrag type DECIMAL(19,2);
