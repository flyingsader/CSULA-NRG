DROP DATABASE NRG;
CREATE DATABASE NRG;
Use NRG;

drop table if exists Devices;
 

CREATE TABLE Devices (
	DeviceID int PRIMARY KEY ,
	DeviceDESC varchar (50) NULL ,
	DeviceOwner varchar (20) NULL ,
	DeviceUsage int NULL ,
	Priority int NULL 
);

CREATE TABLE DeviceData (
	DeviceID int,
	DeviceTimestamp TIMESTAMP,
	PRIMARY KEY (DeviceID, DeviceTimestamp),
	DeviceUsage int NULL  
);

CREATE TABLE WeatherData (
	Locality varchar (20),
	WeatherTimestamp TIMESTAMP,
	PRIMARY KEY (Locality, WeatherTimestamp),
	Temperature int NULL,
	WindSpeed int NULL,
    WindDir int NULL,
	SolarRad int NULL
);

CREATE TABLE GridData (
	Locality varchar (20) ,
	GridTimestamp TIMESTAMP,
	PRIMARY KEY (Locality, GridTimestamp),
	Cap int NULL,
	Demand int NULL
);






