/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     2017/3/9 17:53:54                            */
/*==============================================================*/


/*==============================================================*/
/* Table: FS_FILE                                               */
/*==============================================================*/
create table FS_FILE (
   ID                   VARCHAR(40)          not null comment '附件标识',
   FILE_NAME            VARCHAR(255)         null comment '附件文件名',
   CONTENT_TYPE         VARCHAR(64)          null comment '文件类型:multitype',
   CONTENT_SIZE         DECIMAL(10)          not null default 0,
   FILE_PATH            VARCHAR(255)         null comment '文件存储路径(含文件名)的相对路径',
   UPLOAD_TIME          TIMESTAMP            null,
   MD5                  VARCHAR(40)          null,
   constraint PK_FS_FILE primary key (ID)
)CHARSET=utf8;




