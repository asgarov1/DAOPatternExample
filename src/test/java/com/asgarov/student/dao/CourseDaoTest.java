package com.asgarov.student.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.asgarov.student.data.DataGenerator;
import com.asgarov.student.domain.Course;
import com.asgarov.student.exception.DAOException;
import com.asgarov.student.reader.FileReader;

@TestMethodOrder(OrderAnnotation.class)
public class CourseDaoTest {

    @BeforeAll
    static void generateData() throws Exception {
        new DataGenerator().generateData();
    }

    @Test
    @Order(1)
    void testThatProcessCanCreate10Courses() throws DAOException, IOException {
        CourseDAO courseDAO = new CourseDAO();

        String filePath = "src/resources/courseNames.txt";
        List<String> courseNames = new FileReader().readFile(filePath);

        int numberOfEntitiesBefore = courseDAO.countEntities();
        int numberOfCoursesToCreate = 10;
        for (int i = 0; i < numberOfCoursesToCreate; i++) {
            Course course = new Course(courseNames.get(i), ("Course description " + i));
            courseDAO.create(course);
        }

        int expected = numberOfEntitiesBefore + numberOfCoursesToCreate;
        int actual = courseDAO.countEntities();
        assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    void testThatProcessCanReadCourse() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        int randomCourseId = courseDAO.getRandomCourse().getId();

        Course course = courseDAO.read(randomCourseId);
        assertNotNull(course);
    }

    @Test
    @Order(3)
    void testThatProcessCanUpdateCourse() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();

        int randomCourseId = courseDAO.getRandomCourse().getId();
        Course expected = courseDAO.read(randomCourseId);
        expected.setName("randomSubjectName");

        courseDAO.update(expected);

        Course actual = courseDAO.read(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    @Order(4)
    void testThatProcessCanDeleteCourse() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        int numberOfEntitiesBeforeDelete = courseDAO.countEntities();

        int randomCourseId = courseDAO.getRandomCourse().getId();
        courseDAO.delete(randomCourseId);

        int expected = numberOfEntitiesBeforeDelete - 1;
        int actual = courseDAO.countEntities();
        assertEquals(expected, actual);
    }
}
