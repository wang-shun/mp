/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017-08-08 19:45:46                          */
/*==============================================================*/


/*==============================================================*/
/* Table: MC_ALERT_CONDITION                                    */
/*==============================================================*/
create table MC_ALERT_CONDITION 
(
   ID                   VARCHAR2(40 char)    not null,
   RULE_ID              VARCHAR2(40 char)    not null,
   RULE_TYPE            VARCHAR2(64 char),
   "LEVEL"              VARCHAR2(64 char),
   SETTING_1            VARCHAR2(64 char),
   SETTING_2            VARCHAR2(64 char),
   SETTING_3            VARCHAR2(64 char),
   SETTING_4            VARCHAR2(64 char),
   SETTING_5            VARCHAR2(64 char),
   constraint PK_MC_ALERT_CONDITION primary key (ID)
);

comment on column MC_ALERT_CONDITION.RULE_TYPE is
'threshold:阈值 relative:相对变化 deadman:无数据  ';

comment on column MC_ALERT_CONDITION."LEVEL" is
'info:提示 warn:警告 critical:紧急 resetWarn:警告消除 resetCritical:紧急情况消除';

/*==============================================================*/
/* Table: MC_ALERT_METHOD                                       */
/*==============================================================*/
create table MC_ALERT_METHOD 
(
   ID                   VARCHAR2(40 char)    not null,
   RULE_ID              VARCHAR2(40 char)    not null,
   ALERT_METHOD         VARCHAR2(64 char),
   CONFIG               VARCHAR2(1000 char),
   constraint PK_MC_ALERT_METHOD primary key (ID)
);

comment on column MC_ALERT_METHOD.ALERT_METHOD is
'email:邮件,sms:短信';

/*==============================================================*/
/* Table: MC_ALERT_RULE                                         */
/*==============================================================*/
create table MC_ALERT_RULE 
(
   ID                   VARCHAR2(40 char)    not null,
   SYSTEM_ID            VARCHAR2(40 char)    not null,
   NAME                 VARCHAR2(255 char),
   PAST_TIME            VARCHAR2(64 char),
   MEASUREMENT          VARCHAR2(1000 char),
   VALUE_FIELD          VARCHAR2(64 char),
   FUNC                 VARCHAR2(64 char),
   GROUP_BY             VARCHAR2(512 char),
   WH_ERE               VARCHAR2(1000 char),
   QUERY_QL             VARCHAR2(4000 char),
   MESSAGE              VARCHAR2(4000 char),
   ENABLED              VARCHAR2(5 char)     default '1' not null
      constraint CKC_ENABLED_MC_ALERT check (ENABLED in ('1','0')),
   constraint PK_MC_ALERT_RULE primary key (ID)
);

comment on column MC_ALERT_RULE.FUNC is
'mean:平均值
min:最小值
max:最大值
count:计数
sum:汇总
stddev:标准方差';

comment on column MC_ALERT_RULE.GROUP_BY is
'逗号分隔的tag';

comment on column MC_ALERT_RULE.WH_ERE is
'json,

{
  appId:[''mapps-fileservice'', ''mapps-gateway''],
  host: [''localhost'']
}
';

/*==============================================================*/
/* Table: MC_DOWNSAMPLE                                         */
/*==============================================================*/
create table MC_DOWNSAMPLE 
(
   ID                   VARCHAR2(40 char)    not null,
   SYSTEM_ID            VARCHAR2(40 char)    not null,
   MEASUREMENT          VARCHAR2(64 char),
   REMARKS              VARCHAR2(512 char),
   SAMPLE_SQL           VARCHAR2(4000 char),
   SYNCED               VARCHAR2(5 char)     default '0' not null
      constraint CKC_SYNCED_MC_DOWNS check (SYNCED in ('1','0')),
   ENABLED              VARCHAR2(5 char)     default '1' not null
      constraint CKC_ENABLED_MC_DOWNS check (ENABLED in ('1','0')),
   constraint PK_MC_DOWNSAMPLE primary key (ID)
);

/*==============================================================*/
/* Table: MC_MEASUREMENT                                        */
/*==============================================================*/
create table MC_MEASUREMENT 
(
   ID                   VARCHAR2(40 char)    not null,
   SYSTEM_ID            VARCHAR2(40 char)    not null,
   MEASUREMENT          VARCHAR2(1000 char),
   NAME                 VARCHAR2(512 char),
   RETAIN_POLICY        VARCHAR2(64 char),
   RETAIN_TIME          VARCHAR2(64 char),
   constraint PK_MC_MEASUREMENT primary key (ID)
);

