/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2017/3/21 9:54:33                            */
/*==============================================================*/


alter table MT_MEETING add QRCODE VARCHAR2(64);

alter table MT_MEETING add NOTICE_TYPE CHAR(1);

comment on column MT_MEETING.NOTICE_TYPE is
'0 不提醒 1 提前N分钟提醒 2 提前N小时提醒';

alter table MT_MEETING add NOTICE_SET NUMBER(10) default 0 not null;

alter table MT_MEETING add NOTICE_TIME timestamp;

alter table MT_MEETING add SIGN_TYPE VARCHAR2(64);

comment on column MT_MEETING.SIGN_TYPE is
'open：开放式     close：封闭式';

alter table MT_SIGNIN_SEQU add QRCODE VARCHAR2(64);

alter table MD_PARTICIPANTS add PARENT_ID VARCHAR2(64);

