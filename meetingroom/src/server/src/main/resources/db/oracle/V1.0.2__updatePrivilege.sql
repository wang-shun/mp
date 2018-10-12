/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/7/19 17:19:41                           */
/*==============================================================*/


drop table MR_PRIVILEGE;

/*==============================================================*/
/* Table: MR_PRIVILEGE                                          */
/*==============================================================*/
create table MR_PRIVILEGE (
   ID                   VARCHAR2(40)          not null,
   ECID                 VARCHAR2(40)          not null,
   ROOM_ID              VARCHAR2(40)          not null,
   TYPE                 VARCHAR2(64)          not null,
   ENTITY_ID            VARCHAR2(40)          not null,
   ENTITY_NAME          VARCHAR2(255)         not null,
   DEPT_FULL_ID         VARCHAR2(1000)        null,
   AUTHR_TIME           TIMESTAMP            not null,
   constraint PK_MR_PRIVILEGE primary key (ID)
);

comment on column MR_PRIVILEGE.TYPE is
'user：用户 dept：部门';

comment on column MR_PRIVILEGE.DEPT_FULL_ID is
'如果实体类型为dept，其部门的全路径ID';


