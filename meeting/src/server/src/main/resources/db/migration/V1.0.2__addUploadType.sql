/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2016/9/12 17:08:00                           */
/*==============================================================*/


alter table MT_ATTACHMENT
   drop constraint PK_MT_ATTACHMENT;

drop table if exists tmp_MT_ATTACHMENT;

alter table MT_ATTACHMENT
   rename to tmp_MT_ATTACHMENT;


/*==============================================================*/
/* Table: MT_ATTACHMENT                                         */
/*==============================================================*/
create table MT_ATTACHMENT (
   ID                   VARCHAR(40)          not null,
   MEETING_ID           VARCHAR(40)          not null,
   FILE_PATH            VARCHAR(255)         not null,
   FILE_NAME            VARCHAR(255)         not null,
   CONTENT_TYPE         VARCHAR(64)          null,
   SIZE                 DECIMAL(10)          not null default 0,
   UPLOAD_TIME          TIMESTAMP            null,
   PRIVILEGE            DECIMAL(10)          not null default 0,
   constraint PK_MT_ATTACHMENT primary key (ID)
);

comment on column MT_ATTACHMENT.PRIVILEGE is
'1：查看 3：下载（含查看）';

insert into MT_ATTACHMENT (ID, MEETING_ID, FILE_PATH, FILE_NAME, CONTENT_TYPE, SIZE, PRIVILEGE)
select ID, MEETING_ID, FILE_PATH, FILE_NAME, CONTENT_TYPE, SIZE, PRIVILEGE
from tmp_MT_ATTACHMENT;

drop table if exists tmp_MT_ATTACHMENT;


