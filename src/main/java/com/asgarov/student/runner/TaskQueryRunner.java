package com.asgarov.student.runner;

import java.sql.*;
import java.util.*;

import com.asgarov.student.dao.*;
import com.asgarov.student.domain.*;
import com.asgarov.student.exception.DAOException;

public class TaskQueryRunner {

    public List<Group> findAllGroupsWithLessOrEqualStudents(int maximalNumberOfStudents)
            throws SQLException, DAOException {
        List<Group> groups = new ArrayList<>();

        String query = "SELECT group_id, count(*)" + " FROM students GROUP BY group_id" + " HAVING count(*) <= ?;";

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, maximalNumberOfStudents);
            ResultSet resultSet = statement.executeQuery();

            GroupDAO groupDAO = new GroupDAO();
            while (resultSet.next()) {
                int id = resultSet.getInt("group_id");
                Group group = groupDAO.read(id);
                groups.add(group);
            }
        }

        return groups;
    }

    public List<Student> findAllStudentsRelatedToCourse(String courseName) throws SQLException, DAOException {
        List<Student> students = new ArrayList<Student>();

        String query = "SELECT * FROM students_courses " + "NATURAL JOIN courses WHERE course_name = '" + courseName
                + "';";

        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            StudentDAO studentDAO = new StudentDAO();

            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                Student student = studentDAO.read(id);
                students.add(student);
            }
        }
        return students;
    }

    public void addStudent(Student student) throws DAOException {
        new StudentDAO().create(student);
    }

    public void deleteStudent(int studentId) throws DAOException {
        new StudentDAO().delete(studentId);
    }

    public void addStudentToCourse(Student student, Course course) throws DAOException, SQLException {
        StudentCourseDAO studentCourseDAO = new StudentCourseDAO();
        StudentCourse studentCourse = new StudentCourse(student.getStudentId(), course.getId());

        studentCourseDAO.create(studentCourse);
    }

    public void removeStudentFromCourse(Student student, Course course) throws Exception {
        StudentCourseDAO studentCourseDAO = new StudentCourseDAO();
        StudentCourse studentCourse = studentCourseDAO.read(student.getStudentId(), course.getId());

        studentCourseDAO.delete(studentCourse.getStudentCourseId());
    }
}
