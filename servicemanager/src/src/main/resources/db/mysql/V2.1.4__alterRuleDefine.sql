ALTER TABLE MC_ALERT_RULE ADD COLUMN FUNC                 VARCHAR(64)          null;
ALTER TABLE MC_ALERT_RULE ADD COLUMN GROUP_BY             VARCHAR(512)         null;
ALTER TABLE MC_ALERT_RULE ADD COLUMN WH_ERE               VARCHAR(1000)        null;
ALTER TABLE MC_ALERT_RULE ADD COLUMN QUERY_QL             VARCHAR(4000)        null;
