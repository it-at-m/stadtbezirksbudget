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
    antragsdatum TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    stelle       VARCHAR(255),
    CONSTRAINT pk_andererzuwendungsantrag PRIMARY KEY (id)
);

CREATE TABLE antrag
(
    id                                   UUID    NOT NULL,
    bezirksausschuss_nr                  INTEGER NOT NULL,
    eingangsdatum                        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ist_person_vorsteuerabzugsberechtigt BOOLEAN NOT NULL,
    projekt_id                           UUID    NOT NULL,
    finanzierung_id                      UUID    NOT NULL,
    antragsteller_id                     UUID    NOT NULL,
    bankverbindung_id                    UUID    NOT NULL,
    vertretungsberechtigter_id           UUID,
    CONSTRAINT pk_antrag PRIMARY KEY (id)
);

CREATE TABLE antrag_andere_zuwendungsantraege
(
    antrag_id                    UUID NOT NULL,
    andere_zuwendungsantraege_id UUID NOT NULL
);

CREATE TABLE bankverbindung
(
    id                    UUID NOT NULL,
    zahlungsempfaenger_id UUID NOT NULL,
    person                VARCHAR(255),
    geldinstitut          VARCHAR(255),
    iban                  VARCHAR(255),
    bic                   VARCHAR(255),
    CONSTRAINT pk_bankverbindung PRIMARY KEY (id)
);

CREATE TABLE bearbeitungsstand
(
    id              UUID         NOT NULL,
    anmerkungen     VARCHAR(255) NOT NULL,
    status          SMALLINT,
    ist_mittelabruf BOOLEAN      NOT NULL,
    CONSTRAINT pk_bearbeitungsstand PRIMARY KEY (id)
);

CREATE TABLE finanzierung
(
    id                                    UUID         NOT NULL,
    ist_projekt_vorsteuerabzugsberechtigt BOOLEAN      NOT NULL,
    bewilligter_zuschuss                  DOUBLE PRECISION,
    ist_einladungs_foerderhinweis         BOOLEAN      NOT NULL,
    ist_website_foerderhinweis            BOOLEAN      NOT NULL,
    sonstige_foerderhinweise              VARCHAR(255) NOT NULL,
    CONSTRAINT pk_finanzierung PRIMARY KEY (id)
);

CREATE TABLE finanzierung_finanzierungsmittel_liste
(
    finanzierung_id              UUID NOT NULL,
    finanzierungsmittel_liste_id UUID NOT NULL
);

CREATE TABLE finanzierung_voraussichtliche_ausgaben
(
    finanzierung_id              UUID NOT NULL,
    voraussichtliche_ausgaben_id UUID NOT NULL
);

CREATE TABLE finanzierungsmittel
(
    id                UUID             NOT NULL,
    kategorie         SMALLINT,
    betrag            DOUBLE PRECISION NOT NULL,
    direktorium_notiz VARCHAR(255)     NOT NULL,
    CONSTRAINT pk_finanzierungsmittel PRIMARY KEY (id)
);

CREATE TABLE projekt
(
    id           UUID NOT NULL,
    titel        VARCHAR(255),
    beschreibung VARCHAR(255),
    start        TIMESTAMP WITHOUT TIME ZONE,
    ende         TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_projekt PRIMARY KEY (id)
);

CREATE TABLE voraussichtliche_ausgabe
(
    id                UUID             NOT NULL,
    kategorie         VARCHAR(255),
    betrag            DOUBLE PRECISION NOT NULL,
    direktorium_notiz VARCHAR(255)     NOT NULL,
    CONSTRAINT pk_voraussichtlicheausgabe PRIMARY KEY (id)
);

CREATE TABLE zahlungsempfaenger
(
    id          UUID         NOT NULL,
    dtype       VARCHAR(31),
    telefon_nr  VARCHAR(255),
    email       VARCHAR(255),
    adresse_id  UUID         NOT NULL,
    nachname    VARCHAR(255),
    vorname     VARCHAR(255),
    mobil_nr    VARCHAR(255) NOT NULL,
    name        VARCHAR(255),
    zielsetzung VARCHAR(255) NOT NULL,
    rechtsform  SMALLINT,
    CONSTRAINT pk_zahlungsempfaenger PRIMARY KEY (id)
);

