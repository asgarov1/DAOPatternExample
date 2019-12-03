DROP TABLE IF EXISTS students_courses, groups, students, courses;

CREATE TABLE groups(
	group_id SERIAL PRIMARY KEY,
	group_name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE students (
	student_id SERIAL PRIMARY KEY,
	group_id INT,
	first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL,
	FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses(
	course_id SERIAL PRIMARY KEY,
	course_name VARCHAR(30) NOT NULL,
	course_description VARCHAR(200) NOT NULL
);

create table students_courses
(
  student_course_id SERIAL PRIMARY KEY,
  student_id INT,
  course_id INT,
  CONSTRAINT FK_students FOREIGN KEY (student_id) REFERENCES students (student_id),
  CONSTRAINT FK_courses FOREIGN KEY (course_id) REFERENCES courses (course_id)
);
