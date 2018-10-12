/*==============================================================*/
/* Table: FD_FEEDBACK                                           */
/*==============================================================*/
create table FD_FEEDBACK (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   USER_ID              VARCHAR(64)          not null comment 'loginid',
   USER_NAME            VARCHAR(255)         not null,
   APP_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         not null,
   APP_VER              VARCHAR(64)          not null,
   APP_VER_FMT          VARCHAR(255)         not null,
   DEVICE_NAME          VARCHAR(255)         not null,
   OS_VER               VARCHAR(64)          not null,
   FEEDBACK             VARCHAR(4000)        not null,
   IMAGES               VARCHAR(1000)        null comment '逗号分隔的图片id列表',
   CONTACK              VARCHAR(64)          null comment '手机或者qq号、微信',
   SUBMIT_TIME          TIMESTAMP            not null,
   CONFIRM              enum('2','1','0')    not null default '0' comment '0:待处理,1:处理中,2:已处理',
   DEL_FLAG             enum('1','0')        not null default '0',
   SOLUTION             VARCHAR(1000)        null comment '解决方案',
   PROBLEM              VARCHAR(1000)        null comment '问题原因',
   CONFIRM_USER_ID      VARCHAR(64)          null,
   CONFIRM_USER_NAME    VARCHAR(255)         null,
   CONFIRM_TIME         TIMESTAMP,
   constraint PK_FD_FEEDBACK primary key (ID)
)CHARSET=utf8;

