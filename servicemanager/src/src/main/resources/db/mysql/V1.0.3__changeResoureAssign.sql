/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/

drop table SM_RESOURCE_DETAIL;

drop table SM_DB_ASSIGN;

create table SM_DB_ASSIGN (
   DB_ID                VARCHAR(40)          not null comment '数据库ID',
   ASSIGN_ID            VARCHAR(40)          not null comment '资源分配ID',
   SVC_ID               VARCHAR(40)          not null comment '服务ID',
   ASSIGN_TIME          DATE                 null comment '分配时间',
   constraint PK_SM_DB_ASSIGN primary key (DB_ID, SVC_ID)
)CHARSET=utf8;

