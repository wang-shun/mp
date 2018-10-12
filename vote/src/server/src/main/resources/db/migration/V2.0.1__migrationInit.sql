create table VT_VOTE_UPGRADE (
   UPGRADED             VARCHAR(5)           not null default '0'
      constraint CKC_UPGRADED_VT_VOTE_UPGRADE check (UPGRADED in ('1','0')),
   constraint t_vote_upgrade_pkey primary key (UPGRADED)   
);

insert into VT_VOTE_UPGRADE values('0');      