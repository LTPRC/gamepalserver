CREATE TABLE if not exists `user_info` (
`id` long not null auto_increment,
`uuid` varchar(36) unique not null,
`username` varchar(50) not null,
`password` varchar(50) not null,
`status` integer not null DEFAULT 0,
`create_time` varchar(19) not null,
`update_time` varchar(19) not null,
primary key (`id`)
--,unique key `uuid_key` (`uuid`) using btree
);
