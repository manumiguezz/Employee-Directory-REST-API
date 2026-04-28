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
('ramiro','{bcrypt}$2a$10$ADqzDoca7inE7JQmfDekDeGshwSHl9mbXTuaycnq46sxGZUqg6wrS',1),
('matias','{bcrypt}$2a$10$Jr6OCkw8ysJJ6qlr/wiB5.Dsnp8oy9fgriVl/QZm5LzxTEXDpr2CK',1),
('alejo','{bcrypt}$2a$10$i1DRxrfOek0k04mGGui6QuXKIyXLDedmS.PcvmMjks2gJBg/b2L9y',1);


CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities4_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities4_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `authorities` 
VALUES 
('ramiro','ROLE_EMPLOYEE'),
('matias','ROLE_EMPLOYEE'),
('matias','ROLE_MANAGER'),
('alejo','ROLE_EMPLOYEE'),
('alejo','ROLE_MANAGER'),
('alejo','ROLE_ADMIN');
