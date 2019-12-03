package com.asgarov.student.dao;

import java.sql.*;

import com.asgarov.student.domain.Group;
import com.asgarov.student.exception.DAOException;

public class GroupDAO extends AbstractDAO<Group, Integer> {

    @Override
    protected String getCreateQuery(Group group) {
        return "INSERT INTO groups (group_name) VALUES (?);";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM groups WHERE group_id = ?;";
    }

    protected String getSelectByNameQuery() {
        return "SELECT * FROM groups WHERE group_name = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE groups SET group_name = ? WHERE group_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM groups WHERE group_id = ?";
    }

    @Override
    protected String getCountRowsQuery() {
        return "SELECT count(*) FROM groups;";
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
    protected void setObjectStatement(PreparedStatement statement, Group group) throws DAOException {
        try {
            if (group.getId() != 0) {
                statement.setString(1, group.getName());
                statement.setInt(2, group.getId());
            } else {
                statement.setString(1, group.getName());
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    protected Group readObject(ResultSet resultSet) throws DAOException {
        Group group = new Group();
        try {
            group.setId(resultSet.getInt("group_id"));
            group.setName(resultSet.getString("group_name"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
        return group;
    }

    public Group findByName(String name) throws DAOException {
        Group group;
        String selectByNameQuery = getSelectByNameQuery();

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectByNameQuery)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                group = readObject(resultSet);
            } else {
                throw new DAOException("Couldn't find a group with such ID!");
            }

        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return group;
    }
}
