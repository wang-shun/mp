/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/1/18 10:22:35                           */
/*==============================================================*/


/*==============================================================*/
/* Table: SN_SIGN                                               */
/*==============================================================*/
create table SN_SIGN (
   ECID                 VARCHAR(64)          not null comment '原应用中为org_id',
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
   STATE                CHAR(1)              null comment '暂时不用',
   ORG_DEPT_ID          VARCHAR(40),
   ORG_DEPT_ORDER       VARCHAR(64),
   constraint PK_SN_SIGN primary key (SIGN_ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SN_SIGN_IMAGE                                         */
/*==============================================================*/
create table SN_SIGN_IMAGE (
   SIGN_IMAGE_ID        VARCHAR(40)          not null,
   SIGN_ID              VARCHAR(40)          not null,
   IMAGE                VARCHAR(255)         not null,
   constraint PK_SN_SIGN_IMAGE primary key (SIGN_IMAGE_ID)
)CHARSET=utf8;



create table SIGN_UPGRADE (
   UPGRADED             enum('1','0')     not null default '0',
   constraint sign_upgrade_pkey primary key (UPGRADED)   
)CHARSET=utf8;

insert into SIGN_UPGRADE values('0'); 
