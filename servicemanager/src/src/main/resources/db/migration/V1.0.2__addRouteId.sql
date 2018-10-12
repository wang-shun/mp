/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/
alter table SM_SVC_AUTH
    add column ROUTE_ID             VARCHAR(40)          not null;

