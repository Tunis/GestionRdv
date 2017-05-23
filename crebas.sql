/*==============================================================*/
/* Nom de SGBD :  MySQL 5.0                                     */
/* Date de cr�ation :  17/05/2017 14:43:05                      */
/*==============================================================*/


drop table if exists cheque;

drop table if exists adresse;

drop table if exists medecin;

drop table if exists paiement;

drop table if exists patient;

drop table if exists presentday;

drop table if exists rdv;

drop table if exists tp;

drop table if exists comptajournaliere;

DROP TABLE IF EXISTS hibernate_sequence;


CREATE TABLE medecin
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   email VARCHAR(255),
   firstName VARCHAR(50),
   lastName VARCHAR(50),
   telephone VARCHAR(16)
);
CREATE TABLE adresse
(
   id INT(11) PRIMARY KEY NOT NULL,
   codePostal INT(11) NOT NULL,
   rue VARCHAR(255),
   ville VARCHAR(255)
);
CREATE TABLE patient
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   adresse VARCHAR(255),
   bornDate DATE NOT NULL,
   email VARCHAR(255),
   firstName VARCHAR(255) NOT NULL,
   lastName VARCHAR(255),
   maidenName VARCHAR(255) NOT NULL,
   note VARCHAR(255),
   secuNumber SMALLINT(6) NOT NULL,
   telephone VARCHAR(255),
   adresse_id INT(11)
);
CREATE TABLE tp
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   montant FLOAT NOT NULL,
   payer BIT(1) NOT NULL
);
CREATE TABLE cheque
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   banque VARCHAR(255),
   montant FLOAT NOT NULL,
   name VARCHAR(255)
);
CREATE TABLE comptajournaliere
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   CB FLOAT NOT NULL,
   cheque FLOAT NOT NULL,
   date DATE,
   espece FLOAT NOT NULL,
   impayer FLOAT NOT NULL,
   nbC2 SMALLINT(6) NOT NULL,
   nbCS SMALLINT(6) NOT NULL,
   nbDIU SMALLINT(6) NOT NULL,
   nbEcho SMALLINT(6) NOT NULL,
   nbTp SMALLINT(6) NOT NULL,
   retrait FLOAT NOT NULL,
   soldePrecedent FLOAT NOT NULL,
   medecin_id BIGINT(20)
);
CREATE TABLE hibernate_sequence
(
   next_val BIGINT(20)
);
CREATE TABLE rdv
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   cotation VARCHAR(255) NOT NULL,
   duration BIGINT(20) NOT NULL,
   time TIME,
   typeRdv INT(11),
   patient_id BIGINT(20),
   presentDay_id BIGINT(20),
   paiement_id BIGINT(20)
);
CREATE TABLE paiement
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   cb FLOAT NOT NULL,
   date DATE,
   espece FLOAT NOT NULL,
   payer BIT(1) NOT NULL,
   prix FLOAT NOT NULL,
   rdv_id BIGINT(20)
);
CREATE TABLE presentday
(
   id BIGINT(20) PRIMARY KEY NOT NULL,
   present DATE NOT NULL,
   medecin_id BIGINT(20)
);
CREATE INDEX FKfhi6jkayeqsu1utn2ruthsaa3 ON presentday (medecin_id);
CREATE INDEX FKgwqrtq3w104y66k2ttxghi43 ON rdv (presentDay_id);
CREATE INDEX FKics3gsw6y1y55h4owuimtcj54 ON rdv (patient_id);
CREATE INDEX FKd23rv2xpnutfiykpm0imi8bbw ON rdv (paiement_id);
CREATE INDEX FKdywdoxky1iv6dvtujtqgcqwq ON paiement (rdv_id);
CREATE INDEX FKdt49ieau9obbiprjj9jc9tr5i ON patient (adresse_id);
CREATE INDEX FKn4ct81d2cwwrrjaoehe4hntmn ON comptajournaliere (medecin_id);


INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (30, null, 'M9', 'M9', '99999999');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (29, null, 'M8', 'M8', '88888888');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (28, null, 'M7', 'M7', '77777777');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (27, null, 'M6', 'M6', '66666666');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (26, null, 'M5', 'M5', '55555555');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (25, null, 'M4', 'M4', '44444444');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (24, null, 'M3', 'M3', '33333333');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (23, null, 'M2', 'M2', '22222222');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (22, null, 'M1', 'M1', '11111111');
INSERT INTO test.medecin (id, email, firstName, lastName, telephone) VALUES (21, null, 'M0', 'M0', '00000000');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (1, null, '2017-05-17', null, 'P0', 'P0', 'P0', null, 0, '0606060606');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (2, null, '2016-05-17', null, 'P1', 'P1', 'P1', null, 0, '0808080808');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (3, null, '2015-05-17', null, 'P2', 'P2', 'P2', null, 0, '1616161616');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (4, null, '2014-05-17', null, 'P3', 'P3', 'P3', null, 0, '2424242424');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (5, null, '2013-05-17', null, 'P4', 'P4', 'P4', null, 0, '3232323232');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (6, null, '2012-05-17', null, 'P5', 'P5', 'P5', null, 0, '4040404040');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (7, null, '2011-05-17', null, 'P6', 'P6', 'P6', null, 0, '4848484848');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (8, null, '2010-05-17', null, 'P7', 'P7', 'P7', null, 0, '5656565665');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (9, null, '2009-05-17', null, 'P8', 'P8', 'P8', null, 0, '6464646464');
INSERT INTO test.patient (id, adresse, bornDate, email, firstName, lastName, maidenName, note, secuNumber, telephone) VALUES (10, null, '2008-05-17', null, 'P9', 'P9', 'P9', null, 0, '7272727227');