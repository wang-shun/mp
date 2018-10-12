alter table su_survey modify title NVARCHAR2(100);

alter table su_survey_template modify title NVARCHAR2(100);

alter table su_question_options modify options NVARCHAR2(2000);

alter table su_question modify question NVARCHAR2(2000);

alter table su_answer modify name NVARCHAR2(2000);

alter table su_answer modify answer NVARCHAR2(2000);