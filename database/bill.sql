-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 25, 2024 at 09:29 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `elictric`
--

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

CREATE TABLE `bill` (
  `id` int(20) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `contact` varchar(50) NOT NULL,
  `user` varchar(50) NOT NULL,
  `pass` varchar(250) NOT NULL,
  `status` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill`
--

INSERT INTO `bill` (`id`, `email`, `contact`, `user`, `pass`, `status`, `type`) VALUES
(NULL, 'adasaasfa@gmail.com', '09087043060', 'josephwa', 'qweqweqwe', 'Active', 'Admin'),
(NULL, 'asdasdasd@gmail.com', '09087043060', 'joseph121wa', 'fUSwBg4lF5WofqvPTxobxGQKrJUSRTx/bMu3SrH365Ngte726dGA2iScNXZ2zbOZu13rxOl7Zv+9gdk4SVWDew==', 'PENDING', 'ADMIN'),
(NULL, 'qweqweqwe@gmail.com', '09087043060', 'joseph', 'jXD0i/ezI2Q7NpfowukrWYmfTaxRDC1O2lH2ANk9/EJM1dWg5LZZGU3Ze0wIVtj1ycqLB0YisdtcntILTJiR2Q==', 'PENDING', 'ADMIN'),
(NULL, 'erfgehjm@gmail.com', '5435436546', 'gwrtgweratfgsr', 'wmQ7qNdFElG23wvaM1J/9VNPweOkOV+xYEAi96N/t5VxD1w2I1oJc3bzb61PjLKxO70DL5BERzujdVTFue/gzA==', 'PENDING', 'ADMIN'),
(0, 'hsfadfdgacsv@gmail.com', '8943633534', 'joseph', 'johana121', 'Active', 'Admin'),
(0, '', '', '', '', '', '');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
