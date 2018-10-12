/*==============================================================*/
/* Table: MR_DISABLE_ROOM  停用记录表                                               */
/*==============================================================*/
CREATE TABLE MR_DISABLE_ROOM
(
  ID                  VARCHAR(40)          not null,
  ROOM_ID             VARCHAR(40)          not null,
  DISABLE_BEGIN_TIME  TIMESTAMP                     ,
  DISABLE_END_TIME    TIMESTAMP                     ,
  DISABLE_USER_ID     VARCHAR(40)                  ,
  DISABLE_USER_NAME   VARCHAR(255)                 ,
  DISABLE_USER_DEPT   VARCHAR(255)                 ,
  STATUS              CHAR(1) default '0'   not null comment '0-删除 1-启用',
  constraint PK_MR_DISABLE_ROOM primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MR_RESERVED_RES  物品需求表                                               */
/*==============================================================*/
CREATE TABLE MR_RESERVED_RES
(
  ID                  VARCHAR(40)          not null,
  RESERVED_ID         VARCHAR(40)          not null,
  RES_ID              VARCHAR(40)          not null,
  constraint PK_MR_RESERVED_RES primary key (ID)
)CHARSET=utf8;


/*==============================================================*/
/* Table: MR_RES_CONFIG  物品配置表                                               */
/*==============================================================*/
CREATE TABLE MR_RES_CONFIG
(
  ID                  VARCHAR(40)          not null,
  RES_NAME            VARCHAR(10)          not null,
  RES_TYPE            VARCHAR(5)           not null comment '1-采购 2-设备',
  RES_USER_ID         VARCHAR(40)          not null comment '0-系统默认项',
  RES_ORDER           DECIMAL(10)          not null,
  constraint PK_MR_RES_CONFIG primary key (ID)
)CHARSET=utf8;


insert into MR_RES_CONFIG(ID,RES_NAME,RES_TYPE,RES_USER_ID,RES_ORDER)
select '1','茶','1',0,10
union all
select '2','矿泉水','1',0,20
union all
select '3','水果','1',0,30
union all
select '4','咖啡','1',0,40
union all
select '5','点心','1',0,50
union all
select '6','投影','2',0,10
union all
select '7','音频','2',0,20
union all
select '8','席卡','2',0,30;

