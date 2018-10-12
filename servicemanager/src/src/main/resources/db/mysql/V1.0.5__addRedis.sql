/*==============================================================*/
/* Table: SM_REDIS                                              */
/*==============================================================*/
create table SM_REDIS (
   ID                   VARCHAR(40)          not null,
   REMARKS              VARCHAR(64)          null,
   HOST                 VARCHAR(64)          not null,
   PORT                 NUMERIC(10)          not null default 0,
   DB_INDEX             NUMERIC(10)          not null default 0 comment 'Redis数据库索引（默认为0）',
   USER_NAME            VARCHAR(64)          null,
   PASSWORD             VARCHAR(64)          null comment '加密后的密码',
   CREATE_TIME          DATE                 null,
   CREATOR              VARCHAR(40)          not null,
   ENABLED              enum('1','0')        not null default '1' comment '启用状态',
   constraint PK_SM_REDIS primary key (ID)
)CHARSET=utf8;

