/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/15 8:45:41                            */
/*==============================================================*/


alter table MR_RESERVED
   add column DEL_FLAG CHAR(1) default '0';

comment on column MR_RESERVED.DEL_FLAG is
'删除标识';

