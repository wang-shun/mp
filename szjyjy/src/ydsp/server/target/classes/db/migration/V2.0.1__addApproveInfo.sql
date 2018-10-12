/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2016/11/22 15:43:30                          */
/*==============================================================*/


/*==============================================================*/
/* Table: MR_APPROVE_INFO                                       */
/*==============================================================*/
create table MR_APPROVE_INFO (
   RESERVED_ID          VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null,
   APPROVED             VARCHAR(5)           not null default '0'
      constraint CKC_APPROVED_MR_APPRO check (APPROVED in ('1','0')),
   APPROVE_TIME         TIMESTAMP            null,
   constraint PK_MR_APPROVE_INFO primary key (RESERVED_ID, USER_ID)
);

