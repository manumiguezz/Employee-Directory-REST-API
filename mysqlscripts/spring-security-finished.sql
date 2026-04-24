USE `employee_directory`;

DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;


CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `users`
VALUES
('ramiro','{bcrypt}$2a$10$S7GTZIc/KfegnP9H4GNaCOxHkvu0M32H7f9wg/lHl/c97VQtIoVvm',1),
('matias','{bcrypt}$2a$10$DWG0SGnfxqDIjx.LrywatevHjod2EbepV0W3t5d1aOoRLjSQuvszS',1),
('alejo','{bcrypt}$2a$10$0tqmRxOQufHIrIuQWNHa6.M4ei4LbE4NhN6gfUEOOnxpkbzLABGc6',1);


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
