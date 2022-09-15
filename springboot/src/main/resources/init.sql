
drop table t_user_1;
drop table t_user_2;
create table t_user_1
(
    user_id   bigint primary key auto_increment,
    name varchar(50)
);

create table t_user_2
(
    user_id   bigint primary key auto_increment,
    name varchar(50)
);

select *from t_user_1;
select *from t_user_2;

