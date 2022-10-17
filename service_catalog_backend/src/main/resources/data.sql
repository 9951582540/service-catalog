INSERT INTO application_role_master
       (application_role_id
            , application_name
       )
SELECT
       1
     ,'Project Manager'
WHERE
       NOT EXISTS
       (
              SELECT
                     application_role_id
                   , application_name
              FROM
                     application_role_master
              WHERE
                     application_role_id =1
                     AND application_name= 'Project Manager'
       )
;

INSERT INTO application_role_master
       (application_role_id
            , application_name
       )
SELECT
       2
     ,'Employee'
WHERE
       NOT EXISTS
       (
              SELECT
                     application_role_id
                   , application_name
              FROM
                     application_role_master
              WHERE
                     application_role_id =2
                     AND application_name= 'Employee'
       )
;

INSERT INTO application_role_master
       (application_role_id
            , application_name
       )
SELECT
       3
     ,'Senior Manager'
WHERE
       NOT EXISTS
       (
              SELECT
                     application_role_id
                   , application_name
              FROM
                     application_role_master
              WHERE
                     application_role_id =3
                     AND application_name= 'Senior Manager'
       )
;

INSERT INTO application_role_master
       (application_role_id
            , application_name
       )
SELECT
       4
     ,'Admin'
WHERE
       NOT EXISTS
       (
              SELECT
                     application_role_id
                   , application_name
              FROM
                     application_role_master
              WHERE
                     application_role_id =4
                     AND application_name= 'Admin'
       )
;

INSERT INTO application_role_master
       (application_role_id
            , application_name
       )
SELECT
       5
     ,'Leadership'
WHERE
       NOT EXISTS
       (
              SELECT
                     application_role_id
                   , application_name
              FROM
                     application_role_master
              WHERE
                     application_role_id =5
                     AND application_name= 'Leadership'
       )
;

INSERT INTO employee_role_master
       (role_id
            , employee_role_name
       )
SELECT
       5
     ,'Junior Developer'
WHERE
       NOT EXISTS
       (
              SELECT
                     role_id
                   , employee_role_name
              FROM
                     employee_role_master
              WHERE
                     role_id               =5
                     AND employee_role_name= 'Junior Developer'
       )
;

INSERT INTO employee_role_master
       (role_id
            , employee_role_name
       )
SELECT
       6
     ,'Senior Developer'
WHERE
       NOT EXISTS
       (
              SELECT
                     role_id
                   , employee_role_name
              FROM
                     employee_role_master
              WHERE
                     role_id               =6
                     AND employee_role_name= 'Senior Developer'
       )
;

INSERT INTO employee_role_master
       (role_id
            , employee_role_name
       )
SELECT
       7
     ,'Trainee Engineer'
WHERE
       NOT EXISTS
       (
              SELECT
                     role_id
                   , employee_role_name
              FROM
                     employee_role_master
              WHERE
                     role_id               =7
                     AND employee_role_name= 'Trainee Engineer'
       )
;

INSERT INTO employee_role_master
       (role_id
            , employee_role_name
       )
SELECT
       8
     ,'Admin'
WHERE
       NOT EXISTS
       (
              SELECT
                     role_id
                   , employee_role_name
              FROM
                     employee_role_master
              WHERE
                     role_id               =8
                     AND employee_role_name= 'Admin'
       )
;

INSERT INTO employee_master
       (id
            , certification
            , current_role_experience_months
            , email_id
            , employee_id
            , employee_name
            , employee_status
            , manager_id
            , primary_skill
            , secondary_skill
            , total_experience_months
            , application_role_id
            , employee_role_id
       )
SELECT
       16
     ,'N/A'
     , 0
     ,'ADMIN'
     , 1000
     ,'ADMIN'
     ,'Verified'
     , 1000
     ,'N/A'
     ,'N/A'
     , 0
     , 4
     , 8
WHERE
       NOT EXISTS
       (
              SELECT
                     id
                   , certification
                   , current_role_experience_months
                   , email_id
                   , employee_id
                   , employee_name
                   , employee_status
                   , manager_id
                   , primary_skill
                   , secondary_skill
                   , total_experience_months
                   , application_role_id
                   , employee_role_id
              FROM
                     employee_master
              WHERE
                     id                                 = 16
                     AND certification                  ='N/A'
                     AND current_role_experience_months =0
                     AND email_id                       ='ADMIN'
                     AND employee_id                    =1000
                     AND employee_name                  ='ADMIN'
                     AND employee_status                ='Verified'
                     AND manager_id                     =1000
                     AND primary_skill                  ='N/A'
                     AND secondary_skill                ='N/A'
                     AND total_experience_months        =0
                     AND application_role_id            =4
                     AND employee_role_id               =8
       )
