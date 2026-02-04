CREATE TABLE fk_prototyp
(
    id                  BIGSERIAL PRIMARY KEY,
    name_antragsteller  TEXT,
    geldinstitut        TEXT,
    bezirksausschuss_nr INTEGER,
    created_at          TIMESTAMP DEFAULT now()
);