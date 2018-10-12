/*==============================================================*/
/* Table: SM_RESOURCE_INFO                                      */
/*==============================================================*/
create table SM_RESOURCE_INFO (
   ID                   VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   REMARKS              VARCHAR(512)         null,
   TYPE                 CHAR(1)              not null,
   constraint PK_SM_RESOURCE_INFO primary key (ID)
);

comment on column SM_RESOURCE_INFO.TYPE is
'1-数据库,2-redis,3-第三方接入';

/*==============================================================*/
/* Table: SM_RESOURCE_INFOITEM                                  */
/*==============================================================*/
create table SM_RESOURCE_INFOITEM (
   ID                   VARCHAR(40)          not null,
   INFO_ID              VARCHAR(40)          not null,
   KEY                  VARCHAR(64)          not null,
   NAME                 VARCHAR(255)         not null,
   REMARKS              VARCHAR(512)         null,
   TYPE                 VARCHAR(64)          not null,
   REGEX                VARCHAR(512)         null,
   OPTIONS              VARCHAR(1000)        null,
   GROUP_NAME           VARCHAR(255)         null,
   CONTROL_SIZE         NUMERIC(10)          not null default 0,
   DEFAULT_VALUE        VARCHAR(1000)        null,
   SEQU                 NUMERIC(10)          not null default 0,
   constraint PK_SM_RESOURCE_INFOITEM primary key (ID)
);

comment on column SM_RESOURCE_INFOITEM.TYPE is
'类型，text 文本,radio 单选,checkbox 多选';

comment on column SM_RESOURCE_INFOITEM.OPTIONS is
'#radio或者checkbox时的选项，可为空， 例子file:文件系统,fdfs:FastDFS    ';

comment on column SM_RESOURCE_INFOITEM.GROUP_NAME is
'分组的名称排序依据sequ来，取同一分组下的最小的sequ进行排序';

comment on column SM_RESOURCE_INFOITEM.CONTROL_SIZE is
'用于设置控件大小';

comment on column SM_RESOURCE_INFOITEM.SEQU is
'顺序排列';

/*==============================================================*/
/* Table: SM_RESOURCE                                           */
/*==============================================================*/
create table SM_RESOURCE (
   ID                   VARCHAR(40)          not null,
   RES_ID               VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   REMARKS              VARCHAR(512)         null,
   CREATOR              VARCHAR(40)          not null,
   CREATE_TIME          DATE                 null,
   MODIFIER             VARCHAR(40)          not null,
   MODIFY_TIME          DATE                 null,
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_SM_RESOU check (ENABLED in ('1','0')),
   constraint PK_SM_RESOURCE primary key (ID)
);

comment on column SM_RESOURCE.ID is
'ID';

comment on column SM_RESOURCE.RES_ID is
'资源ID';

comment on column SM_RESOURCE.NAME is
'服务ID';

comment on column SM_RESOURCE.ENABLED is
'启用状态';

/*==============================================================*/
/* Table: SM_RESOURCE_CONFIG                                    */
/*==============================================================*/
create table SM_RESOURCE_CONFIG (
   ID                   VARCHAR(40)          not null,
   RESOURCE_ID          CHAR(10)             null,
   PARAM_KEY            VARCHAR(64)          null,
   PARAM_VALUE          VARCHAR(1000)        null,
   CONFIG_VER           NUMERIC(10)          not null default 0,
   ACTIVED              VARCHAR(5)           not null default '0'
      constraint CKC_ACTIVED_SM_RESOU check (ACTIVED in ('1','0')),
   SETUP_USER           VARCHAR(40)          not null,
   SETUP_TIME           DATE                 null,
   constraint PK_SM_RESOURCE_CONFIG primary key (ID)
);

comment on column SM_RESOURCE_CONFIG.PARAM_KEY is
'参数key';

comment on column SM_RESOURCE_CONFIG.PARAM_VALUE is
'参数值';

comment on column SM_RESOURCE_CONFIG.CONFIG_VER is
'配置版本好，数字，累加';

comment on column SM_RESOURCE_CONFIG.ACTIVED is
'活动为当前有效的配置';

alter table SM_RESOURCE_ASSIGN
    add column REOURCE_ID           VARCHAR(40)          null;