;

INSERT INTO employee_register
       (id
            , email
            , employee_id
            , password
            , local_date_time
            , reset_password_token
            , status
            , application_role_id
       )
SELECT
       17
     ,'ADMIN'
     , 1000
     ,'$2a$10$W8t6tbPlq6FiKGOv/POTye15hFjrj0nf9J7OK7p6e0KTUJ/34GARi'
     ,'2022-07-15 14:16:52.310310'
     ,'N/A'
     ,'Approved'
     , 4
WHERE
       NOT EXISTS
       (
              SELECT
                     id
                   , email
                   , employee_id
                   , password
                   , local_date_time
                   , reset_password_token
                   , status
                   , application_role_id
              FROM
                     employee_register
              WHERE
                     employee_id         =1000
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       18
     ,'Data Visualization Development'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 18
                     AND tower_name = 'Data Visualization Development'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       19
     ,'Data Visualization Platform Support'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 19
                     AND tower_name = 'Data Visualization Platform Support'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       20
     ,'Database'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 20
                     AND tower_name = 'Database'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       21
     ,'Development'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 21
                     AND tower_name = 'Development'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       22
     ,'DevOps'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 22
                     AND tower_name = 'DevOps'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       23
     ,'ETL Platform support'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 23
                     AND tower_name = 'ETL Platform support'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       24
     ,'ETL Development'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 24
                     AND tower_name = 'ETL Development'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       25
     ,'Monitoring'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 25
                     AND tower_name = 'Monitoring'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       26
     ,'Network'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 26
                     AND tower_name = 'Network'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       27
     ,'Server'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 27
                     AND tower_name = 'Server'
       )
;

INSERT INTO tower
       (tower_id
            , tower_name
       )
SELECT
       28
     ,'Service Desk'
WHERE
       NOT EXISTS
       (
              SELECT
                     tower_id
                   , tower_name
              FROM
                     tower
              WHERE
                     tower_id       = 28
                     AND tower_name = 'Service Desk'
       )
;

INSERT INTO skill
       (skill_id
            , skill_level
       )
SELECT
       21
     ,'Developer'
WHERE
       NOT EXISTS
       (
              SELECT
                     skill_id
                   , skill_level
              FROM
                     skill
              WHERE
                     skill_id       =21
                     AND skill_level= 'Developer'
       )
;

INSERT INTO skill
       (skill_id
            , skill_level
       )
SELECT
       22
     ,'Jr Developer'
WHERE
       NOT EXISTS
       (
              SELECT
                     skill_id
                   , skill_level
              FROM
                     skill
              WHERE
                     skill_id       =22
                     AND skill_level= 'Jr Developer'
       )
;

INSERT INTO skill
       (skill_id
            , skill_level
       )
SELECT
       23
     ,'Sr Developer'
WHERE
       NOT EXISTS
       (
              SELECT
                     skill_id
                   , skill_level
              FROM
                     skill
              WHERE
                     skill_id       =23
                     AND skill_level= 'Sr Developer'
       )
;

INSERT INTO skill
       (skill_id
            , skill_level
       )
SELECT
       24
     ,'L1'
WHERE
       NOT EXISTS
       (
              SELECT
                     skill_id
                   , skill_level
              FROM
                     skill
              WHERE
                     skill_id       =24
                     AND skill_level= 'L1'
       )
;

INSERT INTO skill
       (skill_id
            , skill_level
       )
SELECT
       25
     ,'L2'
WHERE
       NOT EXISTS
       (
              SELECT
                     skill_id
                   , skill_level
              FROM
                     skill
              WHERE
                     skill_id       =25
                     AND skill_level= 'L2'
       )
;

INSERT INTO skill
       (skill_id
            , skill_level
       )
SELECT
       26
     ,'L3'
