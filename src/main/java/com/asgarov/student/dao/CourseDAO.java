package com.asgarov.student.dao;

import java.sql.*;
import java.util.*;

import com.asgarov.student.domain.Course;
import com.asgarov.student.exception.DAOException;

public class CourseDAO extends AbstractDAO<Course, Integer> {

    @Override
    protected String getCreateQuery(Course course) {
        return "INSERT INTO courses (course_name, course_description) VALUES (?,?);";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM courses WHERE course_id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM courses WHERE course_id = ?;";
    }

    @Override
    protected String getCountRowsQuery() {
        return "SELECT count(*) FROM courses;";
    }

    @Override
    protected void setIdStatement(PreparedStatement statement, Integer id) throws DAOException {
        try {
            statement.setInt(1, id);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected void setObjectStatement(PreparedStatement statement, Course course) throws DAOException {
        try {
            if (course.getId() != 0) {
                statement.setString(1, course.getName());
                statement.setString(2, course.getDescription());
                statement.setInt(3, course.getId());
            } else {
                statement.setString(1, course.getName());
                statement.setString(2, course.getDescription());
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Course readObject(ResultSet resultSet) throws DAOException {
        Course course = new Course();
        try {
            course.setId(resultSet.getInt("course_id"));
            course.setName(resultSet.getString("course_name"));
            course.setDescription(resultSet.getString("course_description"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return course;
    }

    @Override
    public void delete(Integer courseId) throws DAOException {
        Course course = read(courseId);
        new StudentCourseDAO().delete(course);
        super.delete(courseId);
    }

    public List<Course> findNotSubscribedCourses(Integer studentId) throws DAOException {
        List<Course> notSubscribedCourses = new ArrayList<>();
        String findCourseIdsThatAStudentDoesNotHaveQuery = "SELECT course_id FROM courses WHERE course_id NOT IN "
                + "(SELECT course_id FROM students_courses WHERE student_id = " + studentId + ");";

        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(findCourseIdsThatAStudentDoesNotHaveQuery);

            CourseDAO courseDAO = new CourseDAO();
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                Course course = courseDAO.read(courseId);
                notSubscribedCourses.add(course);
            }

        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return notSubscribedCourses;
    }

    public Course getRandomCourse() throws DAOException {
        Course course = new Course();

        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM courses LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                course = readObject(resultSet);
            } else {
                throw new DAOException("No course entry was found!");
            }

        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
        return course;
    }
}
