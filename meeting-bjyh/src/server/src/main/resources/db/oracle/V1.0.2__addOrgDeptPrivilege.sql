/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/2/17 16:06:49                           */
/*==============================================================*/

alter table MR_ROOM add ORG_DEPT_ID          VARCHAR2(40);
alter table MR_ROOM add ORG_DEPT_ORDER       VARCHAR2(64);

alter table MT_MEETING add ORG_DEPT_ID          VARCHAR2(40);
alter table MT_MEETING add ORG_DEPT_ORDER       VARCHAR2(64);

alter table MR_RESERVED add ORG_DEPT_ID          VARCHAR2(40);
alter table MR_RESERVED add ORG_DEPT_ORDER       VARCHAR2(64);
