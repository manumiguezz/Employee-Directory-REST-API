USE `employee_directory`;

DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;


CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `users` 
VALUES 
('ramiro','{noop}examplepass',1),
('matias','{noop}examplepass',1),
('alejo','{noop}examplepass',1);


CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `authorities` 
VALUES 
('ramiro','ROLE_EMPLOYEE'),
('matias','ROLE_EMPLOYEE'),
('matias','ROLE_MANAGER'),
('alejo','ROLE_EMPLOYEE'),
('alejo','ROLE_MANAGER'),
('alejo','ROLE_ADMIN');

