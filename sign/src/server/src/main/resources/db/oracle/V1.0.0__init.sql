/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/1/18 10:23:03                           */
/*==============================================================*/


/*==============================================================*/
/* Table: SN_SIGN                                               */
/*==============================================================*/
create table SN_SIGN 
(
   ECID                 VARCHAR2(64 char)    not null,
   SIGN_ID              VARCHAR2(40 char)    not null,
   SIGN_TIME            TIMESTAMP,
   LONGITUDE            VARCHAR2(64 char),
   LATITUDE             VARCHAR2(64 char),
   ADDRESS              VARCHAR2(255 char),
   CITY                 VARCHAR2(64 char),
   CONTENT              VARCHAR2(512 char),
   CREATOR              VARCHAR2(40 char)    not null,
   CREATOR_NAME         VARCHAR2(255 char),
   CREATE_TIME          TIMESTAMP,
   DEP_ID               VARCHAR2(40 char),
   DEP_ORDER            VARCHAR2(512 char),
   TASK_NO              VARCHAR2(64 char),
   STATE                CHAR(1),
   constraint PK_SN_SIGN primary key (SIGN_ID)
);

comment on column SN_SIGN.ECID is
'原应用中为org_id';

comment on column SN_SIGN.STATE is
'暂时不用';

/*==============================================================*/
/* Table: SN_SIGN_IMAGE                                         */
/*==============================================================*/
create table SN_SIGN_IMAGE 
(
   SIGN_IMAGE_ID        VARCHAR2(40 char)    not null,
   SIGN_ID              VARCHAR2(40 char)    not null,
   IMAGE                VARCHAR2(255 char)   not null,
   constraint PK_SN_SIGN_IMAGE primary key (SIGN_IMAGE_ID)
);

