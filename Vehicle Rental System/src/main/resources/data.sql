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



create table IF NOT EXISTS `vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `color` varchar(255) NOT NULL,
  `plateNo` varchar(255) NOT NULL UNIQUE,
  `make` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `year` int(11) NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


create table IF NOT EXISTS client_record (
	id integer not null auto_increment, 
	firstName varchar(255) not null, 
	lastName varchar(255) not null, 
	driverLicienceNo varchar(255) not null,
	 expiryDate DATE not null, primary key(id), 
	 phoneNo varchar(25) not null,
CONSTRAINT driverLicienceNo UNIQUE(driverLicienceNo)) ;

--
-- Dumping data for table `vehicle`
--

create table IF NOT EXISTS client_record (id integer not null auto_increment, firstName varchar(255) not null, lastName varchar(255) not null, driverLicienceNo varchar(255) not null, expiryDate DATE not null, primary key(id), phoneNo varchar(25) not null,
CONSTRAINT driverLicienceNo UNIQUE(driverLicienceNo)) ;





/* Reservation */
create table IF NOT EXISTS reservation (id integer not null auto_increment, typeOfReservation varchar(15) not null, clientID integer not null, vehicleID integer not null, fromDateTIme DATETIME not null, toDateTime DATETIME not null, primary key(id), 
	createdOn DATETIME not null,
                                        foreign key (vehicleID) references vehicle(id) on DELETE CASCADE,
                                        foreign key (clientID) references client_record(id) on DELETE CASCADE

 );


/* Mock Data for Reservation */


create table IF NOT EXISTS reservation_history (id integer PRIMARY KEY,
 `typeOfReservation` varchar(15) NOT NULL,
  `typeOfEndOfTransaction` varchar(10) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `driverLicienceNo` varchar(255) NOT NULL,
  `expiryDate` date NOT NULL,
  `phoneNo` varchar(25) NOT NULL,
  `color` varchar(255) NOT NULL,
  `plateNo` varchar(255) NOT NULL,
  `make` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `year` int(11) NOT NULL,
  `fromDateTIme` datetime NOT NULL,
  `toDateTime` datetime NOT NULL,
  `updatedOn` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



/* 
Insert auth data
*/

insert ignore into `user` values
(1, 1, 'admin@niravjdn.xyz', 'admin', 'admin', '$2a$10$3UY4Ym7etTSepKYjrVUAnOYAi1JX4wkW9L4YYJ6tSJGkHrSv.AAri');


insert ignore into `user` values
(2, 1, 'user@niravjdn.xyz', 'user', 'user', '$2a$10$3UY4Ym7etTSepKYjrVUAnOYAi1JX4wkW9L4YYJ6tSJGkHrSv.AAri');

insert ignore into `user` values
(3, 1, 'user2@niravjdn.xyz', 'user2', 'user2', '$2a$10$3UY4Ym7etTSepKYjrVUAnOYAi1JX4wkW9L4YYJ6tSJGkHrSv.AAri');



insert ignore into `user_role` values (1,1);
insert  ignore into `user_role` values (2,2);


