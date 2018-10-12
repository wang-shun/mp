/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017-10-10 10:23:50                          */
/*==============================================================*/


/*==============================================================*/
/* Table: SU_ANSWER                                             */
/*==============================================================*/
create table SU_ANSWER 
(
   ID                   VARCHAR2(40)         not null,
   QUESTION_ID          VARCHAR2(40)         not null,
   NAME                 VARCHAR2(255  char)  not null,
   ANSWER_ID            VARCHAR2(40)         not null,
   ANSWER               VARCHAR2(512 char)   ,
   SEQU                 NUMBER(10)           default 0 not null,
   constraint PK_SU_ANSWER primary key (ID)
);

/*==============================================================*/
/* Table: SU_ANSWER_PEOPLE                                      */
/*==============================================================*/
create table SU_ANSWER_PEOPLE 
(
   ID                   VARCHAR2(40)         not null,
   SURVEY_ID            VARCHAR2(40)         not null,
   PERSON_NAME          VARCHAR2(255  char)  not null,
   ANSWERED             VARCHAR2(5)          default '0' not null
      constraint CKC_ANSWERED_SU_ANSWE check (ANSWERED in ('1','0')),
   PERSON_ID            VARCHAR2(40)         not null,
   NOTICE_STATUS        CHAR(1)              default '0',
   PERSON_TYPE          VARCHAR2(64 char)    not null,
   BEGIN_TIME           TIMESTAMP,
   SUBMIT_TIME          TIMESTAMP,
   DURATION             NUMBER(10)           default 0 not null,
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
create table SU_COMMON_QUESTION 
(
   ID                   VARCHAR2(40)         not null,
   QUESTION_ID          VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   TAG_NAME             VARCHAR2(64 char)    not null,
   constraint PK_SU_COMMON_QUESTION primary key (ID)
);

/*==============================================================*/
/* Table: SU_PARTICIPANTS                                       */
/*==============================================================*/
create table SU_PARTICIPANTS 
(
   ID                   VARCHAR2(40)         not null,
   TYPE                 VARCHAR2(64 char)    not null,
   OBJECT_ID            VARCHAR2(40)         not null,
   OBJECT_NAME          VARCHAR2(255  char)  not null,
   ENTITY_TYPE          VARCHAR2(64 char)    not null,
   ENTITY_ID            VARCHAR2(40)         not null,
   ENTITY_NAME          VARCHAR2(255  char)  not null,
   DEPT_ORDER           VARCHAR2(1000),
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
create table SU_QUESTION 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   CODE                 VARCHAR2(64 char)    not null,
   QUESTION             VARCHAR2(512 char)   ,
   TYPE                 CHAR(1)              not null,
   SEL_MAX              NUMBER(10)           default 0 not null,
   SEL_MIN              NUMBER(10)           default 0 not null,
   REQUIRED             VARCHAR2(5)          default '1' not null
      constraint CKC_REQUIRED_SU_QUEST check (REQUIRED in ('1','0')),
   USABLE               VARCHAR2(5)          default '0' not null
      constraint CKC_USABLE_SU_QUEST check (USABLE in ('1','0')),
   CREATOR              VARCHAR2(40)         not null,
   CREATE_TIME          TIMESTAMP,
   MODIFIER             VARCHAR2(40),
   MODIFIED_TIME        TIMESTAMP,
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
create table SU_QUESTION_OPTIONS 
(
   ID                   VARCHAR2(40)         not null,
   QUESTION_ID          VARCHAR2(40)         not null,
   OPTIONS             VARCHAR2(512 char),
   CUSTOM_INPUT         CHAR(1)              default '0' not null,
   SEQU                 NUMBER(10)           default 0 not null,
   CREATOR              VARCHAR2(40)         not null,
   CREATE_TIME          TIMESTAMP,
   MODIFIER             VARCHAR2(40),
   MODIFIED_TIME        TIMESTAMP,
   constraint PK_SU_QUESTION_OPTIONS primary key (ID)
);

comment on column SU_QUESTION_OPTIONS.CUSTOM_INPUT is
'0 不支持 1 文本框必输 2 可跳过';

/*==============================================================*/
/* Table: SU_SURVEY                                             */
/*==============================================================*/
create table SU_SURVEY 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   TITLE                VARCHAR2(40)         not null,
   STATUS               VARCHAR2(64 char)    not null,
   EFFECTIVE_TIME       TIMESTAMP,
   END_TIME             TIMESTAMP,
   TARGET_PERSONS       NUMBER(10)           default 0 not null,
   ANSWER_PERSONS       NUMBER(10)           default 0 not null,
   PUSH_TO              VARCHAR2(5)          default '1' not null
      constraint CKC_PUSH_TO_SU_SURVE check (PUSH_TO in ('1','0')),
   PAGER                VARCHAR2(1000),
   CREATOR              VARCHAR2(40)         not null,
   CREATE_TIME          TIMESTAMP,
   MODIFIER             VARCHAR2(40),
   MODIFIED_TIME        TIMESTAMP,
   constraint PK_SU_SURVEY primary key (ID)
);

comment on column SU_SURVEY.STATUS is
'0  创建中 1 发布 2 已完成';

comment on column SU_SURVEY.PAGER is
'逗号分隔，表示分页处于第N个问题后面（问题统计不包含分页符）';

/*==============================================================*/
/* Table: SU_SURVEY_GROUP                                       */
/*==============================================================*/
create table SU_SURVEY_GROUP 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   NAME                 VARCHAR2(255  char)  not null,
   SEQU                 NUMBER(10)           default 0 not null,
   constraint PK_SU_SURVEY_GROUP primary key (ID)
);

/*==============================================================*/
/* Table: SU_SURVEY_QUESTION                                    */
/*==============================================================*/
create table SU_SURVEY_QUESTION 
(
   SURVEY_ID            VARCHAR2(40)         not null,
   QUESTION_ID          VARCHAR2(40)         not null,
   SEQU                 NUMBER(10)           default 0 not null,
   constraint PK_SU_SURVEY_QUESTION primary key (SURVEY_ID, QUESTION_ID)
);

/*==============================================================*/
/* Table: SU_SURVEY_TEMPLATE                                    */
/*==============================================================*/
create table SU_SURVEY_TEMPLATE 
(
   ID                   VARCHAR2(40)         not null,
   ECID                 VARCHAR2(40)         not null,
   TITLE                VARCHAR2(255  char)  not null,
   GROUP_ID             VARCHAR2(40)         ,
   TYPE                 CHAR(1)              default '1',
   OWNER                VARCHAR2(40)         not null,
   PAGER                VARCHAR2(1000),
   STATUS               CHAR(1),
   USE_TIMES            NUMBER(10)           default 0 not null,
   QUESTIONS            NUMBER(10)           default 0 not null,
   CREATOR              VARCHAR2(40),
   CREATE_TIME          TIMESTAMP,
   MODIFIER             VARCHAR2(40),
   MODIFIED_TIME        TIMESTAMP,
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
create table SU_TEMPLATE_QUESTION 
(
   TEMPLATE_ID          VARCHAR2(40)         not null,
   QUESTION_ID          VARCHAR2(40)         not null,
   SEQU                 NUMBER(10)           default 0 not null,
   constraint PK_SU_TEMPLATE_QUESTION primary key (TEMPLATE_ID, QUESTION_ID)
);

