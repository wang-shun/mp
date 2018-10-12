/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/

drop table SM_RESOURCE_DETAIL;

drop table SM_DB_ASSIGN;

create table SM_DB_ASSIGN (
   DB_ID                VARCHAR(40)          not null,
   ASSIGN_ID            VARCHAR(40)          not null,
   SVC_ID               VARCHAR(40)          not null,
   ASSIGN_TIME          DATE                 null,
   constraint PK_SM_DB_ASSIGN primary key (DB_ID, SVC_ID)
);

comment on column SM_DB_ASSIGN.DB_ID is
'数据库ID';

comment on column SM_DB_ASSIGN.ASSIGN_ID is
'资源分配ID';

comment on column SM_DB_ASSIGN.SVC_ID is
'服务ID';

comment on column SM_DB_ASSIGN.ASSIGN_TIME is
'分配时间';


