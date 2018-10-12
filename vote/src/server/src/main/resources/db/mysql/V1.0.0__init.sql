
/*==============================================================*/
/* Table: VT_VOTE_ANSWER                                        */
/*==============================================================*/
create table if not exists VT_VOTE_ANSWER (
   ID                   VARCHAR(40)          not null comment '投选id',
   ORG_ID               VARCHAR(40)          not null comment '所属机构id',
   VOTE_INFO_ID         VARCHAR(40)          not null comment '所投投票id',
   VOTE_ITEM_ID         VARCHAR(40)          not null comment '所投投票项id',
   CREATOR              VARCHAR(40)          not null comment '参与投票人id',
   CREATOR_NAME         VARCHAR(255)         null comment '参与投票人名称',
   CREATE_TIME          TIMESTAMP            null comment '投选时间',
   constraint vt_vote_answer_pkey primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: VT_VOTE_INFO                                          */
/*==============================================================*/
create table if not exists VT_VOTE_INFO (
   ID                   VARCHAR(40)          not null comment '投票id',
   ORG_ID               VARCHAR(40)          not null comment '所属机构id',
   TITLE                VARCHAR(255)         null comment '投票标题',
   CONTENT              VARCHAR(512)         null comment '投票内容描述',
   IMAGE                VARCHAR(255)         null comment '投票图片id',
   MULTIPLE             enum('1','0')        not null default '1' comment '是否多选:1是,0否,默认1',
   MAX_CHOOSE           DECIMAL(10)          not null default 0 comment '如果是多选时,最多选几项',
   ANONYMOUS            enum('1','0')        not null default '0' comment '是否匿名投票:1是,0否,默认0(不记名投票)',
   EXPIRE               TIMESTAMP            null comment '投票截止日期',
   READ_COUNT           DECIMAL(10)          not null default 0 comment '浏览阅读数',
   CREATOR              VARCHAR(40)          not null comment '创建人id',
   CREATOR_NAME         VARCHAR(255)         null comment '创建人名称',
   CREATE_TIME          TIMESTAMP            null comment '创建日期',
   STATE                CHAR(1)              null comment '状态:0无效,1有效',
   IS_EXPIRED           enum('1','0')        not null default '0' comment '是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新',
   ORG_DEPT_ID          VARCHAR(40),
   ORG_DEPT_ORDER       VARCHAR(64),
   constraint vt_vote_info_pkey primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: VT_VOTE_ITEM                                          */
/*==============================================================*/
create table if not exists VT_VOTE_ITEM (
   ID                   VARCHAR(40)          not null comment '投票项id',
   VOTE_INFO_ID         VARCHAR(40)          not null comment '所属投票id',
   CONTENT              VARCHAR(512)         null comment '投票项内容描述',
   IMAGE                VARCHAR(255)         null comment '投票项图片id',
   ITEM_ORDER           DECIMAL(10)          not null default 0 comment '选项顺序',
   constraint vt_vote_item_pkey primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: VT_VOTE_SCOPE                                         */
/*==============================================================*/
create table if not exists VT_VOTE_SCOPE (
   VOTE_INFO_ID         VARCHAR(40)          not null comment '投票信息id',
   ORG_ID               VARCHAR(40)          not null comment '所属机构id',
   USER_ID              VARCHAR(40)          not null comment '可参与人id',
   constraint vt_vote_scope_pkey primary key (VOTE_INFO_ID, ORG_ID, USER_ID)
)CHARSET=utf8;


create table VT_VOTE_UPGRADE (
   UPGRADED             enum('1','0')        not null default '0',
   constraint t_vote_upgrade_pkey primary key (UPGRADED)   
)CHARSET=utf8;

insert into VT_VOTE_UPGRADE values('0');   