/*==============================================================*/
/* Table: MC_ALERT_LOGS                                         */
/*==============================================================*/
create table MC_ALERT_LOGS 
(
   ID                   VARCHAR2(40 char)    not null,
   RULE_ID              VARCHAR2(40 char)    not null,
   "LEVEL"              VARCHAR2(64 char),
   MESSAGE              VARCHAR2(1000 char),
   ALERT_DATA           VARCHAR2(4000 char),
   ALERT_TIME           DATE,
   constraint PK_MC_ALERT_LOGS primary key (ID)
);

comment on column MC_ALERT_LOGS."LEVEL" is
'info:提示 warn:警告 critical:紧急 resetWarn:警告消除 resetCritical:紧急情况消除';

comment on column MC_ALERT_LOGS.ALERT_DATA is
'json';
