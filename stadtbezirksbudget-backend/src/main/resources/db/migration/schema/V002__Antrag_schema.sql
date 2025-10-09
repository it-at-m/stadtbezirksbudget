CREATE TABLE adresse
(
    id           UUID NOT NULL,
    strasse      VARCHAR(255),
    hausnummer   VARCHAR(255),
    ort          VARCHAR(255),
    postleitzahl VARCHAR(255),
    CONSTRAINT pk_adresse PRIMARY KEY (id)
);

CREATE TABLE antrag
(
    id                                   UUID    NOT NULL,
    bezirksausschuss_nr                  INTEGER NOT NULL,
    eingangsdatum                        TIMESTAMP WITHOUT TIME ZONE,
    zuwendungen_dritter_beschreibung     VARCHAR(255),
    ist_person_vorsteuerabzugsberechtigt BOOLEAN NOT NULL,
    projekt_id                           UUID,
    finanzierung_id                      UUID,
    antragsteller_id                     UUID,
    bankverbindung_id                    UUID,
    vertretungsberechtigter_id           UUID,
    CONSTRAINT pk_antrag PRIMARY KEY (id)
);

CREATE TABLE antrag_mitglieder
(
    antrag_id     UUID NOT NULL,
    mitglieder_id UUID NOT NULL
);

CREATE TABLE antragsteller
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    telefon_nr  VARCHAR(255),
    email       VARCHAR(255),
    zielsetzung VARCHAR(255),
    rechtsform  SMALLINT,
    adresse_id  UUID,
    CONSTRAINT pk_antragsteller PRIMARY KEY (id)
);

CREATE TABLE bankverbindung
(
    id           UUID NOT NULL,
    person       VARCHAR(255),
    geldinstitut VARCHAR(255),
    iban         VARCHAR(255),
    bic          VARCHAR(255),
    adresse_id   UUID,
    CONSTRAINT pk_bankverbindung PRIMARY KEY (id)
);

CREATE TABLE finanzierung
(
    id                                    UUID             NOT NULL,
    ist_projekt_vorsteuerabzugsberechtigt BOOLEAN          NOT NULL,
    bewilligter_zuschuss                  DOUBLE PRECISION NOT NULL,
    ist_einladungs_foerderhinweis         BOOLEAN          NOT NULL,
    ist_website_foerderhinweis            BOOLEAN          NOT NULL,
    sonstige_foerderhinweis               VARCHAR(255),
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
    direktorium_notiz VARCHAR(255),
    CONSTRAINT pk_finanzierungsmittel PRIMARY KEY (id)
);

CREATE TABLE mitglied
(
    id         UUID NOT NULL,
    nachname   VARCHAR(255),
    vorname    VARCHAR(255),
    adresse_id UUID,
    CONSTRAINT pk_mitglied PRIMARY KEY (id)
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

CREATE TABLE vertretungsberechtigter
(
    id         UUID NOT NULL,
    nachname   VARCHAR(255),
    vorname    VARCHAR(255),
    telefon_nr VARCHAR(255),
    email      VARCHAR(255),
    mobil_nr   VARCHAR(255),
    adresse_id UUID,
    CONSTRAINT pk_vertretungsberechtigter PRIMARY KEY (id)
);

CREATE TABLE voraussichtliche_ausgabe
(
    id                UUID             NOT NULL,
    kategorie         VARCHAR(255),
    betrag            DOUBLE PRECISION NOT NULL,
    direktorium_notiz VARCHAR(255),
    CONSTRAINT pk_voraussichtlicheausgabe PRIMARY KEY (id)
);

ALTER TABLE finanzierung_finanzierungsmittel_liste
    ADD CONSTRAINT uc_finanzierungfinanzierungsmittellist_finanzierungsmittelliste UNIQUE (finanzierungsmittel_liste_id);

ALTER TABLE finanzierung_voraussichtliche_ausgaben
    ADD CONSTRAINT uc_finanzierungvoraussichtlicheausgabe_voraussichtlicheausgaben UNIQUE (voraussichtliche_ausgaben_id);

ALTER TABLE antragsteller
    ADD CONSTRAINT FK_ANTRAGSTELLER_ON_ADRESSE FOREIGN KEY (adresse_id) REFERENCES adresse (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_ANTRAGSTELLER FOREIGN KEY (antragsteller_id) REFERENCES antragsteller (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_BANKVERBINDUNG FOREIGN KEY (bankverbindung_id) REFERENCES bankverbindung (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_FINANZIERUNG FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_PROJEKT FOREIGN KEY (projekt_id) REFERENCES projekt (id);

ALTER TABLE antrag
    ADD CONSTRAINT FK_ANTRAG_ON_VERTRETUNGSBERECHTIGTER FOREIGN KEY (vertretungsberechtigter_id) REFERENCES vertretungsberechtigter (id);

ALTER TABLE bankverbindung
    ADD CONSTRAINT FK_BANKVERBINDUNG_ON_ADRESSE FOREIGN KEY (adresse_id) REFERENCES adresse (id);

ALTER TABLE mitglied
    ADD CONSTRAINT FK_MITGLIED_ON_ADRESSE FOREIGN KEY (adresse_id) REFERENCES adresse (id);

ALTER TABLE vertretungsberechtigter
    ADD CONSTRAINT FK_VERTRETUNGSBERECHTIGTER_ON_ADRESSE FOREIGN KEY (adresse_id) REFERENCES adresse (id);

ALTER TABLE antrag_mitglieder
    ADD CONSTRAINT fk_antmit_on_antrag FOREIGN KEY (antrag_id) REFERENCES antrag (id);

ALTER TABLE antrag_mitglieder
    ADD CONSTRAINT fk_antmit_on_mitglied FOREIGN KEY (mitglieder_id) REFERENCES mitglied (id);

ALTER TABLE finanzierung_finanzierungsmittel_liste
    ADD CONSTRAINT fk_finfinlis_on_finanzierung FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE finanzierung_finanzierungsmittel_liste
    ADD CONSTRAINT fk_finfinlis_on_finanzierungsmittel FOREIGN KEY (finanzierungsmittel_liste_id) REFERENCES finanzierungsmittel (id);

ALTER TABLE finanzierung_voraussichtliche_ausgaben
    ADD CONSTRAINT fk_finvoraus_on_finanzierung FOREIGN KEY (finanzierung_id) REFERENCES finanzierung (id);

ALTER TABLE finanzierung_voraussichtliche_ausgaben
    ADD CONSTRAINT fk_finvoraus_on_voraussichtliche_ausgabe FOREIGN KEY (voraussichtliche_ausgaben_id) REFERENCES voraussichtliche_ausgabe (id);