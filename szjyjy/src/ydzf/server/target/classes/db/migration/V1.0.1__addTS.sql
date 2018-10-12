/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/7/11 10:54:43                           */
/*==============================================================*/


alter table MR_ROOM
   add column CREATE_TIME TIMESTAMP;

alter table MR_ROOM
   add column MODIFIED_TIME TIMESTAMP;

