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
CREATE TABLE if not exists `user_online` (
`id` long not null auto_increment,
`uuid` varchar(36) unique not null,
`login_time` varchar(19) not null,
primary key (`id`)
);

drop table `user_character`;

CREATE TABLE if not exists `user_character` (
`id` long not null auto_increment,
`uuid` varchar(36) unique not null,
`first_name` varchar(20),
`last_name` varchar(20),
`nickname` varchar(20),
`name_color` varchar(10),
`creature` varchar(10),
`gender` varchar(10),
`skin_color` varchar(10),
`hairstyle` varchar(10),
`hair_color` varchar(10),
`eyes` varchar(10),
`face_ratio` varchar(50),
`face_decoration` varchar(50),
`outfit` varchar(50),
`body_decoration` varchar(50),
`avatar` integer DEFAULT 0,
`create_time` varchar(19) not null,
`update_time` varchar(19) not null,
primary key (`id`)
);