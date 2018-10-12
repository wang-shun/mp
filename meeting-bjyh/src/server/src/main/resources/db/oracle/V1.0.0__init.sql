/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/2/16 17:06:47                           */
/*==============================================================*/


/*==============================================================*/
/* Table: MD_PARTICIPANTS                                       */
/*==============================================================*/
create table MD_PARTICIPANTS 
(
   ID                   VARCHAR2(40)         not null,
   TYPE                 VARCHAR2(64)         not null,
   OBJECT_ID            VARCHAR2(40)         not null,
   OBJECT_NAME          VARCHAR2(255)        not null,
   ENTITY_TYPE          VARCHAR2(64)         not null,
   ENTITY_ID            VARCHAR2(40)         not null,
   ENTITY_NAME          VARCHAR2(255)        not null,
   DEPT_ORDER           VARCHAR2(1000),
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

comment on column MD_PARTICIPANTS.DEPT_ORDER is
'说选择部门的order或者人员所在部门的order';

/*==============================================================*/
/* Table: MR_APPROVE_INFO                                       */
/*==============================================================*/
create table MR_APPROVE_INFO 
(
   RESERVED_ID          VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null,
   APPROVED             VARCHAR(5)           default '0' not null
      constraint CKC_APPROVED_MR_APPRO check (APPROVED in ('1','0')),
   APPROVE_RESULT       VARCHAR(5)           default '1'
      constraint CKC_APPROVE_RESULT_MR_APPRO check (APPROVE_RESULT is null or (APPROVE_RESULT in ('1','0'))),
   APPROVE_TIME         TIMESTAMP,
   constraint PK_MR_APPROVE_INFO primary key (RESERVED_ID, USER_ID)
);

comment on column MR_APPROVE_INFO.APPROVE_RESULT is
'是否通过';

/*==============================================================*/
/* Table: MR_FAVORITE                                           */
/*==============================================================*/
create table MR_FAVORITE 
(
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   FAV_TIME             TIMESTAMP,
   constraint PK_MR_FAVORITE primary key (ID)
);

comment on table MR_FAVORITE is
'个人收藏夹';

comment on column MR_FAVORITE.USER_ID is
'用户ID';

comment on column MR_FAVORITE.ROOM_ID is
'会议室ID';

comment on column MR_FAVORITE.FAV_TIME is
'收藏时间';

/*==============================================================*/
/* Table: MR_PRIVILEGE                                          */
/*==============================================================*/
create table MR_PRIVILEGE 
(
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   TYPE                 VARCHAR(64)          not null,
   ENTITY_ID            VARCHAR(40)          not null,
   ENTITY_NAME          VARCHAR(255)         not null,
   AUTHR_TIME           TIMESTAMP            not null,
   DEPT_ORDER           VARCHAR(1000),
   PRIV                 VARCHAR(64)          default 'user',
   constraint PK_MR_PRIVILEGE primary key (ID)
);

comment on column MR_PRIVILEGE.TYPE is
'user：用户 dept：部门';

comment on column MR_PRIVILEGE.DEPT_ORDER is
'授权类型为部门时，部门所在的序号，用于查询子部门权限';

comment on column MR_PRIVILEGE.PRIV is
'admin:管理员 service:服务人员 user:普通用户';

/*==============================================================*/
/* Table: MR_RESERVED                                           */
/*==============================================================*/
create table MR_RESERVED 
(
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   RESERVED_USER_ID     VARCHAR(40)          not null,
   RESERVED_USER_NAME   VARCHAR(255),
   RESERVED_USER_DEPT_ID VARCHAR(40)          not null,
   RESERVED_USER_DEPT   VARCHAR(255),
   RESERVED_TIME        TIMESTAMP,
   ORDER_TIME_BEGIN     TIMESTAMP,
   ORDER_TIME_END       TIMESTAMP,
   ORDER_DURATION       DECIMAL(10)          default 0 not null,
   MEETING_NAME         VARCHAR(255),
   REMARKS              VARCHAR(512),
   STATUS               CHAR(1),
   NEED_APPROVE         VARCHAR(5)           default '0' not null
      constraint CKC_NEED_APPROVE_MR_RESER check (NEED_APPROVE in ('1','0')),
   constraint PK_MR_RESERVED primary key (ID)
);

comment on column MR_RESERVED.STATUS is
'1-准备中 2-使用中 3-已结束 4-已取消 0-已删除 a-审批中 r-审批拒绝';

comment on column MR_RESERVED.NEED_APPROVE is
'预订是否需要审批';

/*==============================================================*/
/* Table: MR_ROOM                                               */
/*==============================================================*/
create table MR_ROOM 
(
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   ADDRESS              VARCHAR(255),
   AREA                 DECIMAL(10)          default 0 not null,
   CAPACITY             DECIMAL(10)          default 0 not null,
   PROJECTOR            VARCHAR(5)           default '1' not null
      constraint CKC_PROJECTOR_MR_ROOM check (PROJECTOR in ('1','0')),
   DISPLAY              VARCHAR(5)           default '1' not null
      constraint CKC_DISPLAY_MR_ROOM check (DISPLAY in ('1','0')),
   MICROPHONE           VARCHAR(5)           default '1' not null
      constraint CKC_MICROPHONE_MR_ROOM check (MICROPHONE in ('1','0')),
   STEREO               VARCHAR(5)           default '1' not null
      constraint CKC_STEREO_MR_ROOM check (STEREO in ('1','0')),
   WIFI                 VARCHAR(5)           default '1' not null
      constraint CKC_WIFI_MR_ROOM check (WIFI in ('1','0')),
   LAYOUT               VARCHAR(255),
   REMARKS              VARCHAR(512),
   STATUS               VARCHAR(5)           default '1' not null
      constraint CKC_STATUS_MR_ROOM check (STATUS in ('1','0')),
   CREATE_TIME          TIMESTAMP            default NULL,
   MODIFIED_TIME        TIMESTAMP            default NULL,
   constraint PK_MR_ROOM primary key (ID)
);

comment on column MR_ROOM.AREA is
'm2';

comment on column MR_ROOM.CAPACITY is
'人';

comment on column MR_ROOM.PROJECTOR is
'是否有（1 是/0  否）';

comment on column MR_ROOM.DISPLAY is
'是否有（1 是/0 否）';

comment on column MR_ROOM.MICROPHONE is
'是否有（1 是/0 否）';

comment on column MR_ROOM.STEREO is
'是否有（1 是/0 否）';

comment on column MR_ROOM.WIFI is
'是否有（1 是/0 否）';

comment on column MR_ROOM.LAYOUT is
'布局图文件服务路径';

comment on column MR_ROOM.REMARKS is
'其他说明';

comment on column MR_ROOM.STATUS is
'是否启用';

/*==============================================================*/
/* Table: MT_AGENDA                                             */
/*==============================================================*/
create table MT_AGENDA 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   BEGIN_TIME           TIMESTAMP            not null,
   END_TIME             TIMESTAMP            not null,
   ADDRESS              VARCHAR2(255),
   REMARKS              VARCHAR2(512),
   constraint PK_MT_AGENDA primary key (ID)
);

