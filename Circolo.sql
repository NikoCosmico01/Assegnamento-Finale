CREATE DATABASE SailingClub;

USE SailingClub;

CREATE TABLE Partner(
    Name varchar(50),
    Surname varchar(50),
    Adress varchar(50),
    CF varchar(16) PRIMARY KEY not null,
    ID_Club int not null
);

CREATE TABLE Boat(
    Name varchar(50) not null,
    ID int PRIMARY KEY not null,
    CF_Owner varchar(16) not null,
    Length float
);

CREATE TABLE Payment(
    ID int PRIMARY KEY not null,
    CreditCard_ID varchar(16),
    Expiration varchar(10),
    CV2 varchar(3),
    AccountHolder varchar(20),
    IBAN varchar(30),
    Description varchar(50) not null,
    Execution_Date varchar(20),
    Amount float not null
);

CREATE TABLE Club(
    Name varchar(50) not null,
    ID int PRIMARY KEY not null
);

CREATE TABLE Competition(
    Name varchar(50),
    ID int PRIMARY KEY not null,
    Cost float not null
);

CREATE TABLE Participants(
    CF_Partner varchar(16) not null,
    ID_Boat int not null,
    ID_Competition int not null,
    PRIMARY KEY (ID_Competition, ID_Boat)
);

CREATE TABLE Fees(
    ID_Payment int PRIMARY KEY not null,
    Description varchar(20) not null,
    Cost float not null
);

CREATE TABLE Partner_Payment_Fees(
    CF varchar(16) not null,
    ID_Boat int,
    ID_Payment int not null,
    Expiration date,
    PRIMARY KEY (CF, ID_Payment)
);



