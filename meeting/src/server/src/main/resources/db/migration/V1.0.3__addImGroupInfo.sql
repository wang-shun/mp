/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/9/20 11:44:03                           */
/*==============================================================*/


alter table MT_MEETING
   drop constraint PK_MT_MEETING;

drop table if exists tmp_MT_MEETING;

alter table MT_MEETING
   rename to tmp_MT_MEETING;

/*==============================================================*/
/* Table: MT_MEETING                                            */
/*==============================================================*/
create table MT_MEETING (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   ADDRESS              VARCHAR(255)         null,
   BEGIN_TIME           TIMESTAMP            not null,
   END_TIME             TIMESTAMP            not null,
   HAS_GROUP            VARCHAR(5)           not null default '0'
      constraint CKC_HAS_GROUP_MT_MEETI check (HAS_GROUP in ('1','0')),
   GROUP_ID             VARCHAR(64)          null,
   SPONSOR              VARCHAR(40)          not null,
   CREATE_TIME          TIMESTAMP            not null,
   STATUS               VARCHAR(64)          not null default '10',
   RELEASE_TIME         TIMESTAMP            null,
   constraint PK_MT_MEETING primary key (ID)
);

comment on column MT_MEETING.GROUP_ID is
'IM群组的ID';

comment on column MT_MEETING.STATUS is
'草稿：10 未进行：20 进行中：30 已取消：40 已结束：50';

insert into MT_MEETING (ID, ECID, NAME, ADDRESS, BEGIN_TIME, END_TIME, SPONSOR, CREATE_TIME, STATUS, RELEASE_TIME)
select ID, ECID, NAME, ADDRESS, BEGIN_TIME, END_TIME, SPONSOR, CREATE_TIME, STATUS, RELEASE_TIME
from tmp_MT_MEETING;

drop table if exists tmp_MT_MEETING;

