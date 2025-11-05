ALTER TABLE antrag
    ALTER COLUMN aktenzeichen SET NOT NULL;

ALTER TABLE antrag
    ALTER COLUMN aktualisierung_art SET NOT NULL;

ALTER TABLE antrag
    ALTER COLUMN aktualisierung_datum SET NOT NULL;

ALTER TABLE finanzierung
    ALTER COLUMN beantragtes_budget SET NOT NULL;

ALTER TABLE finanzierung
    ALTER COLUMN begruendung_eigenmittel SET NOT NULL;

ALTER TABLE antrag
    ALTER COLUMN eingang_datum SET NOT NULL;

ALTER TABLE projekt
    ALTER COLUMN frist_bruch_begruendung SET NOT NULL;

ALTER TABLE finanzierung
    ALTER COLUMN ist_einladung_foerderhinweis SET NOT NULL;

ALTER TABLE projekt
    ALTER COLUMN ist_start_frist SET NOT NULL;

ALTER TABLE finanzierung
    ALTER COLUMN kosten_anmerkung SET NOT NULL;

ALTER TABLE finanzierung
    ALTER COLUMN summe_ausgaben SET NOT NULL;

ALTER TABLE finanzierung
    ALTER COLUMN summe_finanzierungsmittel SET NOT NULL;

ALTER TABLE antrag
    ALTER COLUMN zammad_ticket_nr SET NOT NULL;

ALTER TABLE finanzierungsmittel
    ALTER COLUMN betrag SET NOT NULL;

ALTER TABLE voraussichtliche_ausgabe
    ALTER COLUMN betrag SET NOT NULL;
