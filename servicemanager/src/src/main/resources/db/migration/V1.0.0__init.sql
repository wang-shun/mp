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
   PARAM_KEY            VARCHAR(64)          null,
   PARAM_VALUE          VARCHAR(1000)        null,
   CONFIG_VER           NUMERIC(10)          not null default 0,
   ACTIVED              VARCHAR(5)           not null default '0'
      constraint CKC_ACTIVED_SM_APP_C check (ACTIVED in ('1','0')),
   SETUP_USER           VARCHAR(40)          not null,
   SETUP_TIME           DATE                 null,
   constraint PK_SM_APP_CONFIG primary key (ID)
);

comment on column SM_APP_CONFIG.PARAM_KEY is
'参数key';

comment on column SM_APP_CONFIG.PARAM_VALUE is
'参数值';

comment on column SM_APP_CONFIG.CONFIG_VER is
'配置版本好，数字，累加';

comment on column SM_APP_CONFIG.ACTIVED is
'活动为当前有效的配置';

/*==============================================================*/
/* Table: SM_DATABASE                                           */
/*==============================================================*/
create table SM_DATABASE (
   ID                   VARCHAR(40)          not null,
   DB_TYPE              VARCHAR(64)          null,
   HOST                 VARCHAR(64)          not null,
   PORT                 NUMERIC(10)          not null default 0,
   SID                  VARCHAR(64)          null,
   DB_NAME              VARCHAR(64)          null,
   USER_NAME            VARCHAR(64)          null,
   PASSWORD             VARCHAR(64)          null,
   CREATE_TIME          DATE                 null,
   CREATOR              VARCHAR(40)          not null,
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_SM_DATAB check (ENABLED in ('1','0')),
   constraint PK_SM_DATABASE primary key (ID)
);

comment on column SM_DATABASE.DB_TYPE is
'postgresql\oracle';

comment on column SM_DATABASE.PASSWORD is
'加密后的密码';

comment on column SM_DATABASE.ENABLED is
'启用状态';

/*==============================================================*/
/* Table: SM_DB_ASSIGN                                          */
/*==============================================================*/
create table SM_DB_ASSIGN (
   DB_ID                VARCHAR(40)          not null,
   SVC_ID               VARCHAR(40)          not null,
   SVC_NAME             VARCHAR(255)         null,
   ASSIGN_TIME          DATE                 null,
   constraint PK_SM_DB_ASSIGN primary key (DB_ID, SVC_ID)
);

comment on column SM_DB_ASSIGN.DB_ID is
'数据库ID';

comment on column SM_DB_ASSIGN.SVC_ID is
'服务ID';

comment on column SM_DB_ASSIGN.SVC_NAME is
'服务名称';

comment on column SM_DB_ASSIGN.ASSIGN_TIME is
'分配时间';

/*==============================================================*/
/* Table: SM_RESOURCE_ASSIGN                                    */
/*==============================================================*/
create table SM_RESOURCE_ASSIGN (
   APP_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         null,
   RES_ID               VARCHAR(40)          not null,
   RES_CODE             VARCHAR(64)          not null,
   RES_CONFIG           VARCHAR(4000)        null,
   RES_NAME             VARCHAR(255)         null,
   ASSIGN_TIME          DATE                 null,
   constraint PK_SM_RESOURCE_ASSIGN primary key (APP_ID, RES_ID, RES_CODE)
);

comment on column SM_RESOURCE_ASSIGN.RES_ID is
'依赖的资源应用ID';

comment on column SM_RESOURCE_ASSIGN.RES_CODE is
'识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default';

comment on column SM_RESOURCE_ASSIGN.RES_CONFIG is
'json格式';

/*==============================================================*/
/* Table: SM_ROUTE                                              */
/*==============================================================*/
create table SM_ROUTE (
   ID                   VARCHAR(40)          not null,
   SERVICE_ID           VARCHAR(255)         null,
   URL                  VARCHAR(512)         null,
   SERVICE_NAME         VARCHAR(255)         not null,
   PATH                 VARCHAR(255)         not null,
   STRIP_PREFIX         VARCHAR(5)           not null default '1'
      constraint CKC_STRIP_PREFIX_SM_ROUTE check (STRIP_PREFIX in ('1','0')),
   RETRYABLE            VARCHAR(5)           not null default '1'
      constraint CKC_RETRYABLE_SM_ROUTE check (RETRYABLE in ('1','0')),
   SENSITIVE_HEADERS    VARCHAR(1000)        null,
   CUSTOM_POLICY        VARCHAR(64)          null,
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_SM_ROUTE check (ENABLED in ('1','0')),
   NEED_AUTH            VARCHAR(5)           not null default '1'
      constraint CKC_NEED_AUTH_SM_ROUTE check (NEED_AUTH in ('1','0')),
   constraint PK_SM_ROUTE primary key (ID)
);

comment on table SM_ROUTE is
'ServiceId和服务URL只能选择一个，使用服务id则通过服务发现，由路由进行映射，可支持负载均衡，而是用url则完成特定服务的映射

The service ID (if any) to map to this route. You can specify a physical URL or  a service, but not both.
A full physical URL to map to the route. An alternative is to use a service ID and service discovery to find the physical address.';

comment on column SM_ROUTE.SERVICE_ID is
'服务对应的ServiceId';

comment on column SM_ROUTE.URL is
'服务对应的访问路径';

comment on column SM_ROUTE.SENSITIVE_HEADERS is
'逗号分隔';

comment on column SM_ROUTE.ENABLED is
'启用状态';

/*==============================================================*/
/* Table: SM_SVC_AUTH                                           */
/*==============================================================*/
create table SM_SVC_AUTH (
   APP_ID               VARCHAR(40)          not null,
   SVC_ID               VARCHAR(40)          not null,
   APP_NAME             VARCHAR(255)         null,
   SVC_NAME             VARCHAR(255)         null,
   APPKEY               VARCHAR(64)          null,
   SECRET               VARCHAR(64)          null,
   EXPIRED_TIME         DATE                 null,
   AUTH_TIME            DATE                 null,
   constraint PK_SM_SVC_AUTH primary key (APP_ID, SVC_ID)
);

comment on column SM_SVC_AUTH.APPKEY is
'UUID,去除-';

comment on column SM_SVC_AUTH.SECRET is
'加密存储';

