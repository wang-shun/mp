

/*==============================================================*/
/* Table: MD_PARTICIPANTS                                       */
/*==============================================================*/
create table MD_PARTICIPANTS (
   ID                   VARCHAR(40)          not null,
   TYPE                 VARCHAR(64)          not null comment '参加对象类型',
   OBJECT_ID            VARCHAR(40)          not null comment '参与人员说参加的对象ID',
   OBJECT_NAME          VARCHAR(255)         not null comment '参与人员说参加的对象名称',
   ENTITY_TYPE          VARCHAR(64)          not null comment 'user：用户 dept：部门',
   ENTITY_ID            VARCHAR(40)          not null,
   ENTITY_NAME          VARCHAR(255)         not null,
   DEPT_ORDER           VARCHAR(1000)        null,
   PARENT_ID            VARCHAR(64),
   constraint PK_MD_PARTICIPANTS primary key (ID)
)CHARSET=utf8 comment='通用型人员设置信息';

/*==============================================================*/
/* Table: MT_AGENDA                                             */
/*==============================================================*/
create table MT_AGENDA (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   BEGIN_TIME           TIMESTAMP            not null,
   END_TIME             TIMESTAMP            not null,
   ADDRESS              VARCHAR(255)         ,
   REMARKS              VARCHAR(512)         null,
   constraint PK_MT_AGENDA primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MT_ATTACHMENT                                         */
/*==============================================================*/
create table MT_ATTACHMENT (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   FILE_PATH            VARCHAR(255)         not null,
   FILE_NAME            VARCHAR(255)         not null,
   CONTENT_TYPE         VARCHAR(64)          null,
   FILE_SIZE            DECIMAL(20,6)        not null default 0 comment 'KB',
   UPLOAD_TIME          TIMESTAMP            null,
   PRIVILEGE            DECIMAL(10)          not null default 0 comment '1：查看 3：下载（含查看）',
   constraint PK_MT_ATTACHMENT primary key (ID)
)CHARSET=utf8;

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
   HAS_GROUP            enum('1','0')        not null default '0',
   GROUP_ID             VARCHAR(64)          null comment 'IM群组的ID',
   SPONSOR              VARCHAR(40)          not null,
   CREATE_TIME          TIMESTAMP            not null,
   STATUS               VARCHAR(64)          not null default '10' comment '草稿：10 未进行：20 进行中：30 已取消：40 已结束：50',
   RELEASE_TIME         TIMESTAMP            null,
   ORG_DEPT_ID          VARCHAR(40),
   ORG_DEPT_ORDER       VARCHAR(64),
   QRCODE               VARCHAR(64),
   NOTICE_TYPE          CHAR(1)              comment '0 不提醒 1 提前N分钟提醒 2 提前N小时提醒',
   NOTICE_SET           DECIMAL(10)          default 0 not null,
   NOTICE_TIME          TIMESTAMP,
   SIGN_TYPE            VARCHAR(64)          comment 'open：开放式     close：封闭式',
   constraint PK_MT_MEETING primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MT_PARTICIPANTS                                       */
/*==============================================================*/
create table MT_PARTICIPANTS (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   PERSON_TYPE          VARCHAR(64)          not null default 'inner' comment 'inner：内部人员 outer：外部人员',
   PERSON_ID            VARCHAR(40)          not null comment '内容人员为用户id，外部人员为手机号',
   PERSON_NAME          VARCHAR(255)         not null,
   QRCODE               VARCHAR(512)         null comment '签到二维码图片的path',
   NOTICE_STATUS        CHAR(1)              null default '0' comment '0：未通知 1：已发送 2：已送达',
   VISIBLE              VARCHAR(5)           default '1',
   constraint PK_MT_PARTICIPANTS primary key (ID)
)CHARSET=utf8 comment='与会人员记录由参与人员的人员信息生成';

/*==============================================================*/
/* Table: MT_REMARKS                                            */
/*==============================================================*/
create table MT_REMARKS (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   REMARKS              VARCHAR(512)         not null,
   SEQU                 DECIMAL(10)          not null default 0 comment '升序',
   constraint PK_MT_REMARKS primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MT_SIGNIN_RECORD                                      */
/*==============================================================*/
create table MT_SIGNIN_RECORD (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   SEQU_ID              VARCHAR(40)          not null,
   PERSON_ID            VARCHAR(40)          not null,
   PERSON_NAME          VARCHAR(255)         not null,
   SIGNED               VARCHAR(64)          not null default 'N' comment 'Y：是 N：否',
   SIGN_TIME            TIMESTAMP            null,
   SERV_ID              VARCHAR(40)          null,
   PERSON_TYPE          VARCHAR(64)          comment 'inner：内部人员 outer：外部人员',
   constraint PK_MT_SIGNIN_RECORD primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MT_SIGNIN_SEQU                                        */
/*==============================================================*/
create table MT_SIGNIN_SEQU (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   REMARKS              VARCHAR(512)         not null,
   SEQU                 DECIMAL(10)          not null default 1 comment '升序',
   QRCODE               VARCHAR(64),
   constraint PK_MT_SIGNIN_SEQU primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MT_SIGNIN_SERV                                        */
/*==============================================================*/
create table MT_SIGNIN_SERV (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null,
   USER_NAME            VARCHAR(255)         not null,
   constraint PK_MT_SIGNIN_SERV primary key (ID)
)CHARSET=utf8 comment='与会人员记录由参与人员的人员信息生成';

