alter table su_survey add title_html text;

alter table su_survey_template add title_html text;

alter table su_question_options add options_html text;

alter table su_question add question_html text;

alter table su_answer add name_html text;

update su_survey set title_html=title;

update su_survey_template set title_html=title;

update su_question_options set options_html=options;

update su_question set question_html=question;

update su_answer set name_html=name;
