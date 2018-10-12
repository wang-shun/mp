/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/8 15:34:41                            */
/*==============================================================*/

drop table OP_LOG;

/*==============================================================*/
/* Table: OP_LOG                                                */
/*==============================================================*/
create table OP_LOG (
   ID                   VARCHAR2(40)          not null,
   MODULE               VARCHAR2(64)          not null,
   OBJECT               VARCHAR2(64)          not null,
   OP                   VARCHAR2(64)          not null,
   CONTENT              VARCHAR2(4000)        not null,
   RESULT               VARCHAR2(5)           default '1' not null
      constraint CKC_RESULT_OP_LOG check (RESULT in ('1','0')),
   ECID                 VARCHAR2(40)          not null,
   DEPT_NAME            VARCHAR2(255)         null,
   DEPT_ID              VARCHAR2(40)          not null,
   USER_NAME            VARCHAR2(255)         not null,
   USER_ID              VARCHAR2(40)          not null,
   OP_TIME              TIMESTAMP            not null,
   constraint PK_OP_LOG primary key (ID)
);

comment on column OP_LOG.MODULE is
'代码package';

comment on column OP_LOG.RESULT is
'Y：成功， N：失败';
