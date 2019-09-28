create table IF NOT EXISTS role (role_id integer not null, role varchar(255), primary key (role_id)) ;
create table IF NOT EXISTS user (user_id integer not null auto_increment, active integer, email varchar(255), firstName varchar(255), lastName varchar(255), 
	password varchar(255), primary key (user_id), CONSTRAINT email_unique UNIQUE(email));
create table IF NOT EXISTS user_role (user_id integer not null, role_id integer not null,
	foreign key (role_id) references role(role_id), 
	foreign key (user_id) references user(user_id) on DELETE CASCADE);



insert into role select * from (
select 1, 'ADMIN' union
select 2, 'CLERK'
) x where not exists(select * from role);


create table IF NOT EXISTS vehicle (id integer not null auto_increment, color varchar(255), plate_no varchar(255), maker varchar(255), model varchar(255), year integer, primary key (id), 
	CONSTRAINT plate_no_unique UNIQUE(plate_no)) engine=InnoDB;


create table IF NOT EXISTS vehicletype (vehicletype_id integer not null, type varchar(255), primary key (vehicletype_id)) ;


insert into vehicletype select * from (
select 1, 'SUV' union
select 2, 'SEADEN' union
select 3, 'TRUCK' 
) x where not exists(select * from vehicletype);


create table IF NOT EXISTS vehicle_vehicletype (id integer not null, vehicletype_id integer not null,
	foreign key (id) references vehicle(id) on DELETE CASCADE, 
	foreign key (vehicletype_id) references vehicletype(vehicletype_id));




create table IF NOT EXISTS client_record (id integer not null auto_increment, firstName varchar(255), lastName varchar(255), driverLicienceNo varchar(255), expiryDate DATE, primary key(id), phoneNo varchar(25) not null,
CONSTRAINT driverLicienceNo UNIQUE(driverLicienceNo)) ;



/*
Insert initial data in client table
*/



insert into client_record select * from (
select 1, 'Nirav', 'Patel', 'DAEPP9040', '2020-10-25', '5142246578' union
select 2, 'Savan', 'Patel', 'XYZ', '2019-10-05', '9033379825' union
select 3, 'Jainil', 'Jadav', 'xyz246', '2019-09-26', '9923476098' union
select 4, 'Kalidas', 'Lepera', 'abc321', '2019-09-26', '8898653412' union
select 5, 'Bharatbhai', 'Sharma', 'fgh435', '2019-10-14', '9923499724' union
select 6, 'Alex', 'Jobs', 'def345', '2019-09-28', '9927654312' union
select 7, 'Nishant', 'Chaudhary', 'xyz765', '2020-06-17', '9923474067' union
select 8, 'Jitendra', 'Gates', 'nbv234x', '2021-06-23', '9923476580' union
select 9, 'Dax', 'Bezos', 'abc342', '2023-04-11', '9924067548' union
select 10, 'John', 'Beleya', 'dbcd675', '2025-04-11', '5146665476'

) x where not exists(select * from client_record);
