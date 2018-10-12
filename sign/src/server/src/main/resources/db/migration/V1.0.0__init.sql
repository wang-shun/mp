/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/1/18 10:22:35                           */
/*==============================================================*/


/*==============================================================*/
/* Table: SN_SIGN                                               */
/*==============================================================*/
create table SN_SIGN (
   ECID                 VARCHAR(64)          not null,
   SIGN_ID              VARCHAR(40)          not null,
   SIGN_TIME            TIMESTAMP            null,
   LONGITUDE            VARCHAR(64)          null,
   LATITUDE             VARCHAR(64)          null,
   ADDRESS              VARCHAR(255)         null,
   CITY                 VARCHAR(64)          null,
   CONTENT              VARCHAR(512)         null,
   CREATOR              VARCHAR(40)          not null,
   CREATOR_NAME         VARCHAR(255)         null,
   CREATE_TIME          TIMESTAMP            null,
   DEP_ID               VARCHAR(40)          null,
   DEP_ORDER            VARCHAR(512)         null,
   TASK_NO              VARCHAR(64)          null,
   STATE                CHAR(1)              null,
   constraint PK_SN_SIGN primary key (SIGN_ID)
);

comment on column SN_SIGN.ECID is
'原应用中为org_id';

comment on column SN_SIGN.STATE is
'暂时不用';

/*==============================================================*/
/* Table: SN_SIGN_IMAGE                                         */
/*==============================================================*/
create table SN_SIGN_IMAGE (
   SIGN_IMAGE_ID        VARCHAR(40)          not null,
   SIGN_ID              VARCHAR(40)          not null,
   IMAGE                VARCHAR(255)         not null,
   constraint PK_SN_SIGN_IMAGE primary key (SIGN_IMAGE_ID)
);

