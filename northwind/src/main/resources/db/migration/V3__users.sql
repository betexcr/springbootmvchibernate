create table if not exists users (
  id serial primary key,
  username varchar(50) unique not null,
  password varchar(100) not null,
  roles varchar(100) not null,
  enabled boolean not null default true
);
-- seed demo users (passwords are bcrypt for 'x')
-- bcrypt for 'x' -> $2a$10$7aO.0f5h9fA3s5Yy7uacfO8gkZbY7w7yq0m8mKk5cZlX3j9b1Xq2u
insert into users (username, password, roles, enabled) values
  ('admin', '$2a$10$7aO.0f5h9fA3s5Yy7uacfO8gkZbY7w7yq0m8mKk5cZlX3j9b1Xq2u', 'ADMIN', true)
  on conflict (username) do nothing;
insert into users (username, password, roles, enabled) values
  ('staff', '$2a$10$7aO.0f5h9fA3s5Yy7uacfO8gkZbY7w7yq0m8mKk5cZlX3j9b1Xq2u', 'STAFF', true)
  on conflict (username) do nothing;
insert into users (username, password, roles, enabled) values
  ('demo', '$2a$10$7aO.0f5h9fA3s5Yy7uacfO8gkZbY7w7yq0m8mKk5cZlX3j9b1Xq2u', 'USER', true)
  on conflict (username) do nothing;
