package com.asgarov.student.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.asgarov.student.data.DataGenerator;
import com.asgarov.student.domain.*;
import com.asgarov.student.exception.DAOException;
import com.asgarov.student.runner.*;

@TestMethodOrder(OrderAnnotation.class)
public class StudentCourseDaoTest {
    @BeforeAll
    static void createTables() throws Exception {
        new DataGenerator().generateData();
    }

    @Test
    @Order(1)
    void assignCourseToAStudent() throws Exception {
        new RunnerTest().processShouldAddRandomStudentToRandomCourse();
    }

    @Test
    @Order(2)
    void testThatProcessCanReadAStudentCourse() throws DAOException {
        StudentCourseDAO studentCourseDAO = new StudentCourseDAO();

        Student student = new StudentDAO().getRandomStudent();
        List<StudentCourse> studentCourses = studentCourseDAO.findByStudentId(student.getStudentId());

        assertNotNull(studentCourses);
    }

    @Test
    @Order(3)
    void testThatProcessCanUpdateStudentCourse() throws DAOException, IOException {
        assertThrows(DAOException.class, () -> new StudentCourseDAO().update(new StudentCourse()));
    }

    @Test
    @Order(4)
    void testThatProcessCanDeleteStudentCourse() throws DAOException {
        StudentCourseDAO studentCourseDAO = new StudentCourseDAO();
        int numberOfEntities = studentCourseDAO.countEntities();

        Student student = new StudentDAO().getRandomStudent();
        List<StudentCourse> studentCourses = studentCourseDAO.findByStudentId(student.getStudentId());

        assertNotNull(studentCourses);
        studentCourseDAO.delete(studentCourses.get(0).getStudentCourseId());

        int expected = numberOfEntities - 1;
        int actual = studentCourseDAO.countEntities();
        assertEquals(expected, actual);
    }
}