WHERE
       NOT EXISTS
       (
              SELECT
                     skill_id
                   , skill_level
              FROM
                     skill
              WHERE
                     skill_id       =26
                     AND skill_level= 'L3'
       )
;

INSERT INTO feedback
       (feedback_id
            , feedback_name
       )
SELECT
       39
     ,'Adaquate competence'
WHERE
       NOT EXISTS
       (
              SELECT
                     feedback_id
                   , feedback_name
              FROM
                     feedback
              WHERE
                     feedback_id      =39
                     AND feedback_name= 'Adaquate competence'
       )
;

INSERT INTO feedback
       (feedback_id
            , feedback_name
       )
SELECT
       40
     ,'Low competence'
WHERE
       NOT EXISTS
       (
              SELECT
                     feedback_id
                   , feedback_name
              FROM
                     feedback
              WHERE
                     feedback_id      =40
                     AND feedback_name= 'Low competence'
       )
;

INSERT INTO feedback
       (feedback_id
            , feedback_name
       )
SELECT
       41
     ,'Over Qualified'
WHERE
       NOT EXISTS
       (
              SELECT
                     feedback_id
                   , feedback_name
              FROM
                     feedback
              WHERE
                     feedback_id      =41
                     AND feedback_name= 'Over Qualified'
       )
;

INSERT INTO feedback
       (feedback_id
            , feedback_name
       )
SELECT
       42
     ,'Qualified for next level'
WHERE
       NOT EXISTS
       (
              SELECT
                     feedback_id
                   , feedback_name
              FROM
                     feedback
              WHERE
                     feedback_id      =42
                     AND feedback_name= 'Qualified for next level'
       )
;

INSERT INTO setting
       (setting_id
            , data
            , description
            , name
       )
SELECT
       1
     ,'"Service Catalog"<noreply_servicecatalog@wisseninfotech.com>'
     ,'Service-catalog centralized mail id'
     ,'service-catalog mail id'
WHERE
       NOT EXISTS
       (
              SELECT
                     setting_id
                   , data
                   , description
                   , name
              FROM
                     setting
              WHERE
                     setting_id      =1
                     AND data        = '"Service Catalog"<noreply_servicecatalog@wisseninfotech.com>'
                     AND description ='Service-catalog centralized mail id'
                     AND name        ='service-catalog mail id'
       )
;

INSERT INTO setting
       (setting_id
            , data
            , description
            , name
       )
SELECT
       2
     ,'jkpcjhhlmsddscdq'
     ,'Service-catalog centralized mail id password'
     ,'service-catalog mail id password'
WHERE
       NOT EXISTS
       (
              SELECT
                     setting_id
                   , data
                   , description
                   , name
              FROM
                     setting
              WHERE
                     setting_id      =2
                     AND data        = 'jkpcjhhlmsddscdq'
                     AND description ='Service-catalog centralized mail id password'
                     AND name        ='service-catalog mail id password'
       )
;



INSERT INTO setting
       (setting_id
            , data
            , description
            , name
       )
SELECT
       5
     ,'587'
     ,'Port number used in mailing'
     ,'port'
WHERE
       NOT EXISTS
       (
              SELECT
                     setting_id
                   , data
                   , description
                   , name
              FROM
                     setting
              WHERE
                     setting_id      =5
                     AND data        = '587'
                     AND description ='Port number used in mailing'
                     AND name        ='port'
       )
;




INSERT INTO status
       (status_id
            , current_employee_status
       )
SELECT
       1
     ,'Occupied'
WHERE
       NOT EXISTS
       (
              SELECT
                     status_id
                   , current_employee_status
              FROM
                     status
              WHERE
                     status_id                  =1
                     AND current_employee_status= 'Occupied'
       )
;

INSERT INTO status
       (status_id
            , current_employee_status
       )
SELECT
       2
     ,'On Bench'
WHERE
       NOT EXISTS
       (
              SELECT
                     status_id
                   , current_employee_status
              FROM
                     status
              WHERE
                     status_id                  =2
                     AND current_employee_status= 'On Bench'
       )
;

INSERT INTO status
       (status_id
            , current_employee_status
       )
SELECT
       3
     ,'Available to share'
WHERE
       NOT EXISTS
       (
              SELECT
                     status_id
                   , current_employee_status
              FROM
                     status
              WHERE
                     status_id                  =3
                     AND current_employee_status= 'Available to share'
       )
;