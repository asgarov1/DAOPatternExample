package com.asgarov.student.dao;

import java.sql.*;

import com.asgarov.student.domain.Student;
import com.asgarov.student.exception.DAOException;

public class StudentDAO extends AbstractDAO<Student, Integer> {

    @Override
    protected String getCreateQuery(Student student) {
        return "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?;";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM students WHERE student_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM students WHERE student_id = ?";
    }

    @Override
    protected String getCountRowsQuery() {
        return "SELECT count(*) FROM students;";
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
    protected void setObjectStatement(PreparedStatement statement, Student student) throws DAOException {
        try {
            if (student.getStudentId() != 0) {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
                statement.setInt(4, student.getStudentId());
            } else {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Student readObject(ResultSet resultSet) throws DAOException {
        Student student = new Student();
        try {
            student.setStudentId(resultSet.getInt("student_id"));
            student.setGroupId(resultSet.getInt("group_id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return student;
    }

    @Override
    public void delete(Integer studentId) throws DAOException {
        Student student = read(studentId);
        new StudentCourseDAO().delete(student);
        super.delete(studentId);
    }

    public Student getRandomStudent() throws DAOException {
        Student student = new Student();

        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM students LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                student = readObject(resultSet);
            } else {
                throw new DAOException("No student entry was found!");
            }

        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
        return student;
    }
}
