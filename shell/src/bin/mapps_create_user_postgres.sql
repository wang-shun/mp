--drop database mapps;
--drop user mapps;

CREATE user mapps with PASSWORD 'FHuma025'
  SUPERUSER CREATEDB CREATEROLE
   VALID UNTIL 'infinity';
   
CREATE DATABASE mapps
  WITH OWNER = mapps
       ENCODING = 'UTF8'
       TEMPLATE=TEMPLATE0;
GRANT CONNECT, TEMPORARY ON DATABASE mapps TO public;
GRANT ALL ON DATABASE mapps TO postgres;
GRANT ALL ON DATABASE mapps TO mapps WITH GRANT OPTION;

