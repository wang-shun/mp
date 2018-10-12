/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2017/3/9 17:53:54                            */
/*==============================================================*/


/*==============================================================*/
/* Table: FS_FILE                                               */
/*==============================================================*/
create table FS_FILE (
   ID                   VARCHAR(40)          not null,
   FILE_NAME            VARCHAR(255)         null,
   CONTENT_TYPE         VARCHAR(64)          null,
   SIZE                 DECIMAL(10)          not null default 0,
   FILE_PATH            VARCHAR(255)         null,
   UPLOAD_TIME          TIMESTAMP            null,
   constraint PK_FS_FILE primary key (ID)
);

comment on column FS_FILE.ID is
'附件标识:';

comment on column FS_FILE.FILE_NAME is
'附件文件名:';

comment on column FS_FILE.CONTENT_TYPE is
'文件类型:multitype';

comment on column FS_FILE.FILE_PATH is
'文件存储路径(含文件名)的相对路径';




