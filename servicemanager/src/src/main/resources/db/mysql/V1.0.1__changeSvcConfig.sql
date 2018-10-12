/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/4/1 10:56:35                            */
/*==============================================================*/
drop table SM_RESOURCE_ASSIGN;

/*==============================================================*/
/* Table: SM_RESOURCE_ASSIGN                                    */
/*==============================================================*/
create table SM_RESOURCE_ASSIGN (
   ID                   VARCHAR(40)          not null,
   APP_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         null,
   RES_ID               VARCHAR(40)          not null comment '依赖的资源应用ID',
   RES_CODE             VARCHAR(64)          not null comment '识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default',
   RES_NAME             VARCHAR(255)         null,
   ASSIGN_TIME          DATE                 null,
   constraint PK_SM_RESOURCE_ASSIGN primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_RESOURCE_DETAIL                                    */
/*==============================================================*/
create table SM_RESOURCE_DETAIL (
   ID                   VARCHAR(40)          not null,
   RES_ASSIGN_ID        VARCHAR(40)          not null,
   PARAM_KEY            VARCHAR(64)          not null comment '参数key',
   PARAM_VALUE          VARCHAR(1000)        null comment '参数值',
   constraint PK_SM_RESOURCE_DETAIL primary key (ID)
)CHARSET=utf8;

