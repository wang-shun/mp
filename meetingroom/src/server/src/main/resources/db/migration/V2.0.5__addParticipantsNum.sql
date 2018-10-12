/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/15 8:45:41                            */
/*==============================================================*/


alter table MR_RESERVED
   add column PARTICIPANTS_NUM DECIMAL(10);

comment on column MR_RESERVED.PARTICIPANTS_NUM is
'参会人数';

