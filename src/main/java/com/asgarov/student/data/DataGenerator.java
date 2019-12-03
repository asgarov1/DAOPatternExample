package com.asgarov.student.data;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.asgarov.student.dao.*;
import com.asgarov.student.domain.*;
import com.asgarov.student.exception.DAOException;
import com.asgarov.student.reader.FileReader;
import com.asgarov.student.service.SQLRunner;

public class DataGenerator {
    public void generateData() throws Exception {
        createTables();

        createGroups(10);
        createCourses(10);
        createStudents(200);
        assignCoursesToEachStudent(3);
    }

    public void assignCoursesToEachStudent(int maximumNumberToAssign) throws Exception {
        int numberOfStudents = new StudentDAO().countEntities();
        StudentCourseDAO studentCourseDAO = new StudentCourseDAO();
        for (int i = 1; i <= numberOfStudents; i++) {
            int studentId = i;

            int randomNumberOfCourses = getRandomNumber(maximumNumberToAssign);
            for (int j = 0; j < randomNumberOfCourses; j++) {
                List<Course> courses = new CourseDAO().findNotSubscribedCourses(studentId);
                int courseId = courses.get(j).getId();

                StudentCourse studentCourse = new StudentCourse(studentId, courseId);
                studentCourseDAO.create(studentCourse);
            }
        }
    }

    private int getRandomNumber(int upperRange) {
        int lowerRange = 1;
        return new Random().nextInt(upperRange - lowerRange) + lowerRange;
    }

    public void createStudents(int numberOfStudentsToCreate) throws IOException, DAOException {
        StudentDAO studentDAO = new StudentDAO();

        for (int i = 0; i < numberOfStudentsToCreate; i++) {
            Student student = new Student(getRandomGroupId(), getRandomFirstName(), getRandomLastName());
            studentDAO.create(student);
        }
    }

    private void createCourses(int numberOfCoursesToCreate) throws IOException, DAOException {
        CourseDAO courseDAO = new CourseDAO();

        String filePath = "src/resources/courseNames.txt";
        List<String> courseNames = new FileReader().readFile(filePath);

        for (int i = 0; i < numberOfCoursesToCreate; i++) {
            Course course = new Course(courseNames.get(i), "course description " + i);
            courseDAO.create(course);
        }
    }

    public void createGroups(int numberOfGroupsToCreate) throws DAOException {
        GroupDAO groupDAO = new GroupDAO();

        for (int i = 0; i < numberOfGroupsToCreate; i++) {
            Group group = new Group(getRandomGroupName());
            groupDAO.create(group);
        }
    }

    public void createTables() throws FileNotFoundException, SQLException {
        String pathToSQLScript = "src/resources/createTables.sql";
        SQLRunner.runSQL(pathToSQLScript);
    }

    public String getRandomGroupName() {
        final String HYPHEN = "-";

        StringBuilder groupName = new StringBuilder();

        groupName.append(getRandomLetter());
        groupName.append(getRandomLetter());
        groupName.append(HYPHEN);
        groupName.append(getRandomDoubleDigit());

        return groupName.toString();
    }

    private char getRandomLetter() {
        final int ASCII_A = 65;
        final int ASCII_Z = 90;
        int randomIndex = new Random().nextInt(ASCII_Z - ASCII_A) + ASCII_A;
        return (char) randomIndex;
    }

    private int getRandomDoubleDigit() {
        return new Random().nextInt(100 - 10) + 10;
    }

    public String getRandomFirstName() throws IOException {
        String filePath = "src/resources/firstNames.txt";
        List<String> firstNames = new FileReader().readFile(filePath);

        final int NAME_MIN_INDEX = 0;
        final int NAME_MAX_INDEX = 19;
        int randomIndex = new Random().nextInt(NAME_MAX_INDEX - NAME_MIN_INDEX) + NAME_MIN_INDEX;

        return firstNames.get(randomIndex);
    }

    public String getRandomLastName() throws IOException {
        String filePath = "src/resources/lastNames.txt";
        List<String> firstNames = new FileReader().readFile(filePath);

        final int NAME_MIN_INDEX = 0;
        final int NAME_MAX_INDEX = 19;
        int randomIndex = new Random().nextInt(NAME_MAX_INDEX - NAME_MIN_INDEX) + NAME_MIN_INDEX;

        return firstNames.get(randomIndex);
    }

    public Integer getRandomGroupId() {
        final int GROUP_MIN_ID = 1;
        final int GROUP_MAX_ID = 10;
        return new Random().nextInt(GROUP_MAX_ID - GROUP_MIN_ID) + GROUP_MIN_ID;
    }

    public Course returnRandomCourseFromStudentsCourses() throws Exception {
        int numberOfEntities = new StudentCourseDAO().countEntities();
        final int minimalSQLIndex = 1;
        int randomIndex = new Random().nextInt(numberOfEntities - minimalSQLIndex) + minimalSQLIndex;

        int courseId;

        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()) {

            String getCourseIdQuery = "SELECT course_id FROM students_courses LIMIT 1 OFFSET " + --randomIndex + ";";

            ResultSet resultSet = statement.executeQuery(getCourseIdQuery);
            if (resultSet.next()) {
                courseId = resultSet.getInt("course_id");
            } else {
                throw new Exception("coulnd't find the course_id in students_courses table [in a test]");
            }
        }

        return new CourseDAO().read(courseId);
    }
}
