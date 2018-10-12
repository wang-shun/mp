/*==============================================================*/
/* Table: MC_DASHBOARD                                          */
/*==============================================================*/
create table MC_DASHBOARD (
   ID                   VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         null,
   LAYOUT               VARCHAR(4000)        null comment 'json格式布局定义设置',
   IS_DEFAULT           enum('1','0')        not null default '0',
   REFRESH_TIME         VARCHAR(64)          null,
   TIME_RANGE           VARCHAR(64)          null,
   constraint PK_MC_DASHBOARD primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_DASHBOARD_PANEL                                    */
/*==============================================================*/
create table MC_DASHBOARD_PANEL (
   ID                   VARCHAR(40)          not null,
   DASHBOARD_ID         VARCHAR(40)          not null,
   SYSTEM_ID            VARCHAR(40)          not null,
   NAME                 VARCHAR(255)         null,
   SEQU                 NUMERIC(10)          not null default 0,
   CHART_TYPE           CHAR(1)              null comment '1:折线图、2:曲线图、3:堆积图、4:饼图、5:仪表盘',
   constraint PK_MC_DASHBOARD_PANEL primary key (ID)
)CHARSET=utf8;

/*==============================================================*/
/* Table: MC_DASHBOARD_PANEL_SERIES                             */
/*==============================================================*/
create table MC_DASHBOARD_PANEL_SERIES (
   ID                   VARCHAR(40)          not null,
   PANEL_ID             VARCHAR(40)          not null,
   DASHBOARD_ID         VARCHAR(40)          not null,
   RETENTION_POLICY     VARCHAR(64)          not null,
   MEASUREMENT          VARCHAR(64)          not null,
   WHERE_SETTING        VARCHAR(4000)        not null comment 'json',
   FIELDS_SETTING       VARCHAR(4000)        not null comment 'json',
   `SQL`                VARCHAR(4000)        not null,
   constraint PK_MC_DASHBOARD_PANEL_SERIES primary key (ID)
)CHARSET=utf8;

