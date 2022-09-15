create database shardingjdbc;
create database shardingjdbc1;
create database shardingjdbc2;
create database shardingjdbc3;
create database shardingjdbc_shadow;

create table t_order
(
    id          bigint primary key auto_increment,
    order_id    bigint,
    order_name  varchar(50),
    order_price bigint,
    user_id     bigint,
    address_id  bigint
);

create table t_order_0
(
    id          bigint primary key auto_increment,
    order_id    bigint,
    order_name  varchar(50),
    order_price bigint,
    address_id  bigint,
    user_id     bigint
);

create table t_order_1
(
    id          bigint primary key auto_increment,
    order_id    bigint,
    order_name  varchar(50),
    order_price bigint,
    address_id  bigint,
    user_id     bigint
);

create table t_order_item
(
    id        bigint primary key auto_increment,
    item_name varchar(50),
    order_id  bigint
);

create table t_order_item_0
(
    id        bigint primary key auto_increment,
    item_name varchar(50),
    order_id  bigint
);

create table t_order_item_1
(
    id        bigint primary key auto_increment,
    item_name varchar(50),
    order_id  bigint
);

create table t_address
(
    id           bigint primary key auto_increment,
    address_name varchar(50)
);

create table t_user
(
    id                bigint primary key auto_increment,
    name              varchar(50),
    age               int,
    password          varchar(255),
    password_plain    varchar(255),
    password_assisted varchar(255)
);
