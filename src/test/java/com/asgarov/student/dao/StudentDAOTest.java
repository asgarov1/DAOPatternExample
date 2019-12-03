package com.asgarov.student.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.sql.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.asgarov.student.data.DataGenerator;
import com.asgarov.student.domain.*;
import com.asgarov.student.exception.DAOException;
import com.asgarov.student.service.SQLRunner;

@TestMethodOrder(OrderAnnotation.class)
class StudentDAOTest {

    @BeforeAll
    static void createTables() throws FileNotFoundException, SQLException, DAOException {
        String pathToSQLScript = "src/resources/createTables.sql";
        SQLRunner.runSQL(pathToSQLScript);

        DataGenerator dataGenerator = new DataGenerator();
        int numberOfGroupsToCreate = 10;
        dataGenerator.createGroups(numberOfGroupsToCreate);
    }

    @Test
    @Order(1)
    void testThatProcessCanCreate200Students() throws DAOException, IOException {
        StudentDAO studentDAO = new StudentDAO();
        DataGenerator dataGenerator = new DataGenerator();

        int numberOfStudentsToCreate = 200;
        dataGenerator.createStudents(numberOfStudentsToCreate);

        int expected = numberOfStudentsToCreate;
        int actual = studentDAO.countEntities();
        assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    void testThatProcessCanReadStudent() throws DAOException {
        int numberOfEntities = new StudentDAO().countEntities();
        Student student = new StudentDAO().read(numberOfEntities - 1);
        assertNotNull(student);
    }

    @Test
    @Order(3)
    void testThatProcessCanUpdateStudent() throws DAOException, IOException {
        StudentDAO studentDAO = new StudentDAO();

        int numberOfEntities = studentDAO.countEntities();
        Student expected = studentDAO.read(numberOfEntities - 1);
        DataGenerator dataGenerator = new DataGenerator();
        expected.setFirstName(dataGenerator.getRandomFirstName());
        expected.setLastName(dataGenerator.getRandomLastName());
        expected.setGroupId(dataGenerator.getRandomGroupId());

        studentDAO.update(expected);

        Student actual = studentDAO.read(expected.getStudentId());

        assertEquals(expected, actual);
    }

    @Test
    @Order(4)
    void testThatProcessCanDeleteStudent() throws DAOException {
        StudentDAO studentDAO = new StudentDAO();
        int numberOfEntitiesBeforeDelete = studentDAO.countEntities();

        int studentId = studentDAO.getRandomStudent().getStudentId();
        studentDAO.delete(studentId);

        int expected = numberOfEntitiesBeforeDelete - 1;
        int actual = studentDAO.countEntities();
        assertEquals(expected, actual);
    }
}
