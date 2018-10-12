/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/3/6 11:36:21                            */
/*==============================================================*/


/*==============================================================*/
/* Table: AT_ACTIVITY                                           */
/*==============================================================*/
create table AT_ACTIVITY (
   ID                   VARCHAR2(40)          not null,
   ECID	                VARCHAR2(40)          not null,
   ACT_CONTENT          VARCHAR2(4000)        not null,
   ACT_TITLE			VARCHAR2(255)         not null,
   ACT_START_TIME       TIMESTAMP             not null,
   ACT_END_TIME         TIMESTAMP             not null,
   ACT_ADDRESS          VARCHAR2(255)         not null,
   ACT_COORDINATE       VARCHAR2(255)         not null,
   ENTER_END_TIME       TIMESTAMP             not null,
   CON_TEL              VARCHAR2(255)         not null,
   NUM_LIMIT            NUMBER(10)            default 0 not null,
   ENTER_NUM            NUMBER(10)            default 0 not null,
   PHONE                VARCHAR2(5)           default '0' null 
      constraint CKC_PHONE_AT_ACTIV check (PHONE is null or (PHONE in ('1','0'))),
   NAME                 VARCHAR2(5)           default '0' null 
      constraint CKC_NAME_AT_ACTIV check (NAME is null or (NAME in ('1','0'))),
   ID_CARD              VARCHAR2(5)           default '0' null 
      constraint CKC_ID_CARD_AT_ACTIV check (ID_CARD is null or (ID_CARD in ('1','0'))),
   REMARK               VARCHAR2(5)           default '0' null 
      constraint CKC_REMARK_AT_ACTIV check (REMARK is null or (REMARK in ('1','0'))),
   SEX                  VARCHAR2(5)           default '1' null 
      constraint CKC_SEX_AT_ACTIV check (SEX is null or (SEX in ('1','0'))),
   CREATE_TIME          TIMESTAMP             not null,
   MODIFIED_TIME        TIMESTAMP,
   CREATE_NAME          VARCHAR2(255)         not null,
   CREATE_ID            VARCHAR2(40)          not null,
   ACT_POSTER_URL       VARCHAR2(255)         null,
   DEFAULT_IMAGE        VARCHAR2(255)         null,
   DIS_GROUP_ID         VARCHAR2(40)          not null,
   constraint PK_AT_ACTIVITY primary key (ID)
);

comment on column AT_ACTIVITY.PHONE is
'是否有（1 是/0 否）';

comment on column AT_ACTIVITY.NAME is
'是否有（1 是/0  否）';

comment on column AT_ACTIVITY.ID_CARD is
'是否有（1 是/0  否）';

comment on column AT_ACTIVITY.REMARK is
'是否有（1 是/0  否）';

comment on column AT_ACTIVITY.SEX is
'是否有（1 是/0  否）';

/*==============================================================*/
/* Table: AT_ENTER                                              */
/*==============================================================*/
create table AT_ENTER (
   ENTER_ID             VARCHAR2(40)          not null,
   ACT_ID               VARCHAR2(40)          not null,
   PHONE                VARCHAR2(255)         null,
   NAME                 VARCHAR2(255)         null,
   DEPT_NAME            VARCHAR2(255)         null,
   ID_CARD              VARCHAR2(255)         null,
   REMARK               VARCHAR2(1000)        null,
   SEX                  VARCHAR2(255)         null,
   ENTER_TIME           TIMESTAMP             not null,
   ECID                 VARCHAR2(40)          null,
   ENTER_PERSON_ID      VARCHAR2(40)          not null,
   constraint PK_AT_ENTER primary key (ENTER_ID)
);

/*==============================================================*/
/* Table: AT_PHOTO                                              */
/*==============================================================*/
create table AT_PHOTO (
   PHONE_ID             VARCHAR2(40)          not null,
   ACT_ID               VARCHAR2(40)          not null,
   PHONE_ROUTE          VARCHAR2(255)         not null,
   SORT                 DECIMAL(10)           default 0 not null ,
   CREATE_TIME          TIMESTAMP             not null,
   constraint PK_AT_PHOTO primary key (PHONE_ID)
);

/*==============================================================*/
/* Table: AT_PRIVILEGE                                          */
/*==============================================================*/
create table AT_PRIVILEGE (
   ID                   VARCHAR2(40)          not null,
   ECID                 VARCHAR2(40)          not null,
   ACT_ID               VARCHAR2(40)          not null,
   TYPE                 VARCHAR2(64)          not null,
   ENTITY_ID            VARCHAR2(40)          not null,
   ENTITY_NAME          VARCHAR2(255)         not null,
   AUTHR_TIME           TIMESTAMP             not null,
   DEPT_ORDER           VARCHAR2(1000)        null,
   PRIV                 VARCHAR2(64)          default 'user' null,
   constraint PK_AT_PRIVILEGE primary key (ID)
);

comment on column AT_PRIVILEGE.TYPE is
'user：用户 dept：部门';

comment on column AT_PRIVILEGE.DEPT_ORDER is
'授权类型为部门时，部门所在的序号，用于查询子部门权限';

comment on column AT_PRIVILEGE.PRIV is
'admin:管理员 service:服务人员 user:普通用户';

