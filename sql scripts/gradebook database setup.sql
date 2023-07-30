CREATE DATABASE  IF NOT EXISTS `gradebook`;
USE `gradebook`;


DROP TABLE IF EXISTS `classes_subjects`;
DROP TABLE IF EXISTS `teachers_subjects`;
DROP TABLE IF EXISTS `subjects`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `classes`;
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
('susan.susanes@school.com','{noop}susan',1),
('jack.jackson@student.school.com','{noop}jack',1);


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
('ROLE_STUDENT', 4),
('ROLE_TEACHER', 1),
('ROLE_TEACHER', 3),
('ROLE_HEADTEACHER', 3);


CREATE TABLE `classes` (
  `class_id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`class_id`),
  UNIQUE KEY (`class_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `classes` (`class_name`)
VALUES 
('1a'),
('2b'),
('3c');


CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `class_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `students` (`first_name`, `last_name`, `class_id`, `user_id`)
VALUES 
('Mary', 'Marys', 1, 2),
('Jack', 'Jackson', 1, 4);


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


CREATE TABLE `subjects` (
  `subject_id` int NOT NULL AUTO_INCREMENT,
  `subject_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`subject_id`),
  UNIQUE KEY (`subject_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `subjects` (`subject_name`)
VALUES 
('Maths'),
('English'),
('Information Technology');


CREATE TABLE `teachers_subjects` (
  `teacher_id` int NOT NULL,
  `subject_id` int NOT NULL,
  PRIMARY KEY (`teacher_id`, `subject_id`),
  FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `teachers_subjects`
VALUES 
(1, 1),
(1, 2),
(2, 1),
(2, 3);


CREATE TABLE `classes_subjects` (
  `class_id` int NOT NULL,
  `subject_id` int NOT NULL,
  PRIMARY KEY (`class_id`, `subject_id`),
  FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `classes_subjects`
VALUES 
(1, 1),
(1, 2),
(2, 1),
(2, 3);