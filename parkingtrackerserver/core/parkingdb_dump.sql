SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Table structure for table `availability`
--

CREATE TABLE IF NOT EXISTS `availability` (
  `id` int(1) NOT NULL,
  `near_empty` int(2) NOT NULL DEFAULT '0',
  `part_full` int(2) NOT NULL DEFAULT '0',
  `near_full` int(2) DEFAULT '0',
  `zid` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `zid` (`zid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `availability`
--

INSERT INTO `availability` (`id`, `near_empty`, `part_full`, `near_full`, `zid`) VALUES
(1, 0, 0, 0, 1),
(2, 0, 0, 0, 2),
(3, 0, 0, 0, 3),
(4, 0, 0, 0, 4),
(5, 0, 0, 0, 5),
(6, 0, 0, 0, 6);

-- --------------------------------------------------------

--
-- Table structure for table `grid_points`
--

CREATE TABLE IF NOT EXISTS `grid_points` (
  `id` varchar(1) NOT NULL,
  `lat` double(8,6) DEFAULT NULL,
  `lng` double(8,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `grid_points`
--

INSERT INTO `grid_points` (`id`, `lat`, `lng`) VALUES
('X', 42.647660, -87.854115),
('Y', 42.648059, -87.853320),
('Z', 42.648388, -87.852134),
('W', 42.648890, -87.852166);

-- --------------------------------------------------------

--
-- Table structure for table `zones`
--

CREATE TABLE IF NOT EXISTS `zones` (
  `id` int(1) NOT NULL DEFAULT '0',
  `lat` double(8,6) DEFAULT NULL,
  `lng` double(8,6) DEFAULT NULL,
  `spots` int(3) DEFAULT NULL,
  `spots_aval` int(3) DEFAULT NULL,
  `color` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `zones`
--

INSERT INTO `zones` (`id`, `lat`, `lng`, `spots`, `spots_aval`, `color`) VALUES
(1, 42.648657, -87.853678, 52, 26, 1),
(2, 42.648229, -87.853729, 61, 12, 2),
(3, 42.647818, -87.853784, 59, 12, 2),
(4, 42.648596, -87.852792, 52, 10, 2),
(5, 42.648196, -87.852773, 111, 22, 2),
(6, 42.647807, -87.852800, 104, 21, 2);
