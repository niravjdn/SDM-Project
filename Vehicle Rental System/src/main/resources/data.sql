create table IF NOT EXISTS role (role_id integer not null, role varchar(255), primary key (role_id)) ;
create table IF NOT EXISTS user (user_id integer not null auto_increment, active integer, email varchar(255) not null, firstName varchar(255) not null, lastName varchar(255) not null, 
	password varchar(255) not null, primary key (user_id), CONSTRAINT email_unique UNIQUE(email));
create table IF NOT EXISTS user_role (user_id integer not null, role_id integer not null,
	foreign key (role_id) references role(role_id), 
	foreign key (user_id) references user(user_id) on DELETE CASCADE);



insert into role select * from (
select 1, 'ADMIN' union
select 2, 'CLERK'
) x where not exists(select * from role);


create table IF NOT EXISTS vehicle (id integer not null auto_increment, color varchar(255) not null, plateNo varchar(255) not null, make varchar(255) not null, model varchar(255) not null, year integer not null, primary key (id), 
	CONSTRAINT plate_no_unique UNIQUE(plateNo)) engine=InnoDB;


create table IF NOT EXISTS vehicletype (vehicletype_id integer not null, type varchar(255), primary key (vehicletype_id)) ;


insert into vehicletype select * from (
select 1, 'SUV' union
select 2, 'SEADEN' union
select 3, 'TRUCK' 
) x where not exists(select * from vehicletype);


create table IF NOT EXISTS vehicle_vehicletype (id integer not null, vehicletype_id integer not null,
	foreign key (id) references vehicle(id) on DELETE CASCADE, 
	foreign key (vehicletype_id) references vehicletype(vehicletype_id));




create table IF NOT EXISTS client_record (id integer not null auto_increment, firstName varchar(255) not null, lastName varchar(255) not null, driverLicienceNo varchar(255) not null, expiryDate DATE not null, primary key(id), phoneNo varchar(25) not null,
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



insert into vehicle select * from (
select 1, 'Black', 'GJ 02 BQ 4273', 'Mahindra', 'Xylo', 2019 union
select 2, 'White', 'RJY 16G', 'Audi', 'Q3', 2015 union
select 3, 'Khaki', '5UXZV4C51CL379499', 'Dodge', 'Ram 1500', 2005 union
select 4, 'Green', 'WBAWC7C55AP455059', 'Aston Martin', 'DBS', 2012 union
select 5, 'Turquoise', 'KNADM4A32C6031125', 'Lexus', 'LX', 1999 union
select 6, 'Crimson', 'JTDKN3DU6A0876971', 'Mazda', 'RX-8', 2008  union
select 7, 'Pink', 'WAUWFAFH0EN615344', 'BMW', 'X5', 2009  union
select 8, 'Crimson', '1G6AA5RX5F0530359', 'Subaru', 'Legacy', 2008 union
select 9, 'Pink', 'WAUEH98E77A700599', 'Lincoln', 'Mark VII', 1992 union
select 10, 'Pink', 'WBABD334X4P945075', 'Hyundai', 'Tiburon', 1998 
) x where not exists(select * from vehicle);



/* Reservation */

create table IF NOT EXISTS reservation (id integer not null auto_increment, clientID integer not null, vehicleID integer not null, fromDateTIme DATETIME not null, toDateTime DATETIME not null, primary key(id), 
	createdBy varchar(255),
                                        foreign key (vehicleID) references vehicle(id) on DELETE CASCADE,
                                        foreign key (clientID) references client_record(id) on DELETE CASCADE

 );


/* Mock Data for Reservation */

insert into reservation select * from (
select 1, 3, 2, '2019-10-20 13:00:00', '2019-10-25 17:00:00', 'user'
) x where not exists(select * from reservation);


create table IF NOT EXISTS reservation_history (id integer not null auto_increment, 
	firstName varchar(255) not null, lastName varchar(255) not null, driverLicienceNo varchar(255) not null, 
	expiryDate DATE not null, primary key(id), phoneNo varchar(25) not null,
	color varchar(255) not null, plateNo varchar(255) not null, make varchar(255) not null, model varchar(255) not null, year integer not null,
	fromDateTIme DATETIME not null, toDateTime DATETIME not null,
	updatedOn DateTIME not null,
	createdBy varchar(255)
 );



/* 
Insert auth data
*/

INSERT INTO `user` (`user_id`, `active`, `email`, `firstName`, `lastName`, `password`) VALUES
(1, 1, 'admin@niravjdn.xyz', 'admin', 'admin', '$2a$10$3UY4Ym7etTSepKYjrVUAnOYAi1JX4wkW9L4YYJ6tSJGkHrSv.AAri'),
(2, 1, 'user@niravjdn.xyz', 'user', 'user', '$2a$10$Ckgc3oro24wEdK.C4mYObemK.qAfay0dIflXY/HDwgOYyMcH8o70.'),
(3, 1, 'admin2@niravjdn.xyz', 'admin2', 'admin2', '$2a$10$3UY4Ym7etTSepKYjrVUAnOYAi1JX4wkW9L4YYJ6tSJGkHrSv.AAri');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(3, 1);

