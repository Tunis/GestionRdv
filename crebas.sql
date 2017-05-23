/*==============================================================*/
/* Nom de SGBD :  MySQL 5.0                                     */
/* Date de création :  17/05/2017 14:43:05                      */
/*==============================================================*/


drop table if exists CHEQUE;

drop table if exists CODEPOSTAL;

drop table if exists MEDECINS;

drop table if exists PAIEMENT;

drop table if exists PATIENTS;

drop table if exists PLANNING;

drop table if exists RDV;

drop table if exists TP;

drop table if exists VILLE;

/*==============================================================*/
/* Table : CHEQUE                                               */
/*==============================================================*/
create table CHEQUE
(
   ID_CHEQUE            int not null,
   ID_PAIEMENT          int,
   MONTANT_CHEQUE       decimal not null,
   NOM_CHEQUE           varchar(50) not null,
   BANQUE_CHEQUE        varchar(50) not null,
   primary key (ID_CHEQUE)
);

/*==============================================================*/
/* Table : CODEPOSTAL                                           */
/*==============================================================*/
create table CODEPOSTAL
(
   ID_CODEPOSTAL	 int not null,
   primary key (ID_CODEPOSTAL)
);

/*==============================================================*/
/* Table : MEDECINS                                             */
/*==============================================================*/
create table MEDECINS
(
   ID_MEDECIN           int not null,
   NOM_MEDECIN          varchar(50) not null,
   PRENOM_MEDECIN       varchar(50) not null,
   TELEPHONE_MEDECIN    varchar(16) not null,
   MAIL_MEDECIN         text,
   primary key (ID_MEDECIN)
);

/*==============================================================*/
/* Table : PAIEMENT                                             */
/*==============================================================*/
create table PAIEMENT
(
   ID_PAIEMENT          int not null,
   ID_CHEQUE            int,
   PRIX_TOTAL_PAIEMENT  decimal not null,
   PAYER_PAIEMENT       bool,
   DATE_PAIEMENT        date,
   ESPECES_PAIEMENT     decimal,
   CB_PAIEMENT          decimal,
   primary key (ID_PAIEMENT)
);

/*==============================================================*/
/* Table : PATIENTS                                             */
/*==============================================================*/
create table PATIENTS
(
   ID_PATIENT           int not null,
   ID_VILLE             int,
   ID_CODEPOSTAL        int,
   NOM_PATIENT          varchar(50),
   PRENOM_PATIENT       varchar(50) not null,
   NOMJF_PATIENT        varchar(50) not null,
   DATENAISSANCE_PATIENT date not null,
   TELEPHONE_PATIENT    varchar(16) not null,
   MAIL_PATIENT         text,
   NUMSECU_PATIENT      int,
   ADRESSE_PATIENT      text not null,
   primary key (ID_PATIENT)
);

/*==============================================================*/
/* Table : PLANNING                                             */
/*==============================================================*/
create table PLANNING
(
   ID_PLANNING          int not null,
   ID_MEDECIN           int,
   DATE_PLANNING        date not null,
   primary key (ID_PLANNING)
);

/*==============================================================*/
/* Table : RDV                                                  */
/*==============================================================*/
create table RDV
(
   ID_RDV               int not null,
   ID_PATIENT           int,
   ID_MEDECIN           int,
   ID_PAIEMENT          int,
   DATE_RDV             date not null,
   HEURE_RDV            time not null,
   DUREE_RDV            smallint not null,
   STATUS_RDV           smallint not null,
   TYPECS_RDV           int not null,
   COTATION_RDV         varchar(50) not null,
   primary key (ID_RDV)
);

/*==============================================================*/
/* Table : TP                                                   */
/*==============================================================*/
create table TP
(
   ID_TP                int not null,
   ID_PAIEMENT          int,
   PAYE_TP              bool not null,
   MONTANT_TP           decimal not null,
   primary key (ID_TP)
);

/*==============================================================*/
/* Table : VILLE                                                */
/*==============================================================*/
create table VILLE
(
   ID_VILLE             int not null,
   NOM_VILLE            varchar(50) not null,
   primary key (ID_VILLE)
);

alter table CHEQUE add constraint FK_8 foreign key (ID_PAIEMENT)
      references PAIEMENT (ID_PAIEMENT) on delete restrict on update restrict;

alter table PAIEMENT add constraint FK_RELATION_13 foreign key (ID_CHEQUE)
      references CHEQUE (ID_CHEQUE) on delete restrict on update restrict;

alter table PATIENTS add constraint FK_10 foreign key (ID_CODEPOSTAL)
      references CODEPOSTAL (ID_CODEPOSTAL) on delete restrict on update restrict;

alter table PATIENTS add constraint FK_RELATION_9 foreign key (ID_VILLE)
      references VILLE (ID_VILLE) on delete restrict on update restrict;

alter table PLANNING add constraint FK_1 foreign key (ID_MEDECIN)
      references MEDECINS (ID_MEDECIN) on delete restrict on update restrict;

alter table RDV add constraint FK_2 foreign key (ID_MEDECIN)
      references MEDECINS (ID_MEDECIN) on delete restrict on update restrict;

alter table RDV add constraint FK_3 foreign key (ID_PAIEMENT)
      references PAIEMENT (ID_PAIEMENT) on delete restrict on update restrict;

alter table RDV add constraint FK_4 foreign key (ID_PATIENT)
      references PATIENTS (ID_PATIENT) on delete restrict on update restrict;

alter table TP add constraint FK_7 foreign key (ID_PAIEMENT)
      references PAIEMENT (ID_PAIEMENT) on delete restrict on update restrict;

