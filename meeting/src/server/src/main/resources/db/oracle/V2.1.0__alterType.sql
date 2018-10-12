ALTER TABLE MD_PARTICIPANTS MODIFY ( OBJECT_NAME VARCHAR2(255 CHAR));
ALTER TABLE MD_PARTICIPANTS MODIFY ( ENTITY_NAME VARCHAR2(255 CHAR));
ALTER TABLE MD_PARTICIPANTS MODIFY ( DEPT_ORDER VARCHAR2(1000 CHAR));

ALTER TABLE MT_AGENDA MODIFY ( ADDRESS VARCHAR2(255 CHAR));
ALTER TABLE MT_AGENDA MODIFY ( REMARKS VARCHAR2(512 CHAR));

ALTER TABLE MT_ATTACHMENT MODIFY ( FILE_PATH VARCHAR2(255 CHAR));
ALTER TABLE MT_ATTACHMENT MODIFY ( FILE_NAME VARCHAR2(255 CHAR));

ALTER TABLE MT_MEETING MODIFY ( NAME VARCHAR2(255 CHAR));
ALTER TABLE MT_MEETING MODIFY ( ADDRESS VARCHAR2(255 CHAR));

ALTER TABLE MT_REMARKS MODIFY ( REMARKS VARCHAR2(512 CHAR));

ALTER TABLE MT_SIGNIN_RECORD MODIFY ( PERSON_NAME VARCHAR2(255 CHAR));

ALTER TABLE MT_SIGNIN_SEQU MODIFY ( REMARKS VARCHAR2(512 CHAR));

ALTER TABLE MT_SIGNIN_SERV MODIFY ( USER_NAME VARCHAR2(255 CHAR));