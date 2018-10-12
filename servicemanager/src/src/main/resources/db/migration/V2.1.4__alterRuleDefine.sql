ALTER TABLE "mc_alert_rule" ADD COLUMN FUNC                 VARCHAR(64)          null;
ALTER TABLE "mc_alert_rule" ADD COLUMN GROUP_BY             VARCHAR(512)         null;
ALTER TABLE "mc_alert_rule" ADD COLUMN WH_ERE               VARCHAR(1000)        null;
ALTER TABLE "mc_alert_rule" ADD COLUMN QUERY_QL             VARCHAR(4000)        null;
