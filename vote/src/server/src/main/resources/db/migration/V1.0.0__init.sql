/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2016/11/1 10:23:31                           */
/*==============================================================*/


/*==============================================================*/
/* Table: VT_VOTE_ANSWER                                        */
/*==============================================================*/
create table if not exists VT_VOTE_ANSWER (
   ID                   VARCHAR(40)          not null,
   ORG_ID               VARCHAR(40)          not null,
   VOTE_INFO_ID         VARCHAR(40)          not null,
   VOTE_ITEM_ID         VARCHAR(40)          not null,
   CREATOR              VARCHAR(40)          not null,
   CREATOR_NAME         VARCHAR(255)         null,
   CREATE_TIME          TIMESTAMP            null,
   constraint vt_vote_answer_pkey primary key (ID)
)
WITH (OIDS=FALSE);

comment on column VT_VOTE_ANSWER.ID is
'投选id';

comment on column VT_VOTE_ANSWER.ORG_ID is
'所属机构id';

comment on column VT_VOTE_ANSWER.VOTE_INFO_ID is
'所投投票id';

comment on column VT_VOTE_ANSWER.VOTE_ITEM_ID is
'所投投票项id';

comment on column VT_VOTE_ANSWER.CREATOR is
'参与投票人id';

comment on column VT_VOTE_ANSWER.CREATOR_NAME is
'参与投票人名称';

comment on column VT_VOTE_ANSWER.CREATE_TIME is
'投选时间';

/*==============================================================*/
/* Table: VT_VOTE_INFO                                          */
/*==============================================================*/
create table if not exists VT_VOTE_INFO (
   ID                   VARCHAR(40)          not null,
   ORG_ID               VARCHAR(40)          not null,
   TITLE                VARCHAR(255)         null,
   CONTENT              VARCHAR(512)         null,
   IMAGE                VARCHAR(255)         null,
   MULTIPLE             VARCHAR(5)           not null default '1'
      constraint CKC_MULTIPLE_VT_VOTE_ check (MULTIPLE in ('1','0')),
   MAX_CHOOSE           DECIMAL(10)          not null default 0,
   ANONYMOUS            VARCHAR(5)           not null default '0'
      constraint CKC_ANONYMOUS_VT_VOTE_ check (ANONYMOUS in ('1','0')),
   EXPIRE               TIMESTAMP            null,
   READ_COUNT           DECIMAL(10)          not null default 0,
   CREATOR              VARCHAR(40)          not null,
   CREATOR_NAME         VARCHAR(255)         null,
   CREATE_TIME          TIMESTAMP            null,
   STATE                CHAR(1)              null,
   IS_EXPIRED           VARCHAR(5)           not null default '0'
      constraint CKC_IS_EXPIRED_VT_VOTE_ check (IS_EXPIRED in ('1','0')),
   constraint vt_vote_info_pkey primary key (ID)
)
WITH (OIDS=FALSE);

comment on column VT_VOTE_INFO.ID is
'投票id';

comment on column VT_VOTE_INFO.ORG_ID is
'所属机构id';

comment on column VT_VOTE_INFO.TITLE is
'投票标题';

comment on column VT_VOTE_INFO.CONTENT is
'投票内容描述';

comment on column VT_VOTE_INFO.IMAGE is
'投票图片id';

comment on column VT_VOTE_INFO.MULTIPLE is
'是否多选:1是,0否,默认1';

comment on column VT_VOTE_INFO.MAX_CHOOSE is
'如果是多选时,最多选几项';

comment on column VT_VOTE_INFO.ANONYMOUS is
'是否匿名投票:1是,0否,默认0(不记名投票)';

comment on column VT_VOTE_INFO.EXPIRE is
'投票截止日期';

comment on column VT_VOTE_INFO.READ_COUNT is
'浏览阅读数';

comment on column VT_VOTE_INFO.CREATOR is
'创建人id';

comment on column VT_VOTE_INFO.CREATOR_NAME is
'创建人名称';

comment on column VT_VOTE_INFO.CREATE_TIME is
'创建日期';

comment on column VT_VOTE_INFO.STATE is
'状态:0无效,1有效';

comment on column VT_VOTE_INFO.IS_EXPIRED is
'是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新';

/*==============================================================*/
/* Table: VT_VOTE_ITEM                                          */
/*==============================================================*/
create table if not exists VT_VOTE_ITEM (
   ID                   VARCHAR(40)          not null,
   VOTE_INFO_ID         VARCHAR(40)          not null,
   CONTENT              VARCHAR(512)         null,
   IMAGE                VARCHAR(255)         null,
   ITEM_ORDER           DECIMAL(10)          not null default 0,
   constraint vt_vote_item_pkey primary key (ID)
)
WITH (OIDS=FALSE);

comment on column VT_VOTE_ITEM.ID is
'投票项id';

comment on column VT_VOTE_ITEM.VOTE_INFO_ID is
'所属投票id';

comment on column VT_VOTE_ITEM.CONTENT is
'投票项内容描述';

comment on column VT_VOTE_ITEM.IMAGE is
'投票项图片id';

comment on column VT_VOTE_ITEM.ITEM_ORDER is
'选项顺序';

/*==============================================================*/
/* Table: VT_VOTE_SCOPE                                         */
/*==============================================================*/
create table if not exists VT_VOTE_SCOPE (
   VOTE_INFO_ID         VARCHAR(40)          not null,
   ORG_ID               VARCHAR(40)          not null,
   USER_ID              VARCHAR(40)          not null,
   constraint vt_vote_scope_pkey primary key (VOTE_INFO_ID, ORG_ID, USER_ID)
)
WITH (OIDS=FALSE);

comment on column VT_VOTE_SCOPE.VOTE_INFO_ID is
'投票信息id';

comment on column VT_VOTE_SCOPE.ORG_ID is
'所属机构id';

comment on column VT_VOTE_SCOPE.USER_ID is
'可参与人id';

