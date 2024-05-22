-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2024 at 05:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `elictic`
--

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

CREATE TABLE `bill` (
  `id` int(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `contact` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(250) NOT NULL,
  `status` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `image` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill`
--

INSERT INTO `bill` (`id`, `email`, `contact`, `username`, `password`, `status`, `type`, `image`) VALUES
(14, '', '12312312312', 'USERNAME', 'tA0DOG+4/X3GypxvjWgN/FjWD9bqcsY/VzzFv7sJaVdGqEnJFRfCNc3+rKTnNCCPGEvAiq4GeXdImI6Vko+AsQ==', 'ACTIVE', 'ADMIN', 'src/ImageDB/2.png'),
(16, '1', '12312312312', '12321321321', 'tA0DOG+4/X3GypxvjWgN/FjWD9bqcsY/VzzFv7sJaVdGqEnJFRfCNc3+rKTnNCCPGEvAiq4GeXdImI6Vko+AsQ==', 'ACTIVE', 'CUSTOMER', 'src/ImageDB/2.png');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `t_id` int(50) NOT NULL,
  `t_month` varchar(50) NOT NULL,
  `t_tax` varchar(50) NOT NULL,
  `t_unit` varchar(50) NOT NULL,
  `t_total` varchar(50) NOT NULL,
  `t_reference` varchar(50) NOT NULL,
  `t_payment` varchar(50) DEFAULT NULL,
  `t_status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`t_id`, `t_month`, `t_tax`, `t_unit`, `t_total`, `t_reference`, `t_payment`, `t_status`) VALUES
(16, 'JANUARY', 'VAT (VALUE ADDED TAX)  RATE  = 20%', '200', '240.0', '0bvfRc31EJFvb1C', 'GCASH', 'PAID');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`t_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bill`
--
ALTER TABLE `bill`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `t_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
