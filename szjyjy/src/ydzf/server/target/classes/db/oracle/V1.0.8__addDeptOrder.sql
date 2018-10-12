/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/15 8:45:41                            */
/*==============================================================*/


alter table MR_PRIVILEGE
   add DEPT_ORDER VARCHAR2(1000);

comment on column MR_PRIVILEGE.DEPT_ORDER is
'授权类型为部门时，部门所在的序号，用于查询子部门权限';

