/*==============================================================*/
/* Table: MC_DASHBOARD                                          */
/*==============================================================*/
create table MC_DASHBOARD 
(
   ID                   VARCHAR2(40 char)    not null,
   NAME                 VARCHAR2(255 char),
   LAYOUT               VARCHAR2(4000 char),
   IS_DEFAULT           VARCHAR2(5 char)     default '0' not null
      constraint CKC_IS_DEFAULT_MC_DASHB check (IS_DEFAULT in ('1','0')),
   REFRESH_TIME         VARCHAR2(64 char),
   TIME_RANGE           VARCHAR2(64 char),
   constraint PK_MC_DASHBOARD primary key (ID)
);

comment on column MC_DASHBOARD.LAYOUT is
'json格式布局定义设置';

/*==============================================================*/
/* Table: MC_DASHBOARD_PANEL                                    */
/*==============================================================*/
create table MC_DASHBOARD_PANEL 
(
   ID                   VARCHAR2(40 char)    not null,
   DASHBOARD_ID         VARCHAR2(40 char)    not null,
   SYSTEM_ID            VARCHAR2(40 char)    not null,
   NAME                 VARCHAR2(255 char),
   SEQU                 NUMBER(10)           default 0 not null,
   CHART_TYPE           CHAR(1),
   constraint PK_MC_DASHBOARD_PANEL primary key (ID)
);

comment on column MC_DASHBOARD_PANEL.CHART_TYPE is
'1:折线图、2:曲线图、3:堆积图、4:饼图、5:仪表盘';

/*==============================================================*/
/* Table: MC_DASHBOARD_PANEL_SERIES                             */
/*==============================================================*/
create table MC_DASHBOARD_PANEL_SERIES 
(
   ID                   VARCHAR2(40 char)    not null,
   PANEL_ID             VARCHAR2(40 char)    not null,
   DASHBOARD_ID         VARCHAR2(40 char)    not null,
   RETENTION_POLICY     VARCHAR2(64 char)    not null,
   MEASUREMENT          VARCHAR2(64 char)    not null,
   WHERE_SETTING        VARCHAR2(4000 char)  not null,
   FIELDS_SETTING       VARCHAR2(4000 char)  not null,
   SQL                  VARCHAR2(4000 char)  not null,
   constraint PK_MC_DASHBOARD_PANEL_SERIES primary key (ID)
);

comment on column MC_DASHBOARD_PANEL_SERIES.WHERE_SETTING is
'json';

comment on column MC_DASHBOARD_PANEL_SERIES.FIELDS_SETTING is
'json';
