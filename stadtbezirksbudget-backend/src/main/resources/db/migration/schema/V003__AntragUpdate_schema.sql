ALTER TABLE finanzierungsmittel
    DROP COLUMN betrag;

ALTER TABLE finanzierungsmittel
    ADD betrag DECIMAL NOT NULL;

ALTER TABLE voraussichtliche_ausgabe
    DROP COLUMN betrag;

ALTER TABLE voraussichtliche_ausgabe
    ADD betrag DECIMAL NOT NULL;

ALTER TABLE finanzierung
    DROP COLUMN bewilligter_zuschuss;

ALTER TABLE finanzierung
    ADD bewilligter_zuschuss DECIMAL;

ALTER TABLE finanzierung
    ADD ist_einladung_foerderhinweis BOOLEAN;

ALTER TABLE finanzierung
    ALTER COLUMN ist_einladung_foerderhinweis SET NOT NULL;

ALTER TABLE finanzierung
    DROP COLUMN ist_einladungs_foerderhinweis;
