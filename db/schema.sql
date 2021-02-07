CREATE TABLE accident_types (
  id serial primary key,
  name varchar(2000)
);

CREATE TABLE accident_rules (
  id serial primary key,
  name varchar(2000)
);


CREATE TABLE accidents_rules (
  id serial primary key,
  accident_id integer,
  rule_id integer,
  unique (accident_id, rule_id)
);



CREATE TABLE accident (
  id serial primary key,
  name varchar(2000),
  text varchar(2000),
  address varchar(2000),
  type_id  integer references accident_types(id)
);

insert into accident_types (name) values ('Две машины');
insert into accident_types (name) values ('Машина и человек');
insert into accident_types (name) values ('Машина и велосиед');

insert into rules (name) values ('Статья. 1');
insert into rules (name) values ('Статья. 2');
insert into rules (name) values ('Статья. 3');