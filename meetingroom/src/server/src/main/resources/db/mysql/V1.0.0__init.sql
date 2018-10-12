/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/7/6 16:27:44                            */
/*==============================================================*/


/*==============================================================*/
/* Table: MR_FAVORITE                                           */
/*==============================================================*/
create table MR_FAVORITE (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null comment '用户ID',
   ROOM_ID              VARCHAR(40)          not null comment '会议室ID',
   FAV_TIME             TIMESTAMP            null comment '收藏时间',
   constraint PK_MR_FAVORITE primary key (ID)
)CHARSET=utf8 comment='个人收藏夹';

/*==============================================================*/
/* Table: MR_PRIVILEGE                                          */
/*==============================================================*/
create table MR_PRIVILEGE (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   TYPE                 VARCHAR(64)          not null comment 'user：用户 dept：部门',
   ENTITY_ID            VARCHAR(40)          not null,
   ENTITY_NAME          VARCHAR(255)         not null,
   AUTHR_TIME           TIMESTAMP            not null,
   DEPT_ORDER           VARCHAR(1000)        comment '授权类型为部门时，部门所在的序号，用于查询子部门权限',
   PRIV                 VARCHAR(64)          comment 'admin:管理员 service:服务人员 user:普通用户 leader:领导',
   constraint PK_MR_PRIVILEGE primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MR_RESERVED                                           */
/*==============================================================*/
create table MR_RESERVED (
   ID                    VARCHAR(40)          not null,
   ECID                  VARCHAR(40)          not null,
   ROOM_ID               VARCHAR(40)          not null,
   RESERVED_USER_ID      VARCHAR(40)          not null,
   RESERVED_USER_NAME    VARCHAR(255)         null,
   RESERVED_USER_DEPT    VARCHAR(255)         null,
   RESERVED_TIME         TIMESTAMP            null,
   ORDER_TIME_BEGIN      TIMESTAMP            null,
   ORDER_TIME_END        TIMESTAMP            null,
   ORDER_DURATION        DECIMAL(10)          not null default 0,
   MEETING_NAME          VARCHAR(255)         null,
   STATUS                CHAR(1)              null comment '1-准备中 2-使用中 3-已结束 4-已取消 0-已删除 a-审批中 r-审批拒绝',
   RESERVED_USER_DEPT_ID VARCHAR(40)          default '-1' not null,
   NEED_APPROVE          VARCHAR(5)           comment '预订是否需要审批',
   REMARKS               VARCHAR(512),
   ORG_DEPT_ID           VARCHAR(40),
   ORG_DEPT_ORDER        VARCHAR(64),
   PARTICIPANTS_NUM      DECIMAL(10)          comment '参会人数',
   DEL_FLAG              CHAR(1)              default '0' comment '删除标识',
   RES_REMARK            VARCHAR(512),
   constraint PK_MR_RESERVED primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MR_ROOM                                               */
/*==============================================================*/
create table MR_ROOM (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   ADDRESS              VARCHAR(255)         null,
   AREA                 DECIMAL(10)          not null default 0 comment 'm2',
   CAPACITY             DECIMAL(10)          not null default 0 comment '人',
   PROJECTOR            enum('1','0')        not null default '1' comment '是否有（1 是/0  否）',
   DISPLAY              enum('1','0')        not null default '1' comment '是否有（1 是/0 否）',
   MICROPHONE           enum('1','0')        not null default '1' comment '是否有（1 是/0 否）',
   STEREO               enum('1','0')        not null default '1' comment '是否有（1 是/0 否）',
   WIFI                 enum('1','0')        not null default '1' comment '是否有（1 是/0 否）',
   LAYOUT               varchar(255)         null comment '布局图文件ID',
   REMARKS              VARCHAR(512)         null comment '其他说明',
   STATUS               enum('1','0')        not null default '1' comment '是否启用',
   CREATE_TIME          TIMESTAMP,
   MODIFIED_TIME        TIMESTAMP,
   ORG_DEPT_ID          VARCHAR(40),
   ORG_DEPT_ORDER       VARCHAR(64),
   constraint PK_MR_ROOM primary key (ID)
)CHARSET=utf8;
