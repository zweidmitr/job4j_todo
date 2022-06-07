create table if not exists categories
(
    id   serial primary key,
    name varchar(255)
);

insert into categories(name)
values ('base');
insert into categories(name)
values ('java');
insert into categories(name)
values ('home');
insert into categories(name)
values ('relax');

CREATE TABLE IF NOT EXISTS items_categories
(
    id            serial primary key,
    item_id       int not null references items,
    categories_id int not null references categories
);