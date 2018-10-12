/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/3/9 18:36:14                            */
/*==============================================================*/


/*==============================================================*/
/* Table: FD_FEEDBACK                                           */
/*==============================================================*/
create table FD_FEEDBACK 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   USER_ID              VARCHAR2(64)         not null,
   USER_NAME            VARCHAR2(255)        not null,
   APP_ID               VARCHAR2(40)         not null,
   APP_NAME             VARCHAR2(255)        not null,
   APP_VER              VARCHAR2(64)         not null,
   APP_VER_FMT          VARCHAR2(255)        not null,
   DEVICE_NAME          VARCHAR2(255)        not null,
   OS_VER               VARCHAR2(64)         not null,
   FEEDBACK             VARCHAR2(4000)       not null,
   IMAGES               VARCHAR2(1000),
   CONTACK              VARCHAR2(64),
   SUBMIT_TIME          TIMESTAMP            not null,
   CONFIRM              VARCHAR2(5)          default '0' not null
      constraint CKC_CONFIRM_FD_FEEDB check (CONFIRM in ('1','0')),
   DEL_FLAG             VARCHAR2(5)          default '0' not null
      constraint CKC_DEL_FLAG_FD_FEEDB check (DEL_FLAG in ('1','0')),
   constraint PK_FD_FEEDBACK primary key (ID)
);

comment on column FD_FEEDBACK.USER_ID is
'loginid';

comment on column FD_FEEDBACK.IMAGES is
'逗号分隔的图片id列表';

comment on column FD_FEEDBACK.CONTACK is
'手机或者qq号、微信';

