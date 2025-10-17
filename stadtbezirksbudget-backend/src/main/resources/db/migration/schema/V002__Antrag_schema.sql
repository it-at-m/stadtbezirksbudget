CREATE TABLE adresse
(
    id           UUID NOT NULL,
    strasse      VARCHAR(255),
    hausnummer   VARCHAR(255),
    ort          VARCHAR(255),
    postleitzahl VARCHAR(255),
    CONSTRAINT pk_adresse PRIMARY KEY (id)
);

CREATE TABLE anderer_zuwendungsantrag
(
    id           UUID NOT NULL,
    antragsdatum date NOT NULL,
    stelle       VARCHAR(255),
    antrag_id    UUID NOT NULL,
    CONSTRAINT pk_andererzuwendungsantrag PRIMARY KEY (id)
);

CREATE TABLE antrag
(
    id                                   UUID    NOT NULL,
    eingangsdatum                        date    NOT NULL,
    bezirksausschuss_nr                  INTEGER NOT NULL,
    ist_person_vorsteuerabzugsberechtigt BOOLEAN NOT NULL,
    ist_anderer_zuwendungsantrag         BOOLEAN NOT NULL,
    bearbeitungsstand_id                 UUID    NOT NULL,
    finanzierung_id                      UUID    NOT NULL,
    projekt_id                           UUID    NOT NULL,
    antragsteller_id                     UUID    NOT NULL,
    bankverbindung_id                    UUID    NOT NULL,
    vertretungsberechtigter_id           UUID,
    CONSTRAINT pk_antrag PRIMARY KEY (id)
);

CREATE TABLE bankverbindung
(
    id                    UUID NOT NULL,
    person                VARCHAR(255),
    geldinstitut          VARCHAR(255),
    iban                  VARCHAR(255),
    bic                   VARCHAR(255),
    zahlungsempfaenger_id UUID NOT NULL,
    CONSTRAINT pk_bankverbindung PRIMARY KEY (id)
);

CREATE TABLE bearbeitungsstand
(
    id              UUID         NOT NULL,
    anmerkungen     VARCHAR(255) NOT NULL,
    ist_mittelabruf BOOLEAN      NOT NULL,
    status          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_bearbeitungsstand PRIMARY KEY (id)
);

CREATE TABLE finanzierung
(
    id                                    UUID         NOT NULL,
    ist_projekt_vorsteuerabzugsberechtigt BOOLEAN      NOT NULL,
    ist_einladungs_foerderhinweis         BOOLEAN      NOT NULL,
    ist_website_foerderhinweis            BOOLEAN      NOT NULL,
    ist_sonstiger_foerderhinweis          BOOLEAN      NOT NULL,
    sonstige_foerderhinweise              VARCHAR(255) NOT NULL,
    bewilligter_zuschuss                  DOUBLE PRECISION,
    CONSTRAINT pk_finanzierung PRIMARY KEY (id)
);

CREATE TABLE finanzierungsmittel
(
    id                UUID             NOT NULL,
    kategorie         VARCHAR(255)     NOT NULL,
    betrag            DOUBLE PRECISION NOT NULL,
    direktorium_notiz VARCHAR(255)     NOT NULL,
    finanzierung_id   UUID             NOT NULL,
    CONSTRAINT pk_finanzierungsmittel PRIMARY KEY (id)
);

CREATE TABLE projekt
(
    id           UUID NOT NULL,
    titel        VARCHAR(255),
    beschreibung VARCHAR(255),
    start        date NOT NULL,
    ende         date NOT NULL,
    CONSTRAINT pk_projekt PRIMARY KEY (id)
);

CREATE TABLE voraussichtliche_ausgabe
(
    id                UUID             NOT NULL,
    kategorie         VARCHAR(255),
    betrag            DOUBLE PRECISION NOT NULL,
    direktorium_notiz VARCHAR(255)     NOT NULL,
    finanzierung_id   UUID             NOT NULL,
    CONSTRAINT pk_voraussichtlicheausgabe PRIMARY KEY (id)
);

