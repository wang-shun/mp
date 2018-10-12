/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2016/11/23 9:11:28                           */
/*==============================================================*/

alter table MR_RESERVED
   add PARTICIPANTS_NUM NUMBER(10);

comment on column MR_RESERVED.PARTICIPANTS_NUM is
'参会人数';