comment on column MC_MEASUREMENT.RETAIN_TIME is
'比如2h 3d 4w';

/*==============================================================*/
/* Table: MC_RETENTION_POLICY                                   */
/*==============================================================*/
create table MC_RETENTION_POLICY 
(
   ID                   VARCHAR2(40 char)    not null,
   SYSTEM_ID            VARCHAR2(40 char)    not null,
   RP                   VARCHAR2(64 char),
   RP_NAME              VARCHAR2(512 char),
   RETAIN_TIME          VARCHAR2(64 char),
   IS_DEFAULT           VARCHAR2(5 char)     default '0' not null
      constraint CKC_IS_DEFAULT_MC_RETEN check (IS_DEFAULT in ('1','0')),
   ENABLED              VARCHAR2(5 char)     default '1' not null
      constraint CKC_ENABLED_MC_RETEN check (ENABLED in ('1','0')),
   constraint PK_MC_RETENTION_POLICY primary key (ID)
);

comment on column MC_RETENTION_POLICY.RETAIN_TIME is
'比如2h 3d 4w';

/*==============================================================*/
/* Table: MC_SYSTEM                                             */
/*==============================================================*/
create table MC_SYSTEM 
(
   ID                   VARCHAR2(40 char)    not null,
   DB                   VARCHAR2(64 char),
   ADMIN_USER           VARCHAR2(64 char),
   ADMIN_PASSWD         VARCHAR2(64 char),
   ENABLED              VARCHAR2(5 char)     default '1' not null
      constraint CKC_ENABLED_MC_SYSTE check (ENABLED in ('1','0')),
   constraint PK_MC_SYSTEM primary key (ID)
);

/*==============================================================*/
/* Table: MC_TAG                                                */
/*==============================================================*/
create table MC_TAG 
(
   ID                   VARCHAR2(40 char)    not null,
   TAG                  VARCHAR2(64 char)    not null,
   SYSTEM_ID            VARCHAR2(40 char)    not null,
   NAME                 VARCHAR2(255 char),
   constraint PK_MC_TAG primary key (ID)
);

/*==============================================================*/
/* Table: MC_UNIT_WEIGHT                                        */
/*==============================================================*/
create table MC_UNIT_WEIGHT 
(
   UNIT                 VARCHAR2(64 char)    not null,
   WEIGHT               NUMBER(10)           default 0 not null,
   constraint PK_MC_UNIT_WEIGHT primary key (UNIT)
);

/*==============================================================*/
/* Table: MC_VALUE_FIELD                                        */
/*==============================================================*/
create table MC_VALUE_FIELD 
(
   ID                   VARCHAR2(40 char)    not null,
   MEASUREMENT_ID       VARCHAR2(40 char)    not null,
   FIELD                VARCHAR2(64 char)    not null,
   NAME                 VARCHAR2(255 char),
   UNIT                 VARCHAR2(64 char),
   constraint PK_MC_VALUE_FIELD primary key (ID)
);

