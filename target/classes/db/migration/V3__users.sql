create table if not exists users (
  id serial primary key,
  username varchar(50) unique not null,
  password varchar(100) not null,
  roles varchar(100) not null,
  enabled boolean not null default true
);

-- seed demo users (passwords are bcrypt for 'x')
-- bcrypt for 'x' -> $2a$10$ZxWPcShfLFUHd5mPZB7i5u5xGUh1KV5MrWHSdtUtYBO3q5q.Bn496
insert into users (username, password, roles, enabled) values
  ('admin', '$2a$10$ZxWPcShfLFUHd5mPZB7i5u5xGUh1KV5MrWHSdtUtYBO3q5q.Bn496', 'ADMIN', true);

insert into users (username, password, roles, enabled) values
  ('staff', '$2a$10$ZxWPcShfLFUHd5mPZB7i5u5xGUh1KV5MrWHSdtUtYBO3q5q.Bn496', 'STAFF', true);

insert into users (username, password, roles, enabled) values
  ('demo', '$2a$10$ZxWPcShfLFUHd5mPZB7i5u5xGUh1KV5MrWHSdtUtYBO3q5q.Bn496', 'USER', true);
