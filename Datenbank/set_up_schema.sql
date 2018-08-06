CREATE SCHEMA IF NOT EXISTS auswertung;
USE auswertung;

CREATE TABLE IF NOT EXISTS verein (
    id INT(11) NOT NULL AUTO_INCREMENT,
    bezeichnung VARCHAR(45) NULL,
    bezeichnung_kurz VARCHAR(45) NULL,
    landesverband VARCHAR(45) NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS rennen (
    id INT(11) NOT NULL AUTO_INCREMENT,
    rennnummer INT(11) NULL,
    bezeichnung VARCHAR(45) NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS ergebnis (
    id INT(11) NOT NULL AUTO_INCREMENT,
    startnummer INT(11) NULL,
    name VARCHAR(45) NULL,
    rennen_id INT(11) NULL,
    verein_id INT(11) NULL,
    zeit_1_start_auto BIGINT(14) NULL,
    zeit_1_ziel_auto BIGINT(14) NULL,
    zeit_1_start_man BIGINT(14) NULL,
    zeit_1_ziel_man BIGINT(14) NULL,
    zeit_2_start_auto BIGINT(14) NULL,
    zeit_2_ziel_auto BIGINT(14) NULL,
    zeit_2_start_man BIGINT(14) NULL,
    zeit_2_ziel_man BIGINT(14) NULL,
    niz_1 TINYINT(1) NULL DEFAULT 0,
    niz_2 TINYINT(1) NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX fk_rennen_idx (rennen_id ASC),
    INDEX fk_verein_idx (verein_id ASC),
    CONSTRAINT fk_verein FOREIGN KEY (verein_id)
        REFERENCES auswertung.verein (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_rennen FOREIGN KEY (rennen_id)
        REFERENCES auswertung.rennen (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS fehler (
    ergebnis_id INT(11) NOT NULL,
    lauf INT(11) NOT NULL,
    tor INT(11) NOT NULL,
    fehler_wert INT(11) NULL,
    PRIMARY KEY (ergebnis_id , lauf , tor),
    CONSTRAINT fk_ergebnis FOREIGN KEY (ergebnis_id)
        REFERENCES auswertung.ergebnis (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
)  ENGINE=INNODB;

CREATE VIEW fehler_1 AS
    SELECT 
        ergebnis_id, lauf, tor, fehler_wert
    FROM
        fehler
    WHERE
        lauf = 1;
        
CREATE VIEW fehler_2 AS
    SELECT 
        ergebnis_id, lauf, tor, fehler_wert
    FROM
        fehler
    WHERE
        lauf = 2;