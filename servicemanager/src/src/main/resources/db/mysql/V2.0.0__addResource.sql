/*==============================================================*/
/* Table: SM_RESOURCE_INFO                                      */
/*==============================================================*/
create table SM_RESOURCE_INFO (
   ID                   VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   REMARKS              VARCHAR(512)         null,
   TYPE                 CHAR(1)              not null comment '1-数据库,2-redis,3-第三方接入',
   constraint PK_SM_RESOURCE_INFO primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_RESOURCE_INFOITEM                                  */
/*==============================================================*/
create table SM_RESOURCE_INFOITEM (
   ID                   VARCHAR(40)          not null,
   INFO_ID              VARCHAR(40)          not null,
   `KEY`                VARCHAR(64)          not null,
   NAME                 VARCHAR(255)         not null,
   REMARKS              VARCHAR(512)         null,
   TYPE                 VARCHAR(64)          not null comment '类型，text 文本,radio 单选,checkbox 多选',
   REGEX                VARCHAR(512)         null,
   OPTIONS              VARCHAR(1000)        null comment '#radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    ',
   GROUP_NAME           VARCHAR(255)         null comment '分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序',
   CONTROL_SIZE         NUMERIC(10)          not null default 0 comment '用于设置控件大小',
   DEFAULT_VALUE        VARCHAR(1000)        null,
   SEQU                 NUMERIC(10)          not null default 0 comment '顺序排列',
   constraint PK_SM_RESOURCE_INFOITEM primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_RESOURCE                                           */
/*==============================================================*/
create table SM_RESOURCE (
   ID                   VARCHAR(40)          not null comment 'ID',
   RES_ID               VARCHAR(40)          not null comment '资源ID',
   NAME                 VARCHAR(255)         not null comment '服务ID',
   REMARKS              VARCHAR(512)         null,
   CREATOR              VARCHAR(40)          not null,
   CREATE_TIME          DATE                 null,
   MODIFIER             VARCHAR(40)          not null,
   MODIFY_TIME          DATE                 null,
   ENABLED              enum('1','0')        not null default '1' comment '启用状态',
   constraint PK_SM_RESOURCE primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_RESOURCE_CONFIG                                    */
/*==============================================================*/
create table SM_RESOURCE_CONFIG (
   ID                   VARCHAR(40)          not null,
   RESOURCE_ID          VARCHAR(40)          null,
   PARAM_KEY            VARCHAR(64)          null comment '参数key',
   PARAM_VALUE          VARCHAR(1000)        null comment '参数值',
   CONFIG_VER           NUMERIC(10)          not null default 0 comment '配置版本好，数字，累加',
   ACTIVED              enum('1','0')        not null default '0' comment '活动为当前有效的配置',
   SETUP_USER           VARCHAR(40)          not null,
   SETUP_TIME           DATE                 null,
   constraint PK_SM_RESOURCE_CONFIG primary key (ID)
)CHARSET=utf8;

alter table SM_RESOURCE_ASSIGN
    add column REOURCE_ID           VARCHAR(40)          null;