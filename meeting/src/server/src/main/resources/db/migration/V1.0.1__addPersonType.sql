/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/9/8 11:25:12                            */
/*==============================================================*/


alter table MT_SIGNIN_RECORD
   add column PERSON_TYPE VARCHAR(64);

comment on column MT_SIGNIN_RECORD.PERSON_TYPE is
'inner：内部人员 outer：外部人员';

