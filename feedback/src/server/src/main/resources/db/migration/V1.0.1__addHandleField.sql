alter table FD_FEEDBACK add column 
   SOLUTION VARCHAR(1000) null;

alter table FD_FEEDBACK add column 
   PROBLEM VARCHAR(1000) null;

alter table FD_FEEDBACK add column 
   CONFIRM_USER_ID VARCHAR(64) null;

alter table FD_FEEDBACK add column 
   CONFIRM_USER_NAME VARCHAR(255) null;

alter table FD_FEEDBACK
   add column CONFIRM_TIME TIMESTAMP;
   
comment on column FD_FEEDBACK.SOLUTION is
'解决方案';

comment on column FD_FEEDBACK.PROBLEM is
'问题原因';

comment on column FD_FEEDBACK.CONFIRM is
'0:待处理,1:处理中,2:已处理';

alter table FD_FEEDBACK DROP CONSTRAINT CKC_CONFIRM_FD_FEEDB;

alter table FD_FEEDBACK ADD CONSTRAINT CKC_CONFIRM_FD_FEEDB CHECK (CONFIRM in ('2','1','0'));