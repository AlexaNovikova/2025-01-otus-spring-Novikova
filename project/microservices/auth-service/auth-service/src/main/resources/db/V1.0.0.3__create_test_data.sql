 insert into auth.roles (name)
 values
 ('CLIENT'),
 ('ADMIN'),
 ('EMPLOYEE');

 insert into auth.users (user_name, first_name, last_name, password, email)
 values
 ( 'employee', 'employee', 'employee', '$2a$12$hiKQIOpxfGoZZxBjEGJaFufOYyeHhgHeRagXFJ3RYFfl2nBYB7mrK', 'employee@gmail.com'),
 ( 'client', 'client', 'client', '$2a$12$W/6Eaz5PRQzQdFxl0mwXSONCyFlNVFh2SkA.vvC9NliftnsKaD.sK', 'client@gmail.com'),
 ( 'admin', 'admin', 'admin', '$2a$12$eNxho1JU30heTgwOzcTAjOqdmkkvC9vdW3rguLowZDmurHVbAHzHm', 'admin@gmail.com');

 insert into auth.user_role (user_id, role_id)
 values
(1,3),
(2,1),
(3,2);