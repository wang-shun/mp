alter table SU_SURVEY_TEMPLATE
    add column CREATOR              VARCHAR(40)          null;
    
alter table SU_SURVEY_TEMPLATE
    add column CREATE_TIME          TIMESTAMP            null;
    
alter table SU_SURVEY_TEMPLATE
    add column MODIFIER             VARCHAR(40)          null;
    
alter table SU_SURVEY_TEMPLATE
    add column MODIFIED_TIME        TIMESTAMP            null;    