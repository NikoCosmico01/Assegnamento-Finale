-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 15, 2022 alle 21:42
-- Versione del server: 10.4.22-MariaDB
-- Versione PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sailingclub`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `boat`
--

CREATE TABLE `boat` (
  `Name` varchar(50) NOT NULL,
  `ID` smallint(6) NOT NULL,
  `CF_Owner` varchar(16) NOT NULL,
  `Length` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `boat`
--

INSERT INTO `boat` (`Name`, `ID`, `CF_Owner`, `Length`) VALUES
('Vittoria', 10, 'MRCSTPD', 10),
('Vernazza', 11, 'PPCNDR', 14),
('LaFugassa', 12, 'MRCSTPD', 20),
('Madoche', 13, 'MRCSTPD', 18.1),
('Alessia', 14, 'ILNTRZZ', 110),
('Serenity', 15, 'ILNTRZZ', 18.9),
('Julia', 16, 'ILNTRZZ', 8.81),
('SeaPlay', 17, 'ILNTRZZ', 12),
('SHANE', 18, 'PPCNDR', 10);

-- --------------------------------------------------------

--
-- Struttura della tabella `competition`
--

CREATE TABLE `competition` (
  `Name` varchar(50) DEFAULT NULL,
  `ID` smallint(6) NOT NULL,
  `Cost` double NOT NULL,
  `Date` date DEFAULT NULL,
  `WinPrice` double DEFAULT NULL,
  `Podium` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `competition`
--

INSERT INTO `competition` (`Name`, `ID`, `Cost`, `Date`, `WinPrice`, `Podium`) VALUES
('The Death Challenge', 1, 290.7, '2022-03-24', 890.9, '0'),
('Trial Competition', 2, 100, '2022-08-08', 1870.88, '0'),
('BestBonus', 3, 390.9, '2022-03-30', 190.88, '0'),
('World Cup', 4, 12000, '2022-05-20', 348000, '0'),
('New Year\'s EveNT', 111, 20, '2022-12-31', 890.9, '0');

-- --------------------------------------------------------

--
-- Struttura della tabella `notification`
--

CREATE TABLE `notification` (
  `ID` smallint(6) NOT NULL,
  `stringObject` varchar(20) NOT NULL,
  `Description` varchar(40) NOT NULL,
  `remDays` int(11) NOT NULL,
  `ID_Payment` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `participants`
--

CREATE TABLE `participants` (
  `ID_Boat` smallint(6) NOT NULL,
  `ID_Competition` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `participants`
--

INSERT INTO `participants` (`ID_Boat`, `ID_Competition`) VALUES
(15, 2),
(16, 1),
(17, 4);

-- --------------------------------------------------------

--
-- Struttura della tabella `paymenthistory`
--

CREATE TABLE `paymenthistory` (
  `ID` smallint(6) NOT NULL,
  `CF` varchar(16) NOT NULL,
  `ID_Boat` smallint(6) DEFAULT NULL,
  `currDate` date NOT NULL,
  `Expiration` date DEFAULT NULL,
  `ID_Competition` smallint(6) DEFAULT NULL,
  `Description` varchar(30) DEFAULT NULL,
  `Amount` double NOT NULL,
  `PaymentMethod` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `paymenthistory`
--

INSERT INTO `paymenthistory` (`ID`, `CF`, `ID_Boat`, `currDate`, `Expiration`, `ID_Competition`, `Description`, `Amount`, `PaymentMethod`) VALUES
(1, 'ILNTRZZ', 17, '2022-02-09', '2022-03-09', NULL, 'Boat Addon', 95.9, 'CARD That Ends With 3005'),
(2, 'ILNTRZZ', 16, '2022-03-04', NULL, 2, 'Competition Addon', 100, 'IBAN That Ends With 2386'),
(3, 'PPCNDR', 18, '2022-03-04', '2023-03-04', NULL, 'Boat Addon', 100, 'IBAN That Ends With 8899'),
(4, 'PPCNDR', 18, '2022-03-04', NULL, 2, 'Competition Addon', 100, 'IBAN That Ends With 8899'),
(5, 'ILNTRZZ', 16, '2022-03-06', NULL, 1, 'Competition Addon', 290.7, 'CARD That Ends With 3005'),
(6, 'ILNTRZZ', 17, '2022-03-06', NULL, 2, 'Competition Addon', 100, 'IBAN That Ends With 2386'),
(7, 'ILNTRZZ', 17, '2022-03-06', '2023-03-06', NULL, 'Boat Fee Renewal', 95.9, 'IBAN That Ends With 2386'),
(8, 'PPCNDR', NULL, '2022-03-04', '2023-03-04', NULL, 'Membership Registration', 119.9, 'IBAN That Ends With 8899'),
(224, 'ILNTRZZ', 17, '2022-03-15', NULL, 4, 'Competition Addon', 12000, 'IBAN That Ends With 2386'),
(225, 'ILNTRZZ', 15, '2022-03-15', NULL, 2, 'Competition Addon', 100, 'CARD That Ends With 3005'),
(226, 'ILNTRZZ', 16, '2022-03-15', NULL, 1, 'Competition Addon', 290.7, 'CARD That Ends With 3005');

-- --------------------------------------------------------

--
-- Struttura della tabella `paymentmethods`
--

CREATE TABLE `paymentmethods` (
  `ID` int(11) NOT NULL,
  `CF` varchar(11) NOT NULL,
  `CreditCard_ID` varchar(16) DEFAULT NULL,
  `Expiration` varchar(10) DEFAULT NULL,
  `CV2` varchar(4) DEFAULT NULL,
  `IBAN` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `paymentmethods`
--

INSERT INTO `paymentmethods` (`ID`, `CF`, `CreditCard_ID`, `Expiration`, `CV2`, `IBAN`) VALUES
(1, 'ILNTRZZ', '4246988520043005', '12/23', '434', 'NULL'),
(2, 'ILNTRZZ', 'NULL', NULL, NULL, 'IT77S0300203280737722952386'),
(3, 'MRCSTPD', '1234543256789876', '11/24', '123', 'NULL'),
(4, 'PPCNDR', 'NULL', NULL, 'NULL', 'NL77INGB8057339330542268899');

-- --------------------------------------------------------

--
-- Struttura della tabella `person`
--

CREATE TABLE `person` (
  `Name` varchar(50) DEFAULT NULL,
  `Surname` varchar(50) DEFAULT NULL,
  `Address` varchar(50) DEFAULT NULL,
  `CF` varchar(16) NOT NULL,
  `UserName` varchar(20) NOT NULL,
  `PassWord` varchar(20) NOT NULL,
  `Manager` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `person`
--

INSERT INTO `person` (`Name`, `Surname`, `Address`, `CF`, `UserName`, `PassWord`, `Manager`) VALUES
('Carlo', 'Acutis', 'Via Della Fede 1/A', 'CRLCTS', 'Carlo', 'Prova', 1),
('Ilenia', 'Truzza', 'Via Dei Melegari 28/B', 'ILNTRZZ', 'Ile', 'Prova', 0),
('Marco', 'Calvi', 'Via Di Sto Cavolo 10', 'MRCSTPD', 'Marcolindo', 'Prova', 0),
('Nicolò', 'Thei', 'Via Mozzachiodi 8/F', 'NCLTHEI', 'NicoCosmico01', 'Prova', 1),
('Andrea', 'Oppici', 'Via Delle Fave 51', 'PPCNDR', 'Andre', 'Prova', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `boat`
--
ALTER TABLE `boat`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `CF_Owner` (`CF_Owner`);

--
-- Indici per le tabelle `competition`
--
ALTER TABLE `competition`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Payment` (`ID_Payment`);

