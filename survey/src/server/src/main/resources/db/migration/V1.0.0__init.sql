/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017-09-06 14:21:38                          */
/*==============================================================*/


/*==============================================================*/
/* Table: SU_ANSWER                                             */
/*==============================================================*/
create table SU_ANSWER (
   ID                   VARCHAR(40)          not null,
   QUESTION_ID          VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   ANSWER_ID            VARCHAR(40)          not null,
   ANSWER               VARCHAR(512)         null,
   SEQU                 NUMERIC(10)          not null default 0,
   constraint PK_SU_ANSWER primary key (ID)
);

/*==============================================================*/
/* Table: SU_ANSWER_PEOPLE                                      */
/*==============================================================*/
create table SU_ANSWER_PEOPLE (
   ID                   VARCHAR(40)          not null,
   SURVEY_ID            VARCHAR(40)          not null,
   PERSON_NAME          VARCHAR(255)         not null,
   ANSWERED             VARCHAR(5)           not null default '0'
      constraint CKC_ANSWERED_SU_ANSWE check (ANSWERED in ('1','0')),
   PERSON_ID            VARCHAR(40)          not null,
   NOTICE_STATUS        CHAR(1)              null default '0',
   PERSON_TYPE          VARCHAR(64)          not null,
   BEGIN_TIME           TIMESTAMP            null,
   SUBMIT_TIME          TIMESTAMP            null,
   DURATION             NUMERIC(10)          not null default 0,
   constraint PK_SU_ANSWER_PEOPLE primary key (ID)
);

comment on column SU_ANSWER_PEOPLE.PERSON_ID is
'内容人员为用户id，外部人员为手机号';

comment on column SU_ANSWER_PEOPLE.NOTICE_STATUS is
'0：未通知 1：已发送 2：已送达';

comment on column SU_ANSWER_PEOPLE.PERSON_TYPE is
'inner：内部人员 outer：外部人员';

comment on column SU_ANSWER_PEOPLE.DURATION is
'单位，秒';

/*==============================================================*/
/* Table: SU_COMMON_QUESTION                                    */
/*==============================================================*/
create table SU_COMMON_QUESTION (
   ID                   VARCHAR(40)          not null,
   QUESTION_ID          VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   TAG_NAME             VARCHAR(64)          not null,
   constraint PK_SU_COMMON_QUESTION primary key (ID)
);

/*==============================================================*/
/* Table: SU_PARTICIPANTS                                       */
/*==============================================================*/
create table SU_PARTICIPANTS (
   ID                   VARCHAR(40)          not null,
   TYPE                 VARCHAR(64)          not null,
   OBJECT_ID            VARCHAR(40)          not null,
   OBJECT_NAME          VARCHAR(255)         not null,
   ENTITY_TYPE          VARCHAR(64)          not null,
   ENTITY_ID            VARCHAR(40)          not null,
   ENTITY_NAME          VARCHAR(255)         not null,
   DEPT_ORDER           VARCHAR(1000)        null,
   constraint PK_SU_PARTICIPANTS primary key (ID)
);

comment on table SU_PARTICIPANTS is
'通用型人员设置信息';

comment on column SU_PARTICIPANTS.TYPE is
'参加对象类型';

comment on column SU_PARTICIPANTS.OBJECT_ID is
'参与人员说参加的对象ID';

comment on column SU_PARTICIPANTS.OBJECT_NAME is
'参与人员说参加的对象名称';

comment on column SU_PARTICIPANTS.ENTITY_TYPE is
'user：用户 dept：部门';

comment on column SU_PARTICIPANTS.DEPT_ORDER is
'说选择部门的order或者人员所在部门的order';

/*==============================================================*/
/* Table: SU_QUESTION                                           */
/*==============================================================*/
create table SU_QUESTION (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   CODE                 VARCHAR(64)          not null,
   QUESTION             VARCHAR(512)         null,
   TYPE                 CHAR(1)              not null,
   SEL_MAX              NUMERIC(10)          not null default 0,
   SEL_MIN              NUMERIC(10)          not null default 0,
   REQUIRED             VARCHAR(5)           not null default '1'
      constraint CKC_REQUIRED_SU_QUEST check (REQUIRED in ('1','0')),
   USABLE               VARCHAR(5)           not null default '0'
      constraint CKC_USABLE_SU_QUEST check (USABLE in ('1','0')),
   CREATOR              VARCHAR(40)          not null,
   CREATE_TIME          TIMESTAMP            null,
   MODIFIER             VARCHAR(40)          null,
   MODIFIED_TIME        TIMESTAMP            null,
   constraint PK_SU_QUESTION primary key (ID)
);

comment on column SU_QUESTION.TYPE is
'1 单选 2 多选 3 下拉 4 文本 5 多行文本';

