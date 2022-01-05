-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Gen 05, 2022 alle 15:51
-- Versione del server: 10.4.21-MariaDB
-- Versione PHP: 8.0.11

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
  `ID` int(11) NOT NULL,
  `CF_Owner` varchar(16) NOT NULL,
  `Length` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `boat`
--

INSERT INTO `boat` (`Name`, `ID`, `CF_Owner`, `Length`) VALUES
('Fugassa', 10, 'NCLTHEI', 10),
('CinCion', 11, 'NCLTHEI', 14),
('Savonarola', 12, 'MRCSTPD', 20),
('Ghemellidi', 13, 'MRCSTPD', 18.1),
('Mimì', 14, 'ILNTRZZ', 8),
('Coccò', 15, 'ILNTRZZ', 18.9);

-- --------------------------------------------------------

--
-- Struttura della tabella `club`
--

CREATE TABLE `club` (
  `Name` varchar(50) NOT NULL,
  `ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `club`
--

INSERT INTO `club` (`Name`, `ID`) VALUES
('Marinai Del Levante', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `competition`
--

CREATE TABLE `competition` (
  `Name` varchar(50) DEFAULT NULL,
  `ID` int(11) NOT NULL,
  `Cost` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `competition`
--

INSERT INTO `competition` (`Name`, `ID`, `Cost`) VALUES
('The Destroyer Competition', 100, 120.9),
('The Death Challenge', 101, 290.8),
('Trial Competition', 102, 100);

-- --------------------------------------------------------

--
-- Struttura della tabella `fees`
--

CREATE TABLE `fees` (
  `ID_Payment` int(11) NOT NULL,
  `Description` varchar(20) NOT NULL,
  `Cost` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `fees`
--

INSERT INTO `fees` (`ID_Payment`, `Description`, `Cost`) VALUES
(50, 'Membership', 149.99);

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
(14, 101),
(15, 100);

-- --------------------------------------------------------

--
-- Struttura della tabella `partner_payment_fees`
--

CREATE TABLE `partner_payment_fees` (
  `CF` varchar(16) NOT NULL,
  `ID_Boat` int(11) DEFAULT NULL,
  `ID_Payment` int(11) NOT NULL,
  `Expiration` date DEFAULT NULL,
  `ID_Competition` smallint(6) DEFAULT NULL,
  `Description` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `payment`
--

CREATE TABLE `payment` (
  `ID` int(11) NOT NULL,
  `CreditCard_ID` varchar(16) DEFAULT NULL,
  `Expiration` varchar(10) DEFAULT NULL,
  `CV2` varchar(3) DEFAULT NULL,
  `AccountHolder` varchar(20) DEFAULT NULL,
  `IBAN` varchar(30) DEFAULT NULL,
  `Description` varchar(50) NOT NULL,
  `Execution_Date` varchar(20) DEFAULT NULL,
  `Amount` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `person`
--

CREATE TABLE `person` (
  `Name` varchar(50) DEFAULT NULL,
  `Surname` varchar(50) DEFAULT NULL,
  `Address` varchar(50) DEFAULT NULL,
  `CF` varchar(16) NOT NULL,
  `ID_Club` int(11) NOT NULL,
  `UserName` varchar(20) NOT NULL,
  `PassWord` varchar(20) NOT NULL,
  `Manager` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `person`
--

INSERT INTO `person` (`Name`, `Surname`, `Address`, `CF`, `ID_Club`, `UserName`, `PassWord`, `Manager`) VALUES
('Carlo', 'Acutis', 'Via Della Fede 1', 'CRLCTS', 1, 'Carlo', 'Prova', 0),
('Ilenia', 'Truzza', 'Via Dei Mongolspastici', 'ILNTRZZ', 1, 'Ile', 'Prova', 0),
('Marco', 'Calvi', 'Via Di Sto Cavolo 10', 'MRCSTPD', 1, 'Marcolindo', 'Prova', 0),
('Nicolò', 'Thei', 'Via Mozzachiodi', 'NCLTHEI', 1, 'NicoCosmico01', 'Prova', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `boat`
--
ALTER TABLE `boat`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `club`
--
ALTER TABLE `club`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `competition`
--
ALTER TABLE `competition`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `fees`
--
ALTER TABLE `fees`
  ADD PRIMARY KEY (`ID_Payment`);

--
-- Indici per le tabelle `participants`
--
ALTER TABLE `participants`
  ADD PRIMARY KEY (`ID_Boat`,`ID_Competition`);

--
-- Indici per le tabelle `partner_payment_fees`
--
ALTER TABLE `partner_payment_fees`
  ADD PRIMARY KEY (`CF`,`ID_Payment`);

--
-- Indici per le tabelle `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`CF`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
