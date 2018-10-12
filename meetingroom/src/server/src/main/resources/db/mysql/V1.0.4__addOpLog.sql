/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/3 15:53:23                            */
/*==============================================================*/


/*==============================================================*/
/* Table: OP_LOG                                                */
/*==============================================================*/
create table OP_LOG (
   ID                   VARCHAR(40)          not null,
   MODULE               VARCHAR(64)          not null comment '代码package',
   OBJECT               VARCHAR(64)          not null,
   OP                   VARCHAR(64)          not null,
   CONTENT              VARCHAR(4000)        not null,
   RESULT               enum('1','0')        not null default '1' comment 'Y：成功， N：失败',
   ECID                 VARCHAR(40)          not null,
   DEPT_NAME            VARCHAR(255)         null,
   DEPT_ID              VARCHAR(40)          not null,
   USER_NAME            VARCHAR(255)         not null,
   USER_ID              VARCHAR(40)          not null,
   OP_TIME              TIMESTAMP            not null,
   constraint PK_OP_LOG primary key (ID)
)CHARSET=utf8;
