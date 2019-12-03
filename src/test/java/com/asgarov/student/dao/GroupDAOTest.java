package com.asgarov.student.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.asgarov.student.data.DataGenerator;
import com.asgarov.student.domain.Group;
import com.asgarov.student.exception.DAOException;

@TestMethodOrder(OrderAnnotation.class)
public class GroupDAOTest {

    @BeforeAll
    static void createTables() throws Exception {
        new DataGenerator().generateData();
    }

    @Test
    @Order(1)
    void testThatProcessCanCreate10Groups() throws DAOException {
        List<Group> groups = new ArrayList<>();
        GroupDAO groupDAO = new GroupDAO();

        int numberOfGroupsToCreate = 10;
        for (int i = 0; i < numberOfGroupsToCreate; i++) {
            Group group = new Group(new DataGenerator().getRandomGroupName());
            groupDAO.create(group);
            groups.add(group);
        }

        for (int i = 0; i < numberOfGroupsToCreate; i++) {
            Group storedGroup = groups.get(i);

            String name = storedGroup.getName();
            Group actualGroup = groupDAO.findByName(name);
            assertEquals(storedGroup.getName(), actualGroup.getName());
        }
    }

    @Test
    @Order(2)
    void testThatProcessCanReadGroup() throws DAOException {
        GroupDAO groupDAO = new GroupDAO();
        int numberOfEntities = groupDAO.countEntities();

        Group group = groupDAO.read(numberOfEntities - 1);
        assertNotNull(group);
    }

    @Test
    @Order(3)
    void testThatProcessCanUpdateGroup() throws DAOException {
        GroupDAO groupDAO = new GroupDAO();
        int numberOfEntities = groupDAO.countEntities();

        Group expected = groupDAO.read(numberOfEntities - 1);
        String randomMadeUpName = "AA-11";
        expected.setName(randomMadeUpName);

        groupDAO.update(expected);

        Group actual = groupDAO.read(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    @Order(4)
    void testThatProcessCanDeleteGroup() throws DAOException {
        GroupDAO groupDAO = new GroupDAO();

        Group group = new Group("TestGroup");
        groupDAO.create(group);
        int numberOfGroupsBeforeDelete = groupDAO.countEntities();

        group = groupDAO.findByName(group.getName());

        groupDAO.delete(group.getId());

        int expected = numberOfGroupsBeforeDelete - 1;
        int actual = groupDAO.countEntities();
        assertEquals(expected, actual);
    }
}