comment on column SU_QUESTION.SEL_MIN is
'缺省为1';

comment on column SU_QUESTION.USABLE is
'1 启用 0 停用';

/*==============================================================*/
/* Table: SU_QUESTION_OPTIONS                                   */
/*==============================================================*/
create table SU_QUESTION_OPTIONS (
   ID                   VARCHAR(40)          not null,
   QUESTION_ID          VARCHAR(40)          not null,
   OPTIONS               VARCHAR(512)         null,
   CUSTOM_INPUT         CHAR(1)              not null default '0',
   SEQU                 NUMERIC(10)          not null default 0,
   CREATOR              VARCHAR(40)          not null,
   CREATE_TIME          TIMESTAMP            null,
   MODIFIER             VARCHAR(40)          null,
   MODIFIED_TIME        TIMESTAMP            null,
   constraint PK_SU_QUESTION_OPTIONS primary key (ID)
);

comment on column SU_QUESTION_OPTIONS.CUSTOM_INPUT is
'0 不支持 1 文本框必输 2 可跳过';

/*==============================================================*/
/* Table: SU_SURVEY                                             */
/*==============================================================*/
create table SU_SURVEY (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   TITLE                VARCHAR(40)          not null,
   STATUS               VARCHAR(64)          not null,
   EFFECTIVE_TIME       TIMESTAMP            null,
   END_TIME             TIMESTAMP            null,
   TARGET_PERSONS       NUMERIC(10)          not null default 0,
   ANSWER_PERSONS       NUMERIC(10)          not null default 0,
   PUSH_TO              VARCHAR(5)           not null default '1'
      constraint CKC_PUSH_TO_SU_SURVE check (PUSH_TO in ('1','0')),
   PAGER                VARCHAR(1000)        null,
   CREATOR              VARCHAR(40)          not null,
   CREATE_TIME          TIMESTAMP            null,
   MODIFIER             VARCHAR(40)          null,
   MODIFIED_TIME        TIMESTAMP            null,
   constraint PK_SU_SURVEY primary key (ID)
);

comment on column SU_SURVEY.STATUS is
'0  创建中 1 发布 2 已完成';

comment on column SU_SURVEY.PAGER is
'逗号分隔，表示分页处于第N个问题后面（问题统计不包含分页符）';

/*==============================================================*/
/* Table: SU_SURVEY_GROUP                                       */
/*==============================================================*/
create table SU_SURVEY_GROUP (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         not null,
   SEQU                 NUMERIC(10)          not null default 0,
   constraint PK_SU_SURVEY_GROUP primary key (ID)
);

/*==============================================================*/
/* Table: SU_SURVEY_QUESTION                                    */
/*==============================================================*/
create table SU_SURVEY_QUESTION (
   SURVEY_ID            VARCHAR(40)          not null,
   QUESTION_ID          VARCHAR(40)          not null,
   SEQU                 NUMERIC(10)          not null default 0,
   constraint PK_SU_SURVEY_QUESTION primary key (SURVEY_ID, QUESTION_ID)
);

/*==============================================================*/
/* Table: SU_SURVEY_TEMPLATE                                    */
/*==============================================================*/
create table SU_SURVEY_TEMPLATE (
   ID                   VARCHAR(40)          not null,
   ECID                 VARCHAR(40)          not null,
   TITLE                VARCHAR(255)         not null,
   GROUP_ID             VARCHAR(40)          null,
   TYPE                 CHAR(1)              null default '1',
   OWNER                VARCHAR(40)          not null,
   PAGER                VARCHAR(1000)        null,
   STATUS               CHAR(1)              null,
   USE_TIMES            NUMERIC(10)          not null default 0,
   QUESTIONS            NUMERIC(10)          not null default 0,
   constraint PK_SU_SURVEY_TEMPLATE primary key (ID)
);

comment on column SU_SURVEY_TEMPLATE.GROUP_ID is
'个人模板没有分组';

comment on column SU_SURVEY_TEMPLATE.TYPE is
'1 公共模板 2 个人模板';

comment on column SU_SURVEY_TEMPLATE.OWNER is
'空是公共模板，非空为对应用户id的模板';

comment on column SU_SURVEY_TEMPLATE.PAGER is
'逗号分隔，表示分页处于第N个问题后面（问题统计不包含分页符）';

/*==============================================================*/
/* Table: SU_TEMPLATE_QUESTION                                  */
/*==============================================================*/
create table SU_TEMPLATE_QUESTION (
   TEMPLATE_ID          VARCHAR(40)          not null,
   QUESTION_ID          VARCHAR(40)          not null,
   SEQU                 NUMERIC(10)          not null default 0,
   constraint PK_SU_TEMPLATE_QUESTION primary key (TEMPLATE_ID, QUESTION_ID)
);

