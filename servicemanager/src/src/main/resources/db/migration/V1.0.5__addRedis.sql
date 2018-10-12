/*==============================================================*/
/* Table: SM_REDIS                                              */
/*==============================================================*/
create table SM_REDIS (
   ID                   VARCHAR(40)          not null,
   REMARKS              VARCHAR(64)          null,
   HOST                 VARCHAR(64)          not null,
   PORT                 NUMERIC(10)          not null default 0,
   DB_INDEX             NUMERIC(10)          not null default 0,
   USER_NAME            VARCHAR(64)          null,
   PASSWORD             VARCHAR(64)          null,
   CREATE_TIME          DATE                 null,
   CREATOR              VARCHAR(40)          not null,
   ENABLED              VARCHAR(5)           not null default '1'
      constraint CKC_ENABLED_SM_REDIS check (ENABLED in ('1','0')),
   constraint PK_SM_REDIS primary key (ID)
);

comment on column SM_REDIS.DB_INDEX is
'Redis数据库索引（默认为0）';

comment on column SM_REDIS.PASSWORD is
'加密后的密码';

comment on column SM_REDIS.ENABLED is
'启用状态';
