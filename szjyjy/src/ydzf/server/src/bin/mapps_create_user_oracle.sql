
--drop user first

--drop user mapps cascade;

--create user mapps

CREATE USER mapps IDENTIFIED BY "FHuma025"
 DEFAULT TABLESPACE USERS
 TEMPORARY TABLESPACE TEMP
 QUOTA UNLIMITED ON USERS;
 

--grant privilege to mapps

GRANT "CONNECT" TO mapps; 

GRANT "RESOURCE" TO mapps;
ALTER USER mapps DEFAULT ROLE "CONNECT","RESOURCE";

GRANT CREATE  TABLE              TO mapps;
GRANT CREATE  VIEW               TO mapps;
GRANT CREATE  MATERIALIZED VIEW  TO mapps;
GRANT DEBUG CONNECT SESSION      TO mapps;
GRANT CREATE SYNONYM             TO mapps;
GRANT CREATE JOB                 TO mapps;
GRANT ALTER SESSION              TO mapps;
GRANT FORCE TRANSACTION          TO mapps;
GRANT ON COMMIT REFRESH          TO mapps;
GRANT QUERY REWRITE              TO mapps;
GRANT ANALYZE ANY                TO mapps;
GRANT SELECT ANY DICTIONARY      TO mapps;