/*==============================================================*/
/* Table: MT_ATTACHMENT                                         */
/*==============================================================*/
create table MT_ATTACHMENT 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   FILE_PATH            VARCHAR2(255)        not null,
   FILE_NAME            VARCHAR2(255)        not null,
   CONTENT_TYPE         VARCHAR2(64),
   FILE_SIZE            DECIMAL(20,6)        default 0 not null,
   UPLOAD_TIME          TIMESTAMP,
   PRIVILEGE            NUMBER(10)           default 0 not null,
   constraint PK_MT_ATTACHMENT primary key (ID)
);

comment on column MT_ATTACHMENT.FILE_SIZE is
'KB';

comment on column MT_ATTACHMENT.PRIVILEGE is
'1：查看 3：下载（含查看）';

/*==============================================================*/
/* Table: MT_MEETING                                            */
/*==============================================================*/
create table MT_MEETING 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   NAME                 VARCHAR2(255)        not null,
   RESERVED_ID          VARCHAR2(40),
   ADDRESS              VARCHAR2(255),
   BEGIN_TIME           TIMESTAMP            not null,
   END_TIME             TIMESTAMP            not null,
   HAS_GROUP            VARCHAR2(5)          default '0' not null
      constraint CKC_HAS_GROUP_MT_MEETI check (HAS_GROUP in ('1','0')),
   GROUP_ID             VARCHAR2(64),
   SPONSOR              VARCHAR2(40)         not null,
   CREATE_TIME          TIMESTAMP            not null,
   STATUS               VARCHAR2(64)         default '10' not null,
   RELEASE_TIME         TIMESTAMP,
   constraint PK_MT_MEETING primary key (ID)
);

