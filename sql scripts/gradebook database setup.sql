CREATE DATABASE  IF NOT EXISTS `gradebook`;
USE `gradebook`;

DROP TABLE IF EXISTS `grades`;
DROP TABLE IF EXISTS `teachers_subjects`;
DROP TABLE IF EXISTS `subjects`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `roles`;
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
('john@school.com','{noop}john',1),
('mary@school.com','{noop}mary',1),
('susan@school.com','{noop}susan',1);


CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `roles` (name)
VALUES 
('ROLE_STUDENT'),('ROLE_TEACHER'),('ROLE_HEADTEACHER');


CREATE TABLE `users_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  
  UNIQUE KEY (`user_id`,`role_id`),
  
  KEY (`user_id`),
  
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `users_roles` 
VALUES 
(1,2),
(2,1),
(3,2),
(3,3);


CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `class` varchar(5) NOT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `students` (`first_name`,`last_name`,`class`)
VALUES 
('Mary', 'Marys', '1a');


CREATE TABLE `teachers` (
  `teacher_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `teachers` (`first_name`,`last_name`)
VALUES 
('John', 'Johnys'),
('Susan', 'Susanes');


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
  `teacher_id` int NOT NULL,
  
  UNIQUE KEY (`subject_id`,`teacher_id`),
  
  KEY (`subject_id`),
  
  FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `teachers_subjects` 
VALUES 
(1, 1),
(2, 1),
(1, 2);


CREATE TABLE `grades` (
  `grade_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `teacher_id` int NOT NULL,
  `subject_id` int NOT NULL,
  `grade` int NOT NULL,
  PRIMARY KEY (`grade_id`),
  FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `grades` (`student_id`,`teacher_id`, `subject_id`, `grade`)
VALUES 
(1, 1, 1, 5),
(1, 2, 2, 4);