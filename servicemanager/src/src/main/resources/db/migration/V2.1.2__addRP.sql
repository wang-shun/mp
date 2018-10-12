/*==============================================================*/
/* Table: MC_RETENTION_POLICY                                   */
/*==============================================================*/
create table MC_RETENTION_POLICY (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   RP                   VARCHAR(64)          null,
   RP_NAME              VARCHAR(512)         null,
   RETAIN_TIME          VARCHAR(64)          null,
   IS_DEFAULT           VARCHAR(5)           not null default '0'
      constraint CKC_IS_DEFAULT_MC_RETEN check (IS_DEFAULT in ('1','0')),
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_MC_RETEN check (ENABLED in ('1','0')),
   constraint PK_MC_RETENTION_POLICY primary key (ID)
);

comment on column MC_RETENTION_POLICY.RETAIN_TIME is
'比如2h 3d 4w';

/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/
alter table MC_MEASUREMENT
    add column RETAIN_POLICY        VARCHAR(64)          null;