--
-- Indici per le tabelle `participants`
--
ALTER TABLE `participants`
  ADD PRIMARY KEY (`ID_Boat`,`ID_Competition`),
  ADD KEY `ID_Competition` (`ID_Competition`);

--
-- Indici per le tabelle `paymenthistory`
--
ALTER TABLE `paymenthistory`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `paymenthistory_ibfk_1` (`ID_Boat`),
  ADD KEY `paymenthistory_ibfk_2` (`ID_Competition`);

--
-- Indici per le tabelle `paymentmethods`
--
ALTER TABLE `paymentmethods`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`CF`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `boat`
--
ALTER TABLE `boat`
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT per la tabella `competition`
--
ALTER TABLE `competition`
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=112;

--
-- AUTO_INCREMENT per la tabella `notification`
--
ALTER TABLE `notification`
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT per la tabella `paymenthistory`
--
ALTER TABLE `paymenthistory`
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=227;

--
-- AUTO_INCREMENT per la tabella `paymentmethods`
--
ALTER TABLE `paymentmethods`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `boat`
--
ALTER TABLE `boat`
  ADD CONSTRAINT `boat_ibfk_1` FOREIGN KEY (`CF_Owner`) REFERENCES `person` (`CF`);

--
-- Limiti per la tabella `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`ID_Payment`) REFERENCES `paymenthistory` (`ID`);

--
-- Limiti per la tabella `participants`
--
ALTER TABLE `participants`
  ADD CONSTRAINT `participants_ibfk_1` FOREIGN KEY (`ID_Boat`) REFERENCES `boat` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `participants_ibfk_2` FOREIGN KEY (`ID_Competition`) REFERENCES `competition` (`ID`) ON DELETE CASCADE;

--
-- Limiti per la tabella `paymenthistory`
--
ALTER TABLE `paymenthistory`
  ADD CONSTRAINT `paymenthistory_ibfk_1` FOREIGN KEY (`ID_Boat`) REFERENCES `boat` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `paymenthistory_ibfk_2` FOREIGN KEY (`ID_Competition`) REFERENCES `competition` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