/*==============================================================*/
/* Table: SM_APP_CONFIG                                         */
/*==============================================================*/
create table SM_APP_CONFIG 
(
   ID                   VARCHAR2(40 char)    not null,
   APP_ID               VARCHAR2(40 char)    not null,
   PARAM_KEY            VARCHAR2(64 char),
   PARAM_VALUE          VARCHAR2(1000 char),
   CONFIG_VER           NUMBER(10)           default 0 not null,
   ACTIVED              VARCHAR2(5 char)     default '0' not null
      constraint CKC_ACTIVED_SM_APP_C check (ACTIVED in ('1','0')),
   SETUP_USER           VARCHAR2(40 char)    not null,
   SETUP_TIME           DATE,
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
/* Table: SM_APP_METADATA                                       */
/*==============================================================*/
create table SM_APP_METADATA 
(
   APP_ID               VARCHAR2(40 char)    not null,
   METADATA             VARCHAR2(4000 char),
   constraint PK_SM_APP_METADATA primary key (APP_ID)
);

comment on column SM_APP_METADATA.METADATA is
'json';

/*==============================================================*/
/* Table: SM_DATABASE                                           */
/*==============================================================*/
create table SM_DATABASE 
(
   ID                   VARCHAR2(40 char)    not null,
   REMARKS              VARCHAR2(64 char),
   DB_TYPE              VARCHAR2(64 char),
   HOST                 VARCHAR2(64 char)    not null,
   PORT                 NUMBER(10)           default 0 not null,
   SID                  VARCHAR2(64 char),
   DB_NAME              VARCHAR2(64 char),
   USER_NAME            VARCHAR2(64 char),
   PASSWORD             VARCHAR2(64 char),
   CREATE_TIME          DATE,
   CREATOR              VARCHAR2(40 char)    not null,
   ENABLED              VARCHAR2(5 char)     default '1' not null
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
create table SM_DB_ASSIGN 
(
   DB_ID                VARCHAR2(40 char)    not null,
   ASSIGN_ID            VARCHAR2(40 char)    not null,
   SVC_ID               VARCHAR2(40 char)    not null,
   ASSIGN_TIME          DATE,
   constraint PK_SM_DB_ASSIGN primary key (DB_ID, SVC_ID)
);

comment on column SM_DB_ASSIGN.DB_ID is
'数据库ID';

comment on column SM_DB_ASSIGN.ASSIGN_ID is
'资源分配ID';

comment on column SM_DB_ASSIGN.SVC_ID is
'服务ID';

comment on column SM_DB_ASSIGN.ASSIGN_TIME is
'分配时间';

/*==============================================================*/
/* Table: SM_REDIS                                              */
/*==============================================================*/
create table SM_REDIS 
(
   ID                   VARCHAR2(40 char)    not null,
   REMARKS              VARCHAR2(64 char),
   HOST                 VARCHAR2(64 char)    not null,
   PORT                 NUMBER(10)           default 0 not null,
   DB_INDEX             NUMBER(10)           default 0 not null,
   USER_NAME            VARCHAR2(64 char),
   PASSWORD             VARCHAR2(64 char),
   CREATE_TIME          DATE,
   CREATOR              VARCHAR2(40 char)    not null,
   ENABLED              VARCHAR2(5 char)     default '1' not null
      constraint CKC_ENABLED_SM_REDIS check (ENABLED in ('1','0')),
   constraint PK_SM_REDIS primary key (ID)
);

comment on column SM_REDIS.DB_INDEX is
'Redis数据库索引（默认为0）';

comment on column SM_REDIS.PASSWORD is
'加密后的密码';

comment on column SM_REDIS.ENABLED is
'启用状态';

/*==============================================================*/
/* Table: SM_RESOURCE                                           */
/*==============================================================*/
create table SM_RESOURCE 
(
   ID                   VARCHAR2(40 char)    not null,
   RES_ID               VARCHAR2(40 char)    not null,
   NAME                 VARCHAR2(255 char)   not null,
   REMARKS              VARCHAR2(512 char),
   CREATOR              VARCHAR2(40 char)    not null,
   CREATE_TIME          DATE,
   MODIFIER             VARCHAR2(40 char)    not null,
   MODIFY_TIME          DATE,
   ENABLED              VARCHAR2(5 char)     default '1' not null
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
/* Table: SM_RESOURCE_ASSIGN                                    */
/*==============================================================*/
create table SM_RESOURCE_ASSIGN 
(
   ID                   VARCHAR2(40 char)    not null,
   APP_ID               VARCHAR2(40 char)    not null,
   APP_NAME             VARCHAR2(255 char),
   RES_ID               VARCHAR2(40 char)    not null,
   RES_CODE             VARCHAR2(64 char)    not null,
   RES_NAME             VARCHAR2(255 char),
   REOURCE_ID           VARCHAR2(40 char),
   ASSIGN_TIME          DATE,
   constraint PK_SM_RESOURCE_ASSIGN primary key (ID)
);

comment on column SM_RESOURCE_ASSIGN.RES_ID is
'依赖的资源标识';

comment on column SM_RESOURCE_ASSIGN.RES_CODE is
'识别资源的代码，比如某个应用可能依赖同一资源类型的多个实例，默认为default';

/*==============================================================*/
/* Table: SM_RESOURCE_CONFIG                                    */
/*==============================================================*/
create table SM_RESOURCE_CONFIG 
(
   ID                   VARCHAR2(40 char)    not null,
   RESOURCE_ID          VARCHAR2(40 char)    not null,
   PARAM_KEY            VARCHAR2(64 char),
   PARAM_VALUE          VARCHAR2(1000 char),
   CONFIG_VER           NUMBER(10)           default 0 not null,
   ACTIVED              VARCHAR2(5 char)     default '0' not null
      constraint CKC_ACTIVED_SM_RESOU check (ACTIVED in ('1','0')),
   SETUP_USER           VARCHAR2(40 char)    not null,
   SETUP_TIME           DATE,
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

/*==============================================================*/
/* Table: SM_RESOURCE_INFO                                      */
/*==============================================================*/
create table SM_RESOURCE_INFO 
(
   ID                   VARCHAR2(40 char)    not null,
   NAME                 VARCHAR2(255 char)   not null,
   REMARKS              VARCHAR2(512 char),
   TYPE                 CHAR(1)              not null,
   constraint PK_SM_RESOURCE_INFO primary key (ID)
);

comment on column SM_RESOURCE_INFO.TYPE is
'1-数据库,2-redis,3-第三方接入';

/*==============================================================*/
/* Table: SM_RESOURCE_INFOITEM                                  */
/*==============================================================*/
create table SM_RESOURCE_INFOITEM 
(
   ID                   VARCHAR2(40 char)    not null,
   INFO_ID              VARCHAR2(40 char)    not null,
   KEY                  VARCHAR2(64 char)    not null,
   NAME                 VARCHAR2(255 char)   not null,
   REMARKS              VARCHAR2(512 char),
   TYPE                 VARCHAR2(64 char)    not null,
   REGEX                VARCHAR2(512 char),
   OPTIONS              VARCHAR2(1000 char),
   GROUP_NAME           VARCHAR2(255 char),
   CONTROL_SIZE         NUMBER(10)           default 0 not null,
   DEFAULT_VALUE        VARCHAR2(1000 char),
   SEQU                 NUMBER(10)           default 0 not null,
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
/* Table: SM_ROUTE                                              */
/*==============================================================*/
create table SM_ROUTE 
(
   ID                   VARCHAR2(40 char)    not null,
   SERVICE_ID           VARCHAR2(255 char),
   URL                  VARCHAR2(512 char),
   SERVICE_NAME         VARCHAR2(255 char)   not null,
   PATH                 VARCHAR2(255 char)   not null,
   STRIP_PREFIX         VARCHAR2(5 char)     default '1' not null
      constraint CKC_STRIP_PREFIX_SM_ROUTE check (STRIP_PREFIX in ('1','0')),
   RETRYABLE            VARCHAR2(5 char)     default '1' not null
      constraint CKC_RETRYABLE_SM_ROUTE check (RETRYABLE in ('1','0')),
   SENSITIVE_HEADERS    VARCHAR2(1000 char),
   CUSTOM_POLICY        VARCHAR2(64 char),
   ENABLED              VARCHAR2(5 char)     default '1' not null
      constraint CKC_ENABLED_SM_ROUTE check (ENABLED in ('1','0')),
   NEED_AUTH            VARCHAR2(5 char)     default '1' not null
      constraint CKC_NEED_AUTH_SM_ROUTE check (NEED_AUTH in ('1','0')),
   AUTH_RESOURCE        VARCHAR2(1000 char),
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

comment on column SM_ROUTE.AUTH_RESOURCE is
'逗号分隔的，资源路径, 默认为/api/**';

/*==============================================================*/
/* Table: SM_SVC_AUTH                                           */
/*==============================================================*/
create table SM_SVC_AUTH 
(
   APP_ID               VARCHAR2(40 char)    not null,
   SVC_ID               VARCHAR2(40 char)    not null,
   APP_NAME             VARCHAR2(255 char),
   SVC_NAME             VARCHAR2(255 char),
   ROUTE_ID             VARCHAR2(40 char)    not null,
   APPKEY               VARCHAR2(64 char),
   SECRET               VARCHAR2(64 char),
   EXPIRED_TIME         DATE,
   AUTH_TIME            DATE,
   constraint PK_SM_SVC_AUTH primary key (APP_ID, SVC_ID)
);

comment on column SM_SVC_AUTH.APPKEY is
'UUID,去除-';

comment on column SM_SVC_AUTH.SECRET is
'加密存储';

