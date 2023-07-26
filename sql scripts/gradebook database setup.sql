CREATE DATABASE  IF NOT EXISTS `gradebook`;
USE `gradebook`;


DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;


CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `users` (`username`,`password`,`enabled`)
VALUES 
('john.johnson@school.com','{noop}john',1),
('mary.marys@student.school.com','{noop}mary',1),
('susan.susanes@school.com','{noop}susan',1);


CREATE TABLE `authorities` (
  `authority_id` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(50) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`authority_id`),
  UNIQUE KEY (`authority`, `user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `authorities` (`authority`, `user_id`)
VALUES 
('ROLE_STUDENT', 2),
('ROLE_TEACHER', 1),
('ROLE_TEACHER', 3),
('ROLE_HEADTEACHER', 3);

CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `class_name` varchar(5) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `students` (`first_name`, `last_name`, `class_name`, `user_id`)
VALUES 
('Mary', 'Marys', '1a', 2);

CREATE TABLE `teachers` (
  `teacher_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`teacher_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `teachers` (`first_name`, `last_name`, `user_id`)
VALUES 
('John', 'Johnson', 1),
('Susan', 'Susanes', 3);