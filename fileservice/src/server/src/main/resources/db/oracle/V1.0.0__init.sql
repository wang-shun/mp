/*==============================================================*/
/* Table: FS_FILE                                               */
/*==============================================================*/
create table FS_FILE 
(
   ID                   VARCHAR2(40 char)    not null,
   FILE_NAME            VARCHAR2(255 char),
   CONTENT_TYPE         VARCHAR2(64 char),
   CONTENT_SIZE         NUMBER(10)           default 0 not null,
   FILE_PATH            VARCHAR2(64 char),
   UPLOAD_TIME          TIMESTAMP,
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