ALTER TABLE bankverbindung
    ADD CONSTRAINT uc_79b9fc5faada2c50fc7a5cae5 UNIQUE (person, geldinstitut, iban, bic, zahlungsempfaenger_id);

ALTER TABLE zahlungsempfaenger
    ADD CONSTRAINT uc_8ec5b2df9546feafd9ef863a7 UNIQUE (dtype, email, adresse_id, nachname, vorname, name, zielsetzung,
                                                        rechtsform);

ALTER TABLE antrag_andere_zuwendungsantraege
    ADD CONSTRAINT uc_antrag_andere_zuwendungsantraege_anderezuwendungsantraege UNIQUE (andere_zuwendungsantraege_id);

ALTER TABLE adresse
    ADD CONSTRAINT uc_cff3c04d7299fe58dd0a0dfef UNIQUE (strasse, hausnummer, ort, postleitzahl);

ALTER TABLE projekt
    ADD CONSTRAINT uc_de31f3372638318247e13c50a UNIQUE (titel, beschreibung, start, ende);

ALTER TABLE finanzierung_finanzierungsmittel_liste
    ADD CONSTRAINT uc_finanzierungfinanzierungsmittellist_finanzierungsmittelliste UNIQUE (finanzierungsmittel_liste_id);

ALTER TABLE finanzierung_voraussichtliche_ausgaben
    ADD CONSTRAINT uc_finanzierungvoraussichtlicheausgabe_voraussichtlicheausgaben UNIQUE (voraussichtliche_ausgaben_id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_ANTRAGSTELLER FOREIGN KEY (antragsteller_id) REFERENCES zahlungsempfaenger (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_BANKVERBINDUNG FOREIGN KEY (bankverbindung_id) REFERENCES bankverbindung (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_FINANZIERUNG FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_PROJEKT FOREIGN KEY (projekt_id) REFERENCES projekt (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_VERTRETUNGSBERECHTIGTER FOREIGN KEY (vertretungsberechtigter_id) REFERENCES zahlungsempfaenger (id);

ALTER TABLE bankverbindung
    ADD CONSTRAINT FK_BANKVERBINDUNG_ON_ZAHLUNGSEMPFAENGER FOREIGN KEY (zahlungsempfaenger_id) REFERENCES zahlungsempfaenger (id);

ALTER TABLE zahlungsempfaenger
    ADD CONSTRAINT FK_ZAHLUNGSEMPFAENGER_ON_ADRESSE FOREIGN KEY (adresse_id) REFERENCES adresse (id);

ALTER TABLE antrag_andere_zuwendungsantraege
    ADD CONSTRAINT fk_antandzuw_on_anderer_zuwendungsantrag FOREIGN KEY (andere_zuwendungsantraege_id) REFERENCES anderer_zuwendungsantrag (id);

ALTER TABLE antrag_andere_zuwendungsantraege
    ADD CONSTRAINT fk_antandzuw_on_antrag FOREIGN KEY (antrag_id) REFERENCES antrag (id);

ALTER TABLE finanzierung_finanzierungsmittel_liste
    ADD CONSTRAINT fk_finfinlis_on_finanzierung FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE finanzierung_finanzierungsmittel_liste
    ADD CONSTRAINT fk_finfinlis_on_finanzierungsmittel FOREIGN KEY (finanzierungsmittel_liste_id) REFERENCES finanzierungsmittel (id);

ALTER TABLE finanzierung_voraussichtliche_ausgaben
    ADD CONSTRAINT fk_finvoraus_on_finanzierung FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE finanzierung_voraussichtliche_ausgaben
    ADD CONSTRAINT fk_finvoraus_on_voraussichtliche_ausgabe FOREIGN KEY (voraussichtliche_ausgaben_id) REFERENCES voraussichtliche_ausgabe (id);