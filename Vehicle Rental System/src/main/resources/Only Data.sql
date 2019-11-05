INSERT INTO `vehicle` (`color`, `plateNo`, `make`, `model`, `year`, `type`) VALUES
('Black', 'GJ 02 BQ 4273', 'Mahindra', 'Xylo', 2019, 'SUV'),
('White', 'RJY 16G', 'Audi', 'Q3', 2015, 'TRUCK'),
('Khaki', '5UXZV4C51CL379499', 'Dodge', 'Ram 1500', 2005, 'SEADEN'),
('Turquoise', 'KNADM4A32C6031125', 'Lexus', 'LX', 1999, 'TRUCK'),
('Crimson', 'JTDKN3DU6A0876971', 'Mazda', 'RX-8', 2008, 'SUV'),
('Pink', 'WAUWFAFH0EN615344', 'BMW', 'X5', 2009, 'SEADEN'),
('Crimson', '1G6AA5RX5F0530359', 'Subaru', 'Legacy', 2008, 'SEADEN'),
( 'Pink', 'WBABD334X4P945075', 'Hyundai', 'Tiburon', 1998, 'TRUCK'),
( 'balck', '113', 'Mazda', 'XYX', 2012, 'SUV'),
( 'balck', '1323', 'Maruti', '800', 2012, 'SUV'),
( 'balck', '1312123', 'Jeep', 'XD', 2012, 'SEADEN');




create table IF NOT EXISTS client_record (id integer not null auto_increment, firstName varchar(255) not null, lastName varchar(255) not null, driverLicienceNo varchar(255) not null, expiryDate DATE not null, primary key(id), phoneNo varchar(25) not null,
CONSTRAINT driverLicienceNo UNIQUE(driverLicienceNo)) ;



/*
Insert initial data in client table
*/

INSERT INTO `client_record` ( `firstName`, `lastName`, `driverLicienceNo`, `expiryDate`, `phoneNo`) VALUES
('Nirav', 'Patel', 'DAEPP9040', '2020-10-25', '5142246578'),
('Savan', 'Patel', 'XYZ', '2025-10-05', '9033379825'),
('Jainil', 'Jadav', 'xyz246', '2020-09-26', '9923476098'),
('Kalidas', 'Lepera', 'abc321', '2020-09-26', '8898653412'),
('Bharatbhai', 'Sharma', 'fgh435', '2020-10-14', '9923499724'),
('Alex', 'Jobs', 'def345', '2022-09-28', '9927654312'),
('Nishant', 'Chaudhary', 'xyz765', '2020-06-17', '9923474067'),
('Jitendra', 'Gates', 'nbv234x', '2021-06-23', '9923476580'),
('Dax', 'Bezos', 'abc342', '2023-04-11', '9924067548'),
('John', 'Beleya', 'dbcd675', '2025-04-11', '5146665476');




/* Reservation */

create table IF NOT EXISTS reservation (id integer not null auto_increment, clientID integer not null, vehicleID integer not null, fromDateTIme DATETIME not null, toDateTime DATETIME not null, primary key(id), 
	createdBy varchar(255),
                                        foreign key (vehicleID) references vehicle(id) on DELETE CASCADE,
                                        foreign key (clientID) references client_record(id) on DELETE CASCADE

 );


/* Mock Data for Reservation */


INSERT INTO `reservation` (`id`, `typeOfReservation`, `clientID`, `vehicleID`, `fromDateTIme`, `toDateTime`, `createdOn`) VALUES
(1, 'RESERVATION', 1, 4, '2019-12-15 08:00:00', '2019-11-30 15:00:00', '2019-11-05 17:00:00');



