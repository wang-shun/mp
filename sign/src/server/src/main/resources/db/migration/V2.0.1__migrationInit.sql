create table SIGN_UPGRADE (
   UPGRADED             VARCHAR(5)           not null default '0'
      constraint CKC_UPGRADED_SIGN_UPGRADE check (UPGRADED in ('1','0')),
   constraint sign_upgrade_pkey primary key (UPGRADED)   
);

insert into SIGN_UPGRADE values('0');      