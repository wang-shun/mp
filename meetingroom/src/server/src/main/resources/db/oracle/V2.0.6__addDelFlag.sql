/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2016/11/23 9:11:28                           */
/*==============================================================*/

alter table MR_RESERVED
   add DEL_FLAG CHAR(1) default '0';

comment on column MR_RESERVED.DEL_FLAG is
'删除标识';
