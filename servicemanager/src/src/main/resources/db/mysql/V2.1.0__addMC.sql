/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017-07-12 9:30:37                           */
/*==============================================================*/


/*==============================================================*/
/* Table: MC_ALERT_CONDITION                                    */
/*==============================================================*/
create table MC_ALERT_CONDITION (
   ID                   VARCHAR(40)          not null,
   RULE_ID              VARCHAR(40)          not null,
   RULE_TYPE            VARCHAR(64)          null comment 'threshold:阈值 relative:相对变化 deadman:无数据  ',
   ALERT_LEVEL          VARCHAR(64)          null comment 'info:提示 warn:警告 critical:紧急 resetWarn:警告消除 resetCritical:紧急情况消除',
   SETTING_1            VARCHAR(64)          null,
   SETTING_2            VARCHAR(64)          null,
   SETTING_3            VARCHAR(64)          null,
   SETTING_4            VARCHAR(64)          null,
   SETTING_5            VARCHAR(64)          null,
   constraint PK_MC_ALERT_CONDITION primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_ALERT_METHOD                                       */
/*==============================================================*/
create table MC_ALERT_METHOD (
   ID                   VARCHAR(40)          not null,
   RULE_ID              VARCHAR(40)          not null,
   ALERT_METHOD         VARCHAR(64)          null comment 'email:邮件,sms:短信',
   CONFIG               VARCHAR(1000)        null,
   constraint PK_MC_ALERT_METHOD primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_ALERT_RULE                                         */
/*==============================================================*/
create table MC_ALERT_RULE (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         null,
   PAST_TIME            VARCHAR(64)          null,
   MEASUREMENT          VARCHAR(1000)        null,
   VALUE_FIELD          VARCHAR(64)          null,
   MESSAGE              VARCHAR(4000)        null,
   ENABLED              enum('1','0')        not null default '1',
   constraint PK_MC_ALERT_RULE primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_MEASUREMENT                                        */
/*==============================================================*/
create table MC_MEASUREMENT (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   MEASUREMENT          VARCHAR(1000)        null,
   NAME                 VARCHAR(512)         null,
   RETAIN_TIME          VARCHAR(64)          null comment '比如2h 3d 4w',
   constraint PK_MC_MEASUREMENT primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_SYSTEM                                             */
/*==============================================================*/
create table MC_SYSTEM (
   ID                   VARCHAR(40)          not null,
   DB                   VARCHAR(64)          null,
   DB_USER           VARCHAR(64)          null,
   DB_PASSWD         VARCHAR(64)          null,
   ENABLED              enum('1','0')        not null default '1',
   constraint PK_MC_SYSTEM primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_TAG                                                */
/*==============================================================*/
create table MC_TAG (
   TAG                  VARCHAR(64)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         null,
   constraint PK_MC_TAG primary key (TAG)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_UNIT_WEIGHT                                        */
/*==============================================================*/
create table MC_UNIT_WEIGHT (
   UNIT                 VARCHAR(64)          not null,
   WEIGHT               NUMERIC(10)          not null default 0,
   constraint PK_MC_UNIT_WEIGHT primary key (UNIT)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_VALUE_FIELD                                        */
/*==============================================================*/
create table MC_VALUE_FIELD (
   ID                   VARCHAR(40)          not null,
   MEASUREMENT_ID       VARCHAR(40)          not null,
   FIELD                VARCHAR(64)          not null,
   NAME                 VARCHAR(255)         null,
   UNIT                 VARCHAR(64)          null,
   constraint PK_MC_VALUE_FIELD primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: 采样规则                                                  */
/*==============================================================*/
create table MC_DOWNSAMPLE (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   CQ_NAME          VARCHAR(64)          null,
   REMARKS              VARCHAR(512)         null,
   SAMPLE_SQL           VARCHAR(4000)        null,
   SYNCED               enum('1','0')        not null default '0',
   ENABLED              enum('1','0')        not null default '1',
   constraint PK_MC_DOWNSAMPLE primary key (ID)
)CHARSET=utf8;

