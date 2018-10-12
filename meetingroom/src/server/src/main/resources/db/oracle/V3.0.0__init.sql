/*==============================================================*/
/* Table: MR_DISABLE_ROOM  停用记录表                                               */
/*==============================================================*/
CREATE TABLE MR_DISABLE_ROOM
(
  ID                  VARCHAR2(40)          not null,
  ROOM_ID             VARCHAR2(40)          not null,
  DISABLE_BEGIN_TIME  TIMESTAMP                     ,
  DISABLE_END_TIME    TIMESTAMP                     ,
  DISABLE_USER_ID     VARCHAR2(40)                  ,
  DISABLE_USER_NAME   VARCHAR2(255)                 ,
  DISABLE_USER_DEPT   VARCHAR2(255)                 ,
  STATUS              CHAR(1) default '0'   not null,
  constraint PK_MR_DISABLE_ROOM primary key (ID)
);

comment on column MR_DISABLE_ROOM.STATUS is
'0-删除 1-启用';
/*==============================================================*/
/* col: MR_RESERVED                                              */
/*==============================================================*/
alter table MR_RESERVED add RES_REMARK VARCHAR2(512);
/*==============================================================*/
/* Table: MR_RESERVED_RES  物品需求表                                               */
/*==============================================================*/
CREATE TABLE MR_RESERVED_RES
(
  ID                  VARCHAR2(40)          not null,
  RESERVED_ID         VARCHAR2(40)          not null,
  RES_ID              VARCHAR2(40)          not null,
  constraint PK_MR_RESERVED_RES primary key (ID)
);


/*==============================================================*/
/* Table: MR_RES_CONFIG  物品配置表                                               */
/*==============================================================*/
CREATE TABLE MR_RES_CONFIG
(
  ID                  VARCHAR2(40)          not null,
  RES_NAME            VARCHAR2(10)          not null,
  RES_TYPE            VARCHAR2(5)               not null,
  RES_USER_ID         VARCHAR2(40)          not null,
  RES_ORDER           NUMBER(10)            not null,
  constraint PK_MR_RES_CONFIG primary key (ID)
);

comment on column MR_RES_CONFIG.RES_USER_ID is
'0-系统默认项';

comment on column MR_RES_CONFIG.RES_TYPE is
'1-采购 2-设备';

insert into MR_RES_CONFIG(ID,RES_NAME,RES_TYPE,RES_USER_ID,RES_ORDER)
select '1','茶','1',0,10 from dual
union all
select '2','矿泉水','1',0,20 from dual
union all
select '3','水果','1',0,30 from dual
union all
select '4','咖啡','1',0,40 from dual
union all
select '5','点心','1',0,50 from dual
union all
select '6','投影','2',0,10 from dual
union all
select '7','音频','2',0,20 from dual
union all
select '8','席卡','2',0,30 from dual;


comment on column MR_PRIVILEGE.PRIV is
'admin:管理员 service:服务人员 user:普通用户 leader:领导';