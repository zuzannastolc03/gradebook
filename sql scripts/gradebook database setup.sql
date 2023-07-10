CREATE DATABASE  IF NOT EXISTS `gradebook`;
USE `gradebook`;

DROP TABLE IF EXISTS `grades`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `teachers_subjects`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `subjects`;
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;


CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `users` 
VALUES 
('john@school.com','{noop}john',1),
('mary@school.com','{noop}mary',1),
('susan@school.com','{noop}susan',1);


CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY (`username`,`authority`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `authorities` 
VALUES 
('john@school.com','ROLE_TEACHER'),
('mary@school.com','ROLE_STUDENT'),
('susan@school.com','ROLE_TEACHER'),
('susan@school.com','ROLE_HEADTEACHER');


CREATE TABLE `students` (
  `username` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `class` varchar(5) NOT NULL,
  UNIQUE KEY (`username`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `students` 
VALUES 
('mary@school.com','Mary', 'Marys', '1a');


CREATE TABLE `teachers` (
  `username` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  UNIQUE KEY (`username`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `teachers` 
VALUES 
('john@school.com','John', 'Johnys'),
('susan@school.com','Susan', 'Susanes');


CREATE TABLE `subjects` (
  `subject_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`subject_id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `subjects` (name) 
VALUES 
('Mathematics'),
('English');


CREATE TABLE `teachers_subjects` (
  `subject_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  
  UNIQUE KEY (`subject_id`,`username`),
  
  KEY (`subject_id`),
  
  FOREIGN KEY (`subject_id`) 
  REFERENCES `subjects` (`subject_id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  FOREIGN KEY (`username`) 
  REFERENCES `teachers` (`username`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `teachers_subjects` 
VALUES 
(1, 'john@school.com'),
(2, 'john@school.com'),
(1, 'susan@school.com');


CREATE TABLE `grades` (
  `grade_id` int NOT NULL AUTO_INCREMENT,
  `students_username` varchar(50) NOT NULL,
  `teachers_username` varchar(50) NOT NULL,
  `subject_id` int NOT NULL,
  `grade` int NOT NULL,
  UNIQUE KEY (`grade_id`),
  FOREIGN KEY (`students_username`) REFERENCES `students` (`username`),
  FOREIGN KEY (`teachers_username`) REFERENCES `teachers_subjects` (`username`),
  FOREIGN KEY (`subject_id`) REFERENCES `teachers_subjects` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Wciąż można dodać ocenę z przedmiotu, którego dany nauczyciel nie prowadzi - naprawić 

INSERT INTO `grades` 
VALUES 
(1, 'mary@school.com', 'john@school.com', 1, 5),
(2, 'mary@school.com', 'susan@school.com', 2, 4);