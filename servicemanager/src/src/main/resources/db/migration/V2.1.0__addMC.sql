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
   RULE_TYPE            VARCHAR(64)          null,
   LEVEL                VARCHAR(64)          null,
   SETTING_1            VARCHAR(64)          null,
   SETTING_2            VARCHAR(64)          null,
   SETTING_3            VARCHAR(64)          null,
   SETTING_4            VARCHAR(64)          null,
   SETTING_5            VARCHAR(64)          null,
   constraint PK_MC_ALERT_CONDITION primary key (ID)
);

comment on column MC_ALERT_CONDITION.RULE_TYPE is
'threshold:阈值 relative:相对变化 deadman:无数据  ';

comment on column MC_ALERT_CONDITION.LEVEL is
'info:提示 warn:警告 critical:紧急 resetWarn:警告消除 resetCritical:紧急情况消除';

/*==============================================================*/
/* Table: MC_ALERT_METHOD                                       */
/*==============================================================*/
create table MC_ALERT_METHOD (
   ID                   VARCHAR(40)          not null,
   RULE_ID              VARCHAR(40)          not null,
   ALERT_METHOD         VARCHAR(64)          null,
   CONFIG               VARCHAR(1000)        null,
   constraint PK_MC_ALERT_METHOD primary key (ID)
);

comment on column MC_ALERT_METHOD.ALERT_METHOD is
'email:邮件,sms:短信';

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
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_MC_ALERT check (ENABLED in ('1','0')),
   constraint PK_MC_ALERT_RULE primary key (ID)
);

/*==============================================================*/
/* Table: MC_MEASUREMENT                                        */
/*==============================================================*/
create table MC_MEASUREMENT (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   MEASUREMENT          VARCHAR(1000)        null,
   NAME                 VARCHAR(512)         null,
   RETAIN_TIME          VARCHAR(64)          null,
   constraint PK_MC_MEASUREMENT primary key (ID)
);

comment on column MC_MEASUREMENT.RETAIN_TIME is
'比如2h 3d 4w';

/*==============================================================*/
/* Table: MC_SYSTEM                                             */
/*==============================================================*/
create table MC_SYSTEM (
   ID                   VARCHAR(40)          not null,
   DB                   VARCHAR(64)          null,
   ADMIN_USER           VARCHAR(64)          null,
   ADMIN_PASSWD         VARCHAR(64)          null,
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_MC_SYSTE check (ENABLED in ('1','0')),
   constraint PK_MC_SYSTEM primary key (ID)
);

/*==============================================================*/
/* Table: MC_TAG                                                */
/*==============================================================*/
create table MC_TAG (
   TAG                  VARCHAR(64)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         null,
   constraint PK_MC_TAG primary key (TAG)
);

/*==============================================================*/
/* Table: MC_UNIT_WEIGHT                                        */
/*==============================================================*/
create table MC_UNIT_WEIGHT (
   UNIT                 VARCHAR(64)          not null,
   WEIGHT               NUMERIC(10)          not null default 0,
   constraint PK_MC_UNIT_WEIGHT primary key (UNIT)
);

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
);

/*==============================================================*/
/* Table: 采样规则                                                  */
/*==============================================================*/
create table MC_DOWNSAMPLE (
   ID                   VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   MEASUREMENT          VARCHAR(64)          null,
   REMARKS              VARCHAR(512)         null,
   SAMPLE_SQL           VARCHAR(4000)        null,
   SYNCED               VARCHAR(5)           not null default '0'
      constraint CKC_SYNCED_MC_DOWNSAMPLE check (SYNCED in ('1','0')),
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_MC_DOWNSAMPLE check (ENABLED in ('1','0')),
   constraint PK_MC_DOWNSAMPLE primary key (ID)
);

