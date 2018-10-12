/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/7/19 17:19:41                           */
/*==============================================================*/


drop table if exists MR_PRIVILEGE;

/*==============================================================*/
/* Table: MR_PRIVILEGE                                          */
/*==============================================================*/
create table MR_PRIVILEGE (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   TYPE                 VARCHAR(64)          not null,
   ENTITY_ID            VARCHAR(40)          not null,
   ENTITY_NAME          VARCHAR(255)         not null,
   DEPT_FULL_ID         VARCHAR(1000)        null,
   AUTHR_TIME           TIMESTAMP            not null,
   constraint PK_MR_PRIVILEGE primary key (ID)
);

comment on column MR_PRIVILEGE.TYPE is
'user：用户 dept：部门';

comment on column MR_PRIVILEGE.DEPT_FULL_ID is
'如果实体类型为dept，其部门的全路径ID';


