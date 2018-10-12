/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2016/11/18 10:24:55                          */
/*==============================================================*/
alter table MR_PRIVILEGE
   add column PRIV VARCHAR(64);

comment on column MR_PRIVILEGE.PRIV is
'admin:管理员 service:服务人员 user:普通用户';

alter table MR_RESERVED
   add column NEED_APPROVE VARCHAR(5);

comment on column MR_RESERVED.NEED_APPROVE is
'预订是否需要审批';

comment on column MR_RESERVED.STATUS is
'1-准备中 2-使用中 3-已结束 4-已取消 0-已删除 a-审批中 r-审批拒绝';

