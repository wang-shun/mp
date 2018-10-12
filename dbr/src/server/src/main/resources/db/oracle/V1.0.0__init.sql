/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/3/3 9:45:32                             */
/*==============================================================*/


/*==============================================================*/
/* Table: DBR_BORROW_RETURN_LOG                                 */
/*==============================================================*/
create table DBR_BORROW_RETURN_LOG 
(
   ID                   VARCHAR2(40)         not null,
   DEVICE_ID            VARCHAR2(64)         not null,
   ECID                 VARCHAR2(40)         not null,
   DEPT_NAME            VARCHAR2(255),
   DEPT_ID              VARCHAR2(40)         not null,
   USER_NAME            VARCHAR2(255)        not null,
   USER_ID              VARCHAR2(40)         not null,
   BORROW_TIME          TIMESTAMP,
   RETURN_TIME          TIMESTAMP,
   LOG_FLAG             CHAR(1)              not null,
   constraint PK_DBR_BORROW_RETURN_LOG primary key (ID)
);

comment on column DBR_BORROW_RETURN_LOG.LOG_FLAG is
'0 未归还 1已归还 ';

/*==============================================================*/
/* Table: DBR_DEVICE                                            */
/*==============================================================*/
create table DBR_DEVICE 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   USER_ID              VARCHAR2(64)         not null,
   USER_NAME            VARCHAR2(255)        not null,
   DEVICE_ID            VARCHAR2(64)         not null,
   DEVICE_NAME          VARCHAR2(255)        not null,
   CREATE_TIME          TIMESTAMP            not null,
   DEVICE_STATUS        VARCHAR2(5)          default '0' not null
      constraint CKC_DEVICE_STATUS_DBR_DEVI check (DEVICE_STATUS in ('1','0')),
   constraint PK_DBR_DEVICE primary key (ID)
);

comment on column DBR_DEVICE.USER_ID is
'loginid';

comment on column DBR_DEVICE.DEVICE_STATUS is
'0 未借出 1已借出 ';

