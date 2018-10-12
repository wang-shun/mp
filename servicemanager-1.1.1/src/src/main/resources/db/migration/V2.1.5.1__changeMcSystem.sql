ALTER TABLE mc_system RENAME "admin_user" TO "db_user";

ALTER TABLE mc_system RENAME "admin_passwd" TO "db_passwd";

update mc_system set db_user = 'metrics' where id='1';
