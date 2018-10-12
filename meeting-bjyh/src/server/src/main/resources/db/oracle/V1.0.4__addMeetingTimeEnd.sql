/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/6/23 9:07:03                            */
/*==============================================================*/


alter table MR_RESERVED add meeting_time_end TIMESTAMP;

comment on column MR_RESERVED.meeting_time_end
  is '会议实际结束时间,默认为预约结束时间';

update MR_RESERVED set meeting_time_end = ORDER_TIME_END;