/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/8/16 10:40:39                           */
/*==============================================================*/


/*==============================================================*/
/* Table: MD_PARTICIPANTS                                       */
/*==============================================================*/
create table MD_PARTICIPANTS (
   ID                   VARCHAR(40)          not null,
   TYPE                 VARCHAR(64)          not null,
   OBJECT_ID            VARCHAR(40)          not null,
   OBJECT_NAME          VARCHAR(255)         not null,
   ENTITY_TYPE          VARCHAR(64)          not null,
   ENTITY_ID            VARCHAR(40)          not null,
   ENTITY_NAME          VARCHAR(255)         not null,
   DEPT_ORDER           VARCHAR(1000)        null,
   constraint PK_MD_PARTICIPANTS primary key (ID)
);

comment on table MD_PARTICIPANTS is
'通用型人员设置信息';

comment on column MD_PARTICIPANTS.TYPE is
'参加对象类型';

comment on column MD_PARTICIPANTS.OBJECT_ID is
'参与人员说参加的对象ID';

comment on column MD_PARTICIPANTS.OBJECT_NAME is
'参与人员说参加的对象名称';

comment on column MD_PARTICIPANTS.ENTITY_TYPE is
'user：用户 dept：部门';

/*==============================================================*/
/* Table: MT_AGENDA                                             */
/*==============================================================*/
create table MT_AGENDA (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   BEGIN_TIME           TIMESTAMP            not null,
   END_TIME             TIMESTAMP            not null,
   ADDRESS              VARCHAR(255)         not null,
   REMARKS              VARCHAR(512)         null,
   constraint PK_MT_AGENDA primary key (ID)
);

/*==============================================================*/
/* Table: MT_ATTACHMENT                                         */
/*==============================================================*/
create table MT_ATTACHMENT (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   FILE_PATH            VARCHAR(255)         not null,
   FILE_NAME            VARCHAR(255)         not null,
   CONTENT_TYPE         VARCHAR(64)          null,
   SIZE                 DECIMAL(10)          not null default 0,
   PRIVILEGE            DECIMAL(10)          not null default 0,
   constraint PK_MT_ATTACHMENT primary key (ID)
);

comment on column MT_ATTACHMENT.PRIVILEGE is
'1：查看 3：下载（含查看）';

/*==============================================================*/
/* Table: MT_MEETING                                            */
/*==============================================================*/
create table MT_MEETING (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   ADDRESS              VARCHAR(255)         null,
   BEGIN_TIME           TIMESTAMP            not null,
   END_TIME             TIMESTAMP            not null,
   SPONSOR              VARCHAR(40)          not null,
   CREATE_TIME          TIMESTAMP            not null,
   STATUS               VARCHAR(64)          not null default '10',
   RELEASE_TIME         TIMESTAMP            null,
   constraint PK_MT_MEETING primary key (ID)
);

comment on column MT_MEETING.STATUS is
'草稿：10 未进行：20 进行中：30 已取消：40 已结束：50';

/*==============================================================*/
/* Table: MT_PARTICIPANTS                                       */
/*==============================================================*/
create table MT_PARTICIPANTS (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   PERSON_TYPE          VARCHAR(64)          not null default 'inner',
   PERSON_ID            VARCHAR(40)          not null,
   PERSON_NAME          VARCHAR(255)         not null,
   QRCODE               VARCHAR(512)         null,
   NOTICE_STATUS        CHAR(1)              null default '0',
   constraint PK_MT_PARTICIPANTS primary key (ID)
);

comment on table MT_PARTICIPANTS is
'与会人员记录由参与人员的人员信息生成';

comment on column MT_PARTICIPANTS.PERSON_TYPE is
'inner：内部人员 outer：外部人员';

comment on column MT_PARTICIPANTS.PERSON_ID is
'内容人员为用户id，外部人员为手机号';

comment on column MT_PARTICIPANTS.QRCODE is
'签到二维码图片的path';

comment on column MT_PARTICIPANTS.NOTICE_STATUS is
'0：未通知 1：已发送 2：已送达';

/*==============================================================*/
/* Table: MT_REMARKS                                            */
/*==============================================================*/
create table MT_REMARKS (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   REMARKS              VARCHAR(512)         not null,
   SEQU                 DECIMAL(10)          not null default 0,
   constraint PK_MT_REMARKS primary key (ID)
);

comment on column MT_REMARKS.SEQU is
'升序';

/*==============================================================*/
/* Table: MT_SIGNIN_RECORD                                      */
/*==============================================================*/
create table MT_SIGNIN_RECORD (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   SEQU_ID              VARCHAR(40)          not null,
   PERSON_ID            VARCHAR(40)          not null,
   PERSON_NAME          VARCHAR(255)         not null,
   SIGNED               VARCHAR(64)          not null default 'N',
   SIGN_TIME            TIMESTAMP            null,
   SERV_ID              VARCHAR(40)          null,
   constraint PK_MT_SIGNIN_RECORD primary key (ID)
);

comment on column MT_SIGNIN_RECORD.SIGNED is
'Y：是 N：否';

/*==============================================================*/
/* Table: MT_SIGNIN_SEQU                                        */
/*==============================================================*/
create table MT_SIGNIN_SEQU (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   REMARKS              VARCHAR(512)         not null,
   SEQU                 DECIMAL(10)          not null default 1,
   constraint PK_MT_SIGNIN_SEQU primary key (ID)
);

comment on column MT_SIGNIN_SEQU.SEQU is
'升序';

/*==============================================================*/
/* Table: MT_SIGNIN_SERV                                        */
/*==============================================================*/
create table MT_SIGNIN_SERV (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null,
   USER_NAME            VARCHAR(255)         not null,
   constraint PK_MT_SIGNIN_SERV primary key (ID)
);

comment on table MT_SIGNIN_SERV is
'与会人员记录由参与人员的人员信息生成';

