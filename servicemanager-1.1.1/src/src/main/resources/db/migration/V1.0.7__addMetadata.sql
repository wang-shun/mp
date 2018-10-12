/*==============================================================*/
/* Table: SM_APP_METADATA                                       */
/*==============================================================*/
create table SM_APP_METADATA (
   APP_ID               VARCHAR(40)          not null,
   METADATA             VARCHAR(4000)        null,
   constraint PK_SM_APP_METADATA primary key (APP_ID)
);

comment on column SM_APP_METADATA.METADATA is
'json';
