-- phpMyAdmin SQL Dump
-- version 5.2.2-dev+20230530.b9084615f6
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 02, 2023 at 04:18 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `affogatoaffaircafe`
--

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `harga` decimal(10,2) NOT NULL,
  `detail` text DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `nama`, `harga`, `detail`, `picture`) VALUES
(1, 'Truffle Cream Pasta', 150000.00, 'Pasta krim dengan truffle hitam, disajikan dengan keju Parmesan parut', 'http://10.0.2.2/ProjectMobile/images/truffle_pasta.png'),
(2, 'Blackpepper Chicken', 145000.00, 'Ayam panggang dengan lapisan blackpepper yang kaya rasa', 'http://10.0.2.2/ProjectMobile/images/blackpapper_chicken.png'),
(3, 'Fried Duck', 180000.00, 'Bebek goreng renyah dengan rempah-rempah eksotis', 'http://10.0.2.2/ProjectMobile/images/fried_duck.png'),
(4, 'Crispy Salmon Skin', 150000.00, 'Kulit salmon yang digoreng hingga renyah, disajikan dengan saus tartar', 'http://10.0.2.2/ProjectMobile/images/crispy_salmon.png'),
(5, 'Chicken Mr. Muklis', 175000.00, 'Ayam panggang spesial ala Chef Muklis dengan bumbu rahasia', 'http://10.0.2.2/ProjectMobile/images/chicken_muklis.png'),
(6, 'Matcha Cream Frappuchino', 70000.00, 'Frappuchino krim dengan rasa matcha asli dan whipped cream', 'http://10.0.2.2/ProjectMobile/images/matcha_cream.png'),
(7, 'Caramel Macchiato', 80000.00, 'Kopi macchiato dengan sentuhan karamel manis dan creamy', 'http://10.0.2.2/ProjectMobile/images/caramel_machiatto.png'),
(8, 'Pumpkin Spice Latte', 95000.00, 'Latte musiman dengan rasa labu dan rempah, topping whipped cream', 'http://10.0.2.2/ProjectMobile/images/pumpkin_spice.png'),
(9, 'Peach Tea', 60000.00, 'Teh aroma peach segar, disajikan dingin atau hangat', 'http://10.0.2.2/ProjectMobile/images/peach_tea.png'),
(10, 'Lemon Mocktail', 95000.00, 'Mocktail menyegarkan dengan rasa lemon asli dan soda', 'http://10.0.2.2/ProjectMobile/images/lemon_mocktail.png');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `gender` enum('male','female') NOT NULL,
  `birthdate` date NOT NULL,
  `password` varchar(255) NOT NULL,
  `profilepicture` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `phone_number`, `name`, `email`, `gender`, `birthdate`, `password`, `profilepicture`) VALUES
(48, '081318707567', 'rasya', 'rasya@gmail.com', 'female', '2023-09-19', '$2y$10$3tHXc941wD65GArkKo2V6uNn0EfSoNr1Og0xCwrhrcFhW84G092z6', NULL),
(49, '081511093067', 'jasmine', 'jasmine@gmail.com', 'female', '2003-10-13', '$2y$10$fFTXARxiqNmeI3e.2KqTbuV3C9cpHgOxRqv5GGky7lxxF3S1Q2cyS', NULL),
(53, '082249094312', 'najwa', 'najwa@gmail.com', 'female', '2003-06-05', '$2y$10$Z8mPLp0Pi9e/TqVzA7kgjOD94GcA3wqdRP3I6uWADDE59dC9zblbe', NULL),
(54, '081213', 'nama', 'nama@gmail.com', 'male', '2023-11-13', '$2y$10$cY/CCnzXE/971bV0V1yaXerbY1eyf.KemBgHA9pykc6.vuD4/NNke', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
