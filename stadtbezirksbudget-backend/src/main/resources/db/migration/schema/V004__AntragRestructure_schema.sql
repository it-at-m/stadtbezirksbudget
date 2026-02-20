ALTER TABLE antrag
    RENAME COLUMN bezirksausschuss_nr TO bezirksinformationen_ausschuss_nr;

ALTER TABLE finanzierung
    ADD COLUMN ist_person_vorsteuerabzugsberechtigt BOOLEAN NOT NULL;

UPDATE finanzierung
    SET ist_person_vorsteuerabzugsberechtigt = antrag.ist_person_vorsteuerabzugsberechtigt FROM antrag WHERE finanzierung.id = antrag.finanzierung_id;

ALTER TABLE antrag
    DROP COLUMN ist_person_vorsteuerabzugsberechtigt;

ALTER TABLE antrag
    ADD COLUMN antragsteller_vorname VARCHAR(256) NOT NULL default '';