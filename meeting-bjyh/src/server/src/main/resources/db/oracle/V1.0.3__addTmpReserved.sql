/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/2/17 16:06:49                           */
/*==============================================================*/

create table TMP_RESERVED 
(
   ID                   VARCHAR(40)          not null,
   ROOM_ID              VARCHAR(40)          not null,
   TIME_BEGIN           TIMESTAMP            not null,
   TIME_END             TIMESTAMP            not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_TMP_RESERVED primary key (ID)
);
