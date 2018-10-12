alter table su_survey ALTER COLUMN title TYPE varchar(100);

alter table su_survey_template ALTER COLUMN title TYPE varchar(100);

alter table su_question_options ALTER COLUMN options TYPE varchar(2000);

alter table su_question ALTER COLUMN question TYPE varchar(2000);

alter table su_answer ALTER COLUMN name TYPE varchar(2000);

alter table su_answer ALTER COLUMN answer TYPE varchar(2000);