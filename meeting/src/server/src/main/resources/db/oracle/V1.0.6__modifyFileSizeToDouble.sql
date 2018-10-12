/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2016/10/27 16:20:41                          */
/*==============================================================*/


comment on column MT_ATTACHMENT.FILE_SIZE is
'KB';

alter table MT_ATTACHMENT modify FILE_SIZE DECIMAL(20,6);

