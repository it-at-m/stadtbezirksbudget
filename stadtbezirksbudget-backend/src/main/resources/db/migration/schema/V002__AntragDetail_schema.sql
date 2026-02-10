ALTER TABLE antrag
    ADD zahlung_auszahlung_betrag DECIMAL(19, 2),
    ADD zahlung_auszahlung_datum  date,
    ADD zahlung_anlageav           VARCHAR(256) NOT NULL default '',
    ADD zahlung_anlage_nr          VARCHAR(256) NOT NULL default '',
    ADD zahlung_kreditor           VARCHAR(256) NOT NULL default '',
    ADD zahlung_rechnung_nr       VARCHAR(256) NOT NULL default '',
    ADD zahlung_fi_beleg_nr        VARCHAR(256) NOT NULL default '',
    ADD zahlung_bestellung         VARCHAR(256) NOT NULL default '';

ALTER TABLE antrag
    ADD verwendungsnachweis_betrag DECIMAL(19, 2),
    ADD verwendungsnachweis_status          VARCHAR(256) NOT NULL default '',
    ADD verwendungsnachweis_pruefung_betrag DECIMAL(19, 2),
    ADD verwendungsnachweis_buchungs_datum     date,
    ADD verwendungsnachweis_sap_eingangsdatum  date;

ALTER TABLE antrag
    ADD bezirksinformationen_sitzung_datum date,
    ADD bezirksinformationen_ris_nr                VARCHAR(256) NOT NULL default '',
    ADD bezirksinformationen_bewilligte_foerderung DECIMAL(19, 2),
    ADD bezirksinformationen_bescheid_datum        date;

ALTER TABLE antrag
    ADD projekt_rubrik VARCHAR(256) NOT NULL default '',
    ADD ist_gegendert      BOOLEAN      NOT NULL default false;