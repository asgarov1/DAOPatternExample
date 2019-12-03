package com.asgarov.student.dao;

import java.sql.*;
import java.util.*;

import com.asgarov.student.domain.*;
import com.asgarov.student.exception.DAOException;

public class StudentCourseDAO extends AbstractDAO<StudentCourse, Integer> {

    @Override
    protected String getCreateQuery(StudentCourse course) {
        return "INSERT INTO students_courses (student_id, course_id) VALUES (?,?);";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM students_courses WHERE student_course_id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE students_courses SET student_id = ?, course_id = ? WHERE student_course_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM students_courses WHERE student_course_id = ?;";
    }

    @Override
    protected String getCountRowsQuery() {
        return "SELECT count(*) FROM students_courses;";
    }

    @Override
    protected void setIdStatement(PreparedStatement statement, Integer studentCourseId) throws DAOException {
        try {
            statement.setInt(1, studentCourseId);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, StudentCourse studentCourse) throws DAOException {
        try {
            if (studentCourse.getStudentCourseId() != 0) {
                statement.setInt(1, studentCourse.getStudentId());
                statement.setInt(2, studentCourse.getCourseId());
                statement.setInt(3, studentCourse.getStudentCourseId());
            } else {
                statement.setInt(1, studentCourse.getStudentId());
                statement.setInt(2, studentCourse.getCourseId());
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected StudentCourse readObject(ResultSet resultSet) throws DAOException {
        StudentCourse studentCourse = new StudentCourse();
        try {
            studentCourse.setStudentCourseId(resultSet.getInt("student_course_id"));
            studentCourse.setStudentId(resultSet.getInt("student_id"));
            studentCourse.setCourseId(resultSet.getInt("course_id"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return studentCourse;
    }

    public StudentCourse read(int studentId, int courseId) throws DAOException {
        String query = "SELECT * FROM students_courses WHERE student_id = " + studentId + 
                        " AND course_id = " + courseId + ";";
        
        StudentCourse studentCourse = new StudentCourse();
        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            
            if (resultSet.next()) {
                studentCourse = readObject(resultSet);
            } 
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return studentCourse;
    }

    public List<StudentCourse> findByStudentId(Integer studentId) throws DAOException {
        List<StudentCourse> studentCourses = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM students_courses WHERE student_id = " + studentId + ";";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int course_id = resultSet.getInt("course_id");
                int studentCouresId = resultSet.getInt("student_course_id");
                studentCourses.add(new StudentCourse(studentCouresId, studentId, course_id));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return studentCourses;
    }

    public void delete(Student student) throws DAOException {
        String query = "DELETE FROM students_courses WHERE student_id = " + student.getStudentId() + ";";
        executeQuery(query);
    }

    public void delete(Course course) throws DAOException {
        String query = "DELETE FROM students_courses WHERE course_id = " + course.getId() + ";";
        executeQuery(query);
    }

    private void executeQuery(String query) throws DAOException {
        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }
}
