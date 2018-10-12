/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/10/8 10:29:34                           */
/*==============================================================*/

alter table MT_PARTICIPANTS
   add column VISIBLE VARCHAR(5) default '1';

