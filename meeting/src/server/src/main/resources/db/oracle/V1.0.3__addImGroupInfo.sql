/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2016/10/17 11:57:19                          */
/*==============================================================*/


alter table MT_MEETING 
    add HAS_GROUP            VARCHAR2(5)          default '0' not null
      constraint CKC_HAS_GROUP_MT_MEETI check (HAS_GROUP in ('1','0'));
      
alter table MT_MEETING 
    add GROUP_ID             VARCHAR2(64);
      

