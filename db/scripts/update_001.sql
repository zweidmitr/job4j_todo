create table if not exists items (
    id serial primary key ,
    name text,
    description text,
    created TIMESTAMP,
    done boolean
);

drop table items;