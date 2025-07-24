CREATE TABLE IF NOT EXISTS auth.users
(
    id bigserial PRIMARY KEY,
    user_name varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255),
    email varchar(255) not null,
    password varchar (255) not null
);

create table auth.roles (
    id  bigserial primary key,
    name varchar(50) not null unique
);

CREATE TABLE auth.user_role (
          user_id   bigint not null references users (id),
          role_id   bigint not null references roles (id),
          primary key (user_id, role_id)
);