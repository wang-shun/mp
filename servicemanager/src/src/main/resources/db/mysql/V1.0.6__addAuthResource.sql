/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/
alter table SM_ROUTE
    add column AUTH_RESOURCE        VARCHAR(1000)        null;

