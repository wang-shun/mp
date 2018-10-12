/*==============================================================*/
/* Table: MC_ALERT_LOGS                                         */
/*==============================================================*/
create table MC_ALERT_LOGS (
   ID                   VARCHAR(40)          not null,
   RULE_ID              VARCHAR(40)          not null,
   LEVEL                VARCHAR(64)          null,
   MESSAGE              VARCHAR(1000)        null,
   ALERT_DATA           VARCHAR(4000)        null,
   ALERT_TIME           DATE                 null,
   constraint PK_MC_ALERT_LOGS primary key (ID)
);

comment on column MC_ALERT_LOGS.LEVEL is
'info:提示 warn:警告 critical:紧急 resetWarn:警告消除 resetCritical:紧急情况消除';

comment on column MC_ALERT_LOGS.ALERT_DATA is
'json';
