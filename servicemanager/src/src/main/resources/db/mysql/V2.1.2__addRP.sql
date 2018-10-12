/*==============================================================*/
/* Table: MC_RETENTION_POLICY                                   */
/*==============================================================*/
create table MC_RETENTION_POLICY (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   RP                   VARCHAR(64)          null,
   RP_NAME              VARCHAR(512)         null,
   RETAIN_TIME          VARCHAR(64)          null comment '比如2h 3d 4w',
   IS_DEFAULT           enum('1','0')        not null default '0',
   ENABLED              enum('1','0')        not null default '1',
   constraint PK_MC_RETENTION_POLICY primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/
alter table MC_MEASUREMENT
    add column RETAIN_POLICY        VARCHAR(64)          null;


