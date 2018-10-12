drop table MC_TAG;

/*==============================================================*/
/* Table: MC_TAG                                                */
/*==============================================================*/
create table MC_TAG (
   ID                   VARCHAR(40)          not null,
   TAG                  VARCHAR(64)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         null,
   constraint PK_MC_TAG primary key (ID)
)CHARSET=utf8;
