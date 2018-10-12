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
   USER_ID              VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   FAV_TIME             TIMESTAMP            null,
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
create table MR_PRIVILEGE (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   DEPT_ID              VARCHAR(40)          not null,
   DEPT_FULL_ID         VARCHAR(1000)        null,
   AUTHR_TIME           TIMESTAMP            null,
   constraint PK_MR_PRIVILEGE primary key (ID)
);

/*==============================================================*/
/* Table: MR_RESERVED                                           */
/*==============================================================*/
create table MR_RESERVED (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   RESERVED_USER_ID     VARCHAR(40)          not null,
   RESERVED_USER_NAME   VARCHAR(255)         null,
   RESERVED_USER_DEPT   VARCHAR(255)         null,
   RESERVED_TIME        TIMESTAMP            null,
   ORDER_TIME_BEGIN     TIMESTAMP            null,
   ORDER_TIME_END       TIMESTAMP            null,
   ORDER_DURATION       DECIMAL(10)          not null default 0,
   MEETING_NAME         VARCHAR(255)         null,
   STATUS               CHAR(1)              null,
   constraint PK_MR_RESERVED primary key (ID)
);

comment on column MR_RESERVED.STATUS is
'1-准备中 2-使用中 3-已结束 4-已取消 0-已删除';

/*==============================================================*/
/* Table: MR_ROOM                                               */
/*==============================================================*/
create table MR_ROOM (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   ADDRESS              VARCHAR(255)         null,
   AREA                 DECIMAL(10)          not null default 0,
   CAPACITY             DECIMAL(10)          not null default 0,
   PROJECTOR            VARCHAR(5)           not null default '1'
      constraint CKC_PROJECTOR_MR_ROOM check (PROJECTOR in ('1','0')),
   DISPLAY              VARCHAR(5)           not null default '1'
      constraint CKC_DISPLAY_MR_ROOM check (DISPLAY in ('1','0')),
   MICROPHONE           VARCHAR(5)           not null default '1'
      constraint CKC_MICROPHONE_MR_ROOM check (MICROPHONE in ('1','0')),
   STEREO               VARCHAR(5)           not null default '1'
      constraint CKC_STEREO_MR_ROOM check (STEREO in ('1','0')),
   WIFI                 VARCHAR(5)           not null default '1'
      constraint CKC_WIFI_MR_ROOM check (WIFI in ('1','0')),
   LAYOUT               VARCHAR(40)          null,
   REMARKS              VARCHAR(512)         null,
   STATUS               VARCHAR(5)           not null default '1'
      constraint CKC_STATUS_MR_ROOM check (STATUS in ('1','0')),
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
'布局图文件ID';

comment on column MR_ROOM.REMARKS is
'其他说明';

comment on column MR_ROOM.STATUS is
'是否启用';

