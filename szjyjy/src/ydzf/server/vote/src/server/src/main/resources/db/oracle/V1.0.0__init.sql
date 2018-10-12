/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2016/11/24 14:07:10                          */
/*==============================================================*/


/*==============================================================*/
/* Table: VT_VOTE_ANSWER                                        */
/*==============================================================*/
create table VT_VOTE_ANSWER 
(
   ID                   VARCHAR2(40)         not null,
   ORG_ID               VARCHAR2(40)         not null,
   VOTE_INFO_ID         VARCHAR2(40)         not null,
   VOTE_ITEM_ID         VARCHAR2(40)         not null,
   CREATOR              VARCHAR2(40)         not null,
   CREATOR_NAME         VARCHAR2(255),
   CREATE_TIME          TIMESTAMP,
   constraint "t_vote_answer_pkey" primary key (ID)
);

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
create table VT_VOTE_INFO 
(
   ID                   VARCHAR2(40)         not null,
   ORG_ID               VARCHAR2(40)         not null,
   TITLE                VARCHAR2(255),
   CONTENT              VARCHAR2(512),
   IMAGE                VARCHAR2(255),
   MULTIPLE             VARCHAR2(5)          default '1' not null
      constraint CKC_MULTIPLE_VT_VOTE_ check (MULTIPLE in ('1','0')),
   MAX_CHOOSE           NUMBER(10)           default 0 not null,
   ANONYMOUS            VARCHAR2(5)          default '0' not null
      constraint CKC_ANONYMOUS_VT_VOTE_ check (ANONYMOUS in ('1','0')),
   EXPIRE               TIMESTAMP,
   READ_COUNT           NUMBER(10)           default 0 not null,
   CREATOR              VARCHAR2(40)         not null,
   CREATOR_NAME         VARCHAR2(255),
   CREATE_TIME          TIMESTAMP,
   STATE                CHAR(1),
   IS_EXPIRED           VARCHAR2(5)          default '0' not null
      constraint CKC_IS_EXPIRED_VT_VOTE_ check (IS_EXPIRED in ('1','0')),
   constraint "t_vote_info_pkey" primary key (ID)
);

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
create table VT_VOTE_ITEM 
(
   ID                   VARCHAR2(40)         not null,
   VOTE_INFO_ID         VARCHAR2(40)         not null,
   CONTENT              VARCHAR2(512),
   IMAGE                VARCHAR2(255),
   ITEM_ORDER           NUMBER(10)           default 0 not null,
   constraint "t_vote_item_pkey" primary key (ID)
);

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
create table VT_VOTE_SCOPE 
(
   VOTE_INFO_ID         VARCHAR2(40)         not null,
   ORG_ID               VARCHAR2(40)         not null,
   USER_ID              VARCHAR2(40)         not null,
   constraint "t_vote_scope_pkey" primary key (VOTE_INFO_ID, ORG_ID, USER_ID)
);

comment on column VT_VOTE_SCOPE.VOTE_INFO_ID is
'投票信息id';

comment on column VT_VOTE_SCOPE.ORG_ID is
'所属机构id';

comment on column VT_VOTE_SCOPE.USER_ID is
'可参与人id';

