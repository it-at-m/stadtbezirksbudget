CREATE TABLE anderer_zuwendungsantrag
(
    id           UUID         NOT NULL,
    antragsdatum date         NOT NULL,
    stelle       VARCHAR(256) NOT NULL CHECK (anderer_zuwendungsantrag.stelle <> ''),
    antrag_id    UUID         NOT NULL,
    CONSTRAINT pk_andererzuwendungsantrag PRIMARY KEY (id)
);

CREATE TABLE antrag
(
    id                                                 UUID                        NOT NULL,
    bezirksausschuss_nr                                INTEGER                     NOT NULL,
    eingang_datum                                      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ist_person_vorsteuerabzugsberechtigt               BOOLEAN                     NOT NULL,
    ist_anderer_zuwendungsantrag                       BOOLEAN                     NOT NULL,
    zammad_ticket_nr                                   VARCHAR(256)                NOT NULL,
    aktualisierung_datum                               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    aktenzeichen                                       VARCHAR(256)                NOT NULL,
    aktualisierung_art                                 VARCHAR(256)                NOT NULL CHECK (antrag.aktualisierung_art <> ''),
    bearbeitungsstand_anmerkungen                      VARCHAR(256)                NOT NULL,
    bearbeitungsstand_ist_mittelabruf                  BOOLEAN                     NOT NULL,
    bearbeitungsstand_status                           VARCHAR(256)                NOT NULL CHECK (antrag.bearbeitungsstand_status <> ''),
    finanzierung_ist_projekt_vorsteuerabzugsberechtigt BOOLEAN                     NOT NULL,
    finanzierung_kosten_anmerkung                      VARCHAR(256)                NOT NULL,
    finanzierung_summe_ausgaben                        DECIMAL(19, 2)              NOT NULL,
    finanzierung_summe_finanzierungsmittel             DECIMAL(19, 2)              NOT NULL,
    finanzierung_begruendung_eigenmittel               VARCHAR(256),
    finanzierung_beantragtes_budget                    DECIMAL(19, 2)              NOT NULL,
    finanzierung_ist_einladung_foerderhinweis          BOOLEAN                     NOT NULL,
    finanzierung_ist_website_foerderhinweis            BOOLEAN                     NOT NULL,
    finanzierung_ist_sonstiger_foerderhinweis          BOOLEAN                     NOT NULL,
    finanzierung_sonstige_foerderhinweise              VARCHAR(256)                NOT NULL,
    finanzierung_bewilligter_zuschuss                  DECIMAL(19, 2),
    projekt_titel                                      VARCHAR(256)                NOT NULL CHECK (antrag.projekt_titel <> ''),
    projekt_start                                      date                        NOT NULL,
    projekt_ist_start_frist                            BOOLEAN                     NOT NULL,
    projekt_frist_bruch_begruendung                    VARCHAR(256)                NOT NULL,
    projekt_ende                                       date                        NOT NULL,
    projekt_beschreibung                               VARCHAR(256)                NOT NULL CHECK (antrag.projekt_beschreibung <> ''),
    antragsteller_name                                 VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_name <> ''),
    antragsteller_telefon_nr                           VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_telefon_nr <> ''),
    antragsteller_email                                VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_email <> ''),
    antragsteller_rechtsform                           VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_rechtsform <> ''),
    antragsteller_zielsetzung                          VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_zielsetzung <> ''),
    antragsteller_adresse_strasse                      VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_adresse_strasse <> ''),
    antragsteller_adresse_hausnummer                   VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_adresse_hausnummer <> ''),
    antragsteller_adresse_ort                          VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_adresse_ort <> ''),
    antragsteller_adresse_postleitzahl                 VARCHAR(256)                NOT NULL CHECK (antrag.antragsteller_adresse_postleitzahl <> ''),
    bankverbindung_person                              VARCHAR(256)                NOT NULL CHECK (antrag.bankverbindung_person <> ''),
    bankverbindung_geldinstitut                        VARCHAR(256)                NOT NULL CHECK (antrag.bankverbindung_geldinstitut <> ''),
    bankverbindung_iban                                VARCHAR(256)                NOT NULL CHECK (antrag.bankverbindung_iban <> ''),
    bankverbindung_bic                                 VARCHAR(256)                NOT NULL CHECK (antrag.bankverbindung_bic <> ''),
    vertretungsberechtigter_nachname                   VARCHAR(256),
    vertretungsberechtigter_vorname                    VARCHAR(256),
    vertretungsberechtigter_mobil_nr                   VARCHAR(256),
    vertretungsberechtigter_telefon_nr                 VARCHAR(256),
    vertretungsberechtigter_email                      VARCHAR(256),
    vertretungsberechtigter_adresse_strasse            VARCHAR(256),
    vertretungsberechtigter_adresse_hausnummer         VARCHAR(256),
    vertretungsberechtigter_adresse_ort                VARCHAR(256),
    vertretungsberechtigter_adresse_postleitzahl       VARCHAR(256),
    CONSTRAINT pk_antrag PRIMARY KEY (id)
);

CREATE TABLE finanzierungsmittel
(
    id                UUID           NOT NULL,
    kategorie         VARCHAR(256)   NOT NULL CHECK (finanzierungsmittel.kategorie <> ''),
    betrag            DECIMAL(19, 2) NOT NULL,
    direktorium_notiz VARCHAR(256)   NOT NULL,
    antrag_id         UUID           NOT NULL,
    CONSTRAINT pk_finanzierungsmittel PRIMARY KEY (id)
);

CREATE TABLE voraussichtliche_ausgabe
(
    id                UUID           NOT NULL,
    kategorie         VARCHAR(256)   NOT NULL CHECK (voraussichtliche_ausgabe.kategorie <> ''),
    betrag            DECIMAL(19, 2) NOT NULL,
    direktorium_notiz VARCHAR(256)   NOT NULL,
    antrag_id         UUID           NOT NULL,
    CONSTRAINT pk_voraussichtlicheausgabe PRIMARY KEY (id)
);

ALTER TABLE anderer_zuwendungsantrag
    ADD CONSTRAINT FK_ANDERERZUWENDUNGSANTRAG_ON_ANTRAG FOREIGN KEY (antrag_id) REFERENCES antrag (id);

ALTER TABLE finanzierungsmittel
    ADD CONSTRAINT FK_FINANZIERUNGSMITTEL_ON_ANTRAG FOREIGN KEY (antrag_id) REFERENCES antrag (id);

ALTER TABLE voraussichtliche_ausgabe
    ADD CONSTRAINT FK_VORAUSSICHTLICHEAUSGABE_ON_ANTRAG FOREIGN KEY (antrag_id) REFERENCES antrag (id);