/*==============================================================*/
/* Table: SM_APP_METADATA                                       */
/*==============================================================*/
create table SM_APP_METADATA (
   APP_ID               VARCHAR(40)             not null,
   METADATA             text                    comment 'json',
   constraint PK_SM_APP_METADATA primary key (APP_ID)
)CHARSET=utf8;

