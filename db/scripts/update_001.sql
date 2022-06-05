create table if not exists items
(
    id          serial primary key,
    name        text,
    description text,
    created     TIMESTAMP,
    done        boolean,
    user_id     int not null references users (id)
);

create table if not exists users
(
    id       serial primary key,
    name     text,
    email    varchar(255) unique,
    password text
);

drop table items;
drop table users;