CREATE TABLE zahlungsempfaenger
(
    id          UUID NOT NULL,
    dtype       VARCHAR(31),
    telefon_nr  VARCHAR(255),
    email       VARCHAR(255),
    adresse_id  UUID NOT NULL,
    name        VARCHAR(255),
    rechtsform  VARCHAR(255),
    zielsetzung VARCHAR(255),
    nachname    VARCHAR(255),
    vorname     VARCHAR(255),
    mobil_nr    VARCHAR(255),
    CONSTRAINT pk_zahlungsempfaenger PRIMARY KEY (id)
);

ALTER TABLE bankverbindung
    ADD CONSTRAINT uc_79b9fc5faada2c50fc7a5cae5 UNIQUE (person, geldinstitut, iban, bic, zahlungsempfaenger_id);

ALTER TABLE zahlungsempfaenger
    ADD CONSTRAINT uc_8ec5b2df9546feafd9ef863a7 UNIQUE (dtype, email, adresse_id, nachname, vorname, name, zielsetzung,
                                                        rechtsform);

ALTER TABLE adresse
    ADD CONSTRAINT uc_cff3c04d7299fe58dd0a0dfef UNIQUE (strasse, hausnummer, ort, postleitzahl);

ALTER TABLE projekt
    ADD CONSTRAINT uc_de31f3372638318247e13c50a UNIQUE (titel, beschreibung, start, ende);

ALTER TABLE anderer_zuwendungsantrag
    ADD CONSTRAINT FK_ANDERERZUWENDUNGSANTRAG_ON_ANTRAG FOREIGN KEY (antrag_id) REFERENCES antrag (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_ANTRAGSTELLER FOREIGN KEY (antragsteller_id) REFERENCES zahlungsempfaenger (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_BANKVERBINDUNG FOREIGN KEY (bankverbindung_id) REFERENCES bankverbindung (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_BEARBEITUNGSSTAND FOREIGN KEY (bearbeitungsstand_id) REFERENCES bearbeitungsstand (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_FINANZIERUNG FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_PROJEKT FOREIGN KEY (projekt_id) REFERENCES projekt (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_VERTRETUNGSBERECHTIGTER FOREIGN KEY (vertretungsberechtigter_id) REFERENCES zahlungsempfaenger (id);

ALTER TABLE bankverbindung
    ADD CONSTRAINT FK_BANKVERBINDUNG_ON_ZAHLUNGSEMPFAENGER FOREIGN KEY (zahlungsempfaenger_id) REFERENCES zahlungsempfaenger (id);

ALTER TABLE finanzierungsmittel
    ADD CONSTRAINT FK_FINANZIERUNGSMITTEL_ON_FINANZIERUNG FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE voraussichtliche_ausgabe
    ADD CONSTRAINT FK_VORAUSSICHTLICHEAUSGABE_ON_FINANZIERUNG FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE zahlungsempfaenger
    ADD CONSTRAINT FK_ZAHLUNGSEMPFAENGER_ON_ADRESSE FOREIGN KEY (adresse_id) REFERENCES adresse (id);

CREATE INDEX idx_antrag_projekt ON antrag(projekt_id);
CREATE INDEX idx_antrag_finanzierung ON antrag(finanzierung_id);
CREATE INDEX idx_antrag_antragsteller ON antrag(antragsteller_id);
CREATE INDEX idx_antrag_bankverbindung ON antrag(bankverbindung_id);
CREATE INDEX idx_antrag_vertretungsberechtigter ON antrag(vertretungsberechtigter_id);
CREATE INDEX idx_bankverbindung_zahlungsempfaenger ON bankverbindung(zahlungsempfaenger_id);
CREATE INDEX idx_zahlungsempfaenger_adresse ON zahlungsempfaenger(adresse_id);
CREATE INDEX idx_antrag_bearbeitungsstand ON antrag(bearbeitungsstand_id);