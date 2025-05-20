create table authors (
    id bigserial,
    full_name varchar(255) unique,
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255) unique,
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table comments(
    id bigserial,
    text varchar(255),
    book_id bigint references books(id) on delete cascade,
    primary key (id)
);

create table users (
    id                      bigserial primary key,
    username                varchar(50) not null unique,
    password                varchar(80) not null,
    email                   varchar(255) unique
);

create table roles (
    id                      bigserial primary key,
    name                    varchar(50) not null unique
);

CREATE TABLE users_roles (
          user_id                 bigint not null references users (id),
          role_id                 bigint not null references roles (id),
          primary key (user_id, role_id)
);
