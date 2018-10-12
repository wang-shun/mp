/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/3 15:53:23                            */
/*==============================================================*/

alter table MR_PRIVILEGE drop column DEPT_FULL_ID;

/*==============================================================*/
/* Table: OP_LOG                                                */
/*==============================================================*/
create table OP_LOG (
   ID                   VARCHAR2(40)          not null,
   MODULE               VARCHAR2(64)          not null,
   OBJECT               VARCHAR2(64)          not null,
   OP                   VARCHAR2(64)          not null,
   CONTENT              VARCHAR2(512)         not null,
   RESULT               VARCHAR2(5)           default '1' not null
      constraint CKC_RESULT_OP_LOG check (RESULT in ('1','0')),
   ECID                 VARCHAR2(40)          not null,
   USER_NAME            VARCHAR2(255)         not null,
   USER_ID              VARCHAR2(40)          not null,
   OP_TIME              TIMESTAMP            not null,
   constraint PK_OP_LOG primary key (ID)
);



