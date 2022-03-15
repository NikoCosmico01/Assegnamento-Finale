-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 15, 2022 alle 09:16
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
('Fugassa', 10, 'NCLTHEI', 10),
('CinCion', 11, 'NCLTHEI', 14),
('Savonarola', 12, 'MRCSTPD', 20),
('Ghemellidi', 13, 'MRCSTPD', 18.1),
('Ricchi', 24, 'ILNTRZZ', 110),
('Da Sottoscrivere', 30, 'ILNTRZZ', 18.9),
('Plova', 34, 'ILNTRZZ', 8.81),
('AloA', 37, 'ILNTRZZ', 12),
('Suca', 38, 'PPCNDR', 10);

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
('The Death Challenge', 101, 290.7, '2022-02-10', 890.9, '24-13'),
('Trial Competition', 102, 100, '2022-08-08', 1870.88, '0');

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
(34, 101),
(37, 102),
(38, 102);

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
(15, 'ILNTRZZ', 37, '2022-02-09', '2022-03-09', NULL, 'Boat Addon', 95.9, 'CARD That Ends With 3005'),
(19, 'ILNTRZZ', 34, '2022-03-04', NULL, 102, 'Competition Addon', 100, 'IBAN That Ends With 2386'),
(22, 'PPCNDR', NULL, '2022-03-04', '2023-03-04', NULL, 'Membership Registration', 119.9, 'IBAN That Ends With 8899'),
(23, 'PPCNDR', 38, '2022-03-04', '2023-03-04', NULL, 'Boat Addon', 100, 'IBAN That Ends With 8899'),
(24, 'PPCNDR', 38, '2022-03-04', NULL, 102, 'Competition Addon', 100, 'IBAN That Ends With 8899'),
(25, 'ILNTRZZ', 34, '2022-03-06', NULL, 101, 'Competition Addon', 290.7, 'CARD That Ends With 3005'),
(26, 'ILNTRZZ', 37, '2022-03-06', NULL, 102, 'Competition Addon', 100, 'IBAN That Ends With 2386'),
(27, 'ILNTRZZ', 37, '2022-03-06', '2023-03-06', NULL, 'Boat Fee Renewal', 95.9, 'IBAN That Ends With 2386');

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
(13, 'ILNTRZZ', '4246988520043005', '12/23', '434', 'NULL'),
(16, 'ILNTRZZ', 'NULL', NULL, NULL, 'IT77S0300203280737722952386'),
(17, 'MRCSTPD', '1234543256789876', '11/24', '123', 'NULL'),
(20, 'PPCNDR', 'NULL', 'NULL/NULL', 'NULL', 'NL77INGB8057339330542268899');

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
('Carlo', 'Acutis', 'Via Della Fede 1', 'CRLCTS', 'Carlo', 'Prova', 1),
('Ilenia', 'Truzza', 'Via Dei Mongolspastici', 'ILNTRZZ', 'Ile', 'Prova', 0),
('Marco', 'Calvi', 'Via Di Sto Cavolo 10', 'MRCSTPD', 'Marcolindo', 'Prova', 0),
('Nicolò', 'Thei', 'Via Mozzachiodi', 'NCLTHEI', 'NicoCosmico01', 'Prova', 0),
('Andrea', 'Oppici', 'Via Delle Fave', 'PPCNDR', 'Andre', 'oppi', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `boat`
--
ALTER TABLE `boat`
  ADD PRIMARY KEY (`ID`);

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
  ADD KEY `ID_Boat` (`ID_Boat`),
  ADD KEY `ID_Competition` (`ID_Competition`);

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
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;

--
-- AUTO_INCREMENT per la tabella `notification`
--
ALTER TABLE `notification`
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT per la tabella `paymenthistory`
--
ALTER TABLE `paymenthistory`
  MODIFY `ID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT per la tabella `paymentmethods`
--
ALTER TABLE `paymentmethods`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Limiti per le tabelle scaricate
--

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
  ADD CONSTRAINT `paymenthistory_ibfk_1` FOREIGN KEY (`ID_Boat`) REFERENCES `boat` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `paymenthistory_ibfk_2` FOREIGN KEY (`ID_Competition`) REFERENCES `competition` (`ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
