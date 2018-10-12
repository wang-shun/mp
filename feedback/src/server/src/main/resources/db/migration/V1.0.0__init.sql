/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/3/9 18:35:11                            */
/*==============================================================*/


/*==============================================================*/
/* Table: FD_FEEDBACK                                           */
/*==============================================================*/
create table FD_FEEDBACK (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   USER_ID              VARCHAR(64)          not null,
   USER_NAME            VARCHAR(255)         not null,
   APP_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         not null,
   APP_VER              VARCHAR(64)          not null,
   APP_VER_FMT          VARCHAR(255)         not null,
   DEVICE_NAME          VARCHAR(255)         not null,
   OS_VER               VARCHAR(64)          not null,
   FEEDBACK             VARCHAR(4000)        not null,
   IMAGES               VARCHAR(1000)        null,
   CONTACK              VARCHAR(64)          null,
   SUBMIT_TIME          TIMESTAMP            not null,
   CONFIRM              VARCHAR(5)           not null default '0'
      constraint CKC_CONFIRM_FD_FEEDB check (CONFIRM in ('1','0')),
   DEL_FLAG             VARCHAR(5)           not null default '0'
      constraint CKC_DEL_FLAG_FD_FEEDB check (DEL_FLAG in ('1','0')),
   constraint PK_FD_FEEDBACK primary key (ID)
);

comment on column FD_FEEDBACK.USER_ID is
'loginid';

comment on column FD_FEEDBACK.IMAGES is
'逗号分隔的图片id列表';

comment on column FD_FEEDBACK.CONTACK is
'手机或者qq号、微信';

