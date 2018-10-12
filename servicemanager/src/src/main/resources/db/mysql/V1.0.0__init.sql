/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/3/23 14:27:15                           */
/*==============================================================*/


/*==============================================================*/
/* Table: SM_APP_CONFIG                                         */
/*==============================================================*/
create table SM_APP_CONFIG (
   ID                   VARCHAR(40)          not null,
   APP_ID               VARCHAR(40)          not null,
   PARAM_KEY            VARCHAR(64)          null comment '参数key',
   PARAM_VALUE          VARCHAR(1000)        null comment '参数值',
   CONFIG_VER           NUMERIC(10)          not null default 0 comment '配置版本好，数字，累加',
   ACTIVED              enum('1','0')        not null default '0' comment '活动为当前有效的配置',
   SETUP_USER           VARCHAR(40)          not null,
   SETUP_TIME           DATE                 null,
   constraint PK_SM_APP_CONFIG primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_DATABASE                                           */
/*==============================================================*/
create table SM_DATABASE (
   ID                   VARCHAR(40)          not null,
   DB_TYPE              VARCHAR(64)          null comment 'postgresql\oracle',
   HOST                 VARCHAR(64)          not null,
   PORT                 NUMERIC(10)          not null default 0,
   SID                  VARCHAR(64)          null,
   DB_NAME              VARCHAR(64)          null,
   USER_NAME            VARCHAR(64)          null,
   PASSWORD             VARCHAR(64)          null comment '加密后的密码',
   CREATE_TIME          DATE                 null,
   CREATOR              VARCHAR(40)          not null,
   ENABLED              enum('1','0')        not null default '1' comment '启用状态',
   constraint PK_SM_DATABASE primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_DB_ASSIGN                                          */
/*==============================================================*/
create table SM_DB_ASSIGN (
   DB_ID                VARCHAR(40)          not null comment '数据库ID',
   SVC_ID               VARCHAR(40)          not null comment '服务ID',
   SVC_NAME             VARCHAR(255)         null comment '服务名称',
   ASSIGN_TIME          DATE                 null comment '分配时间',
   constraint PK_SM_DB_ASSIGN primary key (DB_ID, SVC_ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_RESOURCE_ASSIGN                                    */
/*==============================================================*/
create table SM_RESOURCE_ASSIGN (
   APP_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         null,
   RES_ID               VARCHAR(40)          not null comment '依赖的资源应用ID',
   RES_CODE             VARCHAR(64)          not null comment '识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default',
   RES_CONFIG           VARCHAR(4000)        null comment 'json格式',
   RES_NAME             VARCHAR(255)         null,
   ASSIGN_TIME          DATE                 null,
   constraint PK_SM_RESOURCE_ASSIGN primary key (APP_ID, RES_ID, RES_CODE)
)CHARSET=utf8;

/*==============================================================*/
/* Table: SM_ROUTE                                              */
/*==============================================================*/
create table SM_ROUTE (
   ID                   VARCHAR(40)          not null,
   SERVICE_ID           VARCHAR(255)         null comment '服务对应的ServiceId',
   URL                  VARCHAR(512)         null comment '服务对应的访问路径',
   SERVICE_NAME         VARCHAR(255)         not null,
   PATH                 VARCHAR(255)         not null,
   STRIP_PREFIX         enum('1','0')        not null default '1',
   RETRYABLE            enum('1','0')        not null default '1',
   SENSITIVE_HEADERS    VARCHAR(1000)        null comment '逗号分隔',
   CUSTOM_POLICY        VARCHAR(64)          null,
   ENABLED              enum('1','0')        not null default '1' comment '启用状态',
   NEED_AUTH            enum('1','0')        not null default '1',
   constraint PK_SM_ROUTE primary key (ID)
)CHARSET=utf8 comment='ServiceId和服务URL只能选择一个，使用服务id则通过服务发现，由路由进行映射，可支持负载均衡，而是用url则完成特定服务的映射

The service ID (if any) to map to this route. You can specify a physical URL or  a service, but not both.
A full physical URL to map to the route. An alternative is to use a service ID and service discovery to find the physical address.';


/*==============================================================*/
/* Table: SM_SVC_AUTH                                           */
/*==============================================================*/
create table SM_SVC_AUTH (
   APP_ID               VARCHAR(40)          not null,
   SVC_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         null,
   SVC_NAME             VARCHAR(255)         null,
   APPKEY               VARCHAR(64)          null comment 'UUID,去除-',
   SECRET               VARCHAR(64)          null comment '加密存储',
   EXPIRED_TIME         DATE                 null,
   AUTH_TIME            DATE                 null,
   constraint PK_SM_SVC_AUTH primary key (APP_ID, SVC_ID)
)CHARSET=utf8;

