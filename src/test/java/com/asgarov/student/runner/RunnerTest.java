package com.asgarov.student.runner;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.junit.jupiter.api.*;

import com.asgarov.student.dao.*;
import com.asgarov.student.data.DataGenerator;
import com.asgarov.student.domain.*;
import com.asgarov.student.exception.DAOException;

public class RunnerTest {

    @BeforeAll
    static void setup() throws Exception {
        new DataGenerator().generateData();
    }

    @Test
    void processShouldFindAllGroupsWithLessOrEqualsStudentCount() throws SQLException, DAOException {
        int madeUpStudentCount = 4;
        List<Group> students = new TaskQueryRunner().findAllGroupsWithLessOrEqualStudents(madeUpStudentCount);

        assertNotNull(students);
    }

    @Test
    void processShouldFindAllStudentsRelatedToCourse() throws Exception {
        Course course = new DataGenerator().returnRandomCourseFromStudentsCourses();
        List<Student> students = new TaskQueryRunner().findAllStudentsRelatedToCourse(course.getName());

        assertNotNull(students);
    }

    @Test
    void processShouldAddStudent() throws DAOException, IOException {
        StudentDAO studentDAO = new StudentDAO();
        int numberOfStudentsBeforeAdd = studentDAO.countEntities();

        DataGenerator dataGenerator = new DataGenerator();
        Student student = new Student(dataGenerator.getRandomGroupId(), dataGenerator.getRandomFirstName(),
                dataGenerator.getRandomLastName());

        new TaskQueryRunner().addStudent(student);

        int expected = numberOfStudentsBeforeAdd + 1;
        int actual = studentDAO.countEntities();
        assertEquals(expected, actual);
    }

    @Test
    void processShouldDeleteStudent() throws DAOException, IOException {
        StudentDAO studentDAO = new StudentDAO();
        int numberOfStudentsBeforeDelete = studentDAO.countEntities();

        int randomStudentId = studentDAO.getRandomStudent().getStudentId();
        new TaskQueryRunner().deleteStudent(randomStudentId);

        int expected = numberOfStudentsBeforeDelete - 1;
        int actual = studentDAO.countEntities();

        assertEquals(expected, actual);
    }

    @Test
    public void processShouldAddRandomStudentToRandomCourse() throws Exception {
        StudentDAO studentDAO = new StudentDAO();
        int randomStudentId = studentDAO.getRandomStudent().getStudentId();
        Student student = studentDAO.read(randomStudentId);

        List<Course> notSubscribedCourses = new CourseDAO().findNotSubscribedCourses(student.getStudentId());
        Course course = notSubscribedCourses.get(0);

        StudentCourseDAO studentCourseDAO = new StudentCourseDAO();
        int numberOfRowsBeforeAdd = studentCourseDAO.countEntities();

        new TaskQueryRunner().addStudentToCourse(student, course);

        int numberOfRowsAfterAdd = studentCourseDAO.countEntities();

        int expected = numberOfRowsBeforeAdd + 1;
        int actual = numberOfRowsAfterAdd;
        assertEquals(expected, actual);

    }

    @Test
    void processShouldRemoveStudentFromOneOfHisCourses() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {
            int numberOfRowsBeforeDelete = new StudentCourseDAO().countEntities();

            String query = "SELECT student_id FROM students LIMIT 1;";

            ResultSet resultSet = statement.executeQuery(query);

            int studentId;
            if (resultSet.next()) {
                studentId = resultSet.getInt("student_id");
            } else {
                throw new Exception("Couldn't find a single student_id!");
            }

            query = "SELECT course_id FROM students_courses WHERE student_id = " + studentId + " LIMIT 1;";
            resultSet = statement.executeQuery(query);

            int courseId;
            if (resultSet.next()) {
                courseId = resultSet.getInt("course_id");
            } else {
                throw new Exception("Couldn't find a single course_id!");
            }

            Student student = new StudentDAO().read(studentId);
            Course course = new CourseDAO().read(courseId);

            new TaskQueryRunner().removeStudentFromCourse(student, course);

            int expected = numberOfRowsBeforeDelete - 1;
            int actual = new StudentCourseDAO().countEntities();
            assertEquals(expected, actual);
        }
    }
}
