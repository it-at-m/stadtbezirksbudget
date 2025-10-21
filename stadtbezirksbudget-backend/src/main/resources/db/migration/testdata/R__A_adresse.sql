truncate table adresse, projekt, finanzierung, bearbeitungsstand, voraussichtliche_ausgabe, finanzierungsmittel, zahlungsempfaenger, bankverbindung, antrag, anderer_zuwendungsantrag RESTART IDENTITY CASCADE;

INSERT INTO adresse (id, strasse, hausnummer, postleitzahl, ort)
VALUES ('00000000-0000-0000-0000-000000000000', 'Musterstraße', '1A', '12345', 'Musterstadt'),
       ('00000000-0000-0000-0000-000000000001', 'Beispielweg', '2', '67890', 'Beispielstadt'),
       ('00000000-0000-0000-0000-000000000002', 'Testallee', '100', '54321', 'Teststadt'),
       ('00000000-0000-0000-0000-000000000003', 'Demo Boulevard', '50B', '98765', 'Demostadt');