comment on column MT_MEETING.GROUP_ID is
'创建的聊天群组ID';

comment on column MT_MEETING.STATUS is
'草稿：10 未进行：20 进行中：30 已取消：40 已结束：50';

/*==============================================================*/
/* Table: MT_PARTICIPANTS                                       */
/*==============================================================*/
create table MT_PARTICIPANTS 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   PERSON_TYPE          VARCHAR2(64)         default 'inner' not null,
   PERSON_ID            VARCHAR2(40)         not null,
   PERSON_NAME          VARCHAR2(255)        not null,
   QRCODE               VARCHAR2(512),
   NOTICE_STATUS        CHAR(1)              default '0',
   VISIBLE              VARCHAR2(5)          default '1' not null
      constraint CKC_VISIBLE_MT_PARTI check (VISIBLE in ('1','0')),
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
create table MT_REMARKS 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   REMARKS              VARCHAR2(512)        not null,
   SEQU                 NUMBER(10)           default 0 not null,
   constraint PK_MT_REMARKS primary key (ID)
);

comment on column MT_REMARKS.SEQU is
'升序';

/*==============================================================*/
/* Table: MT_SIGNIN_RECORD                                      */
/*==============================================================*/
create table MT_SIGNIN_RECORD 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   SEQU_ID              VARCHAR2(40)         not null,
   PERSON_ID            VARCHAR2(40)         not null,
   PERSON_NAME          VARCHAR2(255)        not null,
   SIGNED               VARCHAR2(64)         default 'N' not null,
   SIGN_TIME            TIMESTAMP,
   SERV_ID              VARCHAR2(40),
   PERSON_TYPE          VARCHAR2(64)         default 'inner' not null,
   constraint PK_MT_SIGNIN_RECORD primary key (ID)
);

comment on column MT_SIGNIN_RECORD.SIGNED is
'Y：是 N：否';

comment on column MT_SIGNIN_RECORD.PERSON_TYPE is
'inner：内部人员 outer：外部人员';

/*==============================================================*/
/* Table: MT_SIGNIN_SEQU                                        */
/*==============================================================*/
create table MT_SIGNIN_SEQU 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   REMARKS              VARCHAR2(512)        not null,
   SEQU                 NUMBER(10)           default 1 not null,
   constraint PK_MT_SIGNIN_SEQU primary key (ID)
);

comment on column MT_SIGNIN_SEQU.SEQU is
'升序';

/*==============================================================*/
/* Table: MT_SIGNIN_SERV                                        */
/*==============================================================*/
create table MT_SIGNIN_SERV 
(
   ID                   VARCHAR2(40)         not null,
   MEETING_ID           VARCHAR2(40)         not null,
   USER_ID              VARCHAR2(40)         not null,
   USER_NAME            VARCHAR2(255)        not null,
   constraint PK_MT_SIGNIN_SERV primary key (ID)
);

comment on table MT_SIGNIN_SERV is
'与会人员记录由参与人员的人员信息生成';

/*==============================================================*/
/* Table: OP_LOG                                                */
/*==============================================================*/
create table OP_LOG 
(
   ID                   VARCHAR(40)          not null,
   MODULE               VARCHAR(64)          not null,
   OBJECT               VARCHAR(64)          not null,
   OP                   VARCHAR(64)          not null,
   CONTENT              VARCHAR(4000)        not null,
   RESULT               VARCHAR(5)           default '1' not null
      constraint CKC_RESULT_OP_LOG check (RESULT in ('1','0')),
   ECID                 VARCHAR(40)          not null,
   DEPT_NAME            VARCHAR(255),
   DEPT_ID              VARCHAR(40)          not null,
   USER_NAME            VARCHAR(255)         not null,
   USER_ID              VARCHAR(40)          not null,
   OP_TIME              TIMESTAMP            not null,
   constraint PK_OP_LOG primary key (ID)
);

comment on column OP_LOG.MODULE is
'代码package';

comment on column OP_LOG.RESULT is
'Y：成功， N：失败';

