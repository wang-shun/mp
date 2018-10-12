/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/2/17 9:07:03                            */
/*==============================================================*/


alter table MT_MEETING add NOTICE_TYPE CHAR(1);

comment on column MT_MEETING.NOTICE_TYPE is
'0 不提醒 1 提前N分钟提醒 2 提前N小时提醒';

alter table MT_MEETING add NOTICE_SET NUMBER(10) default 0 not null;

alter table MT_MEETING add NOTICE_TIME timestamp;