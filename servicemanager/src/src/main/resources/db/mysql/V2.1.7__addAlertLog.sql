/*==============================================================*/
/* Table: MC_ALERT_LOGS                                         */
/*==============================================================*/
create table MC_ALERT_LOGS (
   ID                   VARCHAR(40)          not null,
   RULE_ID              VARCHAR(40)          not null,
   ALERT_LEVEL          VARCHAR(64)          null comment 'info:提示 warn:警告 critical:紧急 resetWarn:警告消除 resetCritical:紧急情况消除',
   MESSAGE              VARCHAR(1000)        null,
   ALERT_DATA           VARCHAR(4000)        null comment 'json',
   ALERT_TIME           timestamp            null,
   constraint PK_MC_ALERT_LOGS primary key (ID)
)CHARSET=utf8;

