/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/2/17 16:06:49                           */
/*==============================================================*/

alter table MR_ROOM add column ORG_DEPT_ID          VARCHAR(40);
alter table MR_ROOM add column ORG_DEPT_ORDER       VARCHAR(64);

alter table MR_RESERVED add column ORG_DEPT_ID          VARCHAR(40);
alter table MR_RESERVED add column ORG_DEPT_ORDER       VARCHAR(64);
