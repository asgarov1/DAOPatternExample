package com.asgarov.student.dao;

import java.sql.*;

import com.asgarov.student.exception.DAOException;

public abstract class AbstractDAO<P, K> implements GenericDAO<P, K> {
    private static final int UPDATE_EXECUTED_SUCCESSFULLY = 1;

    protected abstract String getCreateQuery(P object);

    protected abstract String getUpdateQuery();

    protected abstract String getSelectByIdQuery();

    protected abstract String getDeleteQuery();

    protected abstract void setIdStatement(PreparedStatement preparedStatement, K id) throws DAOException;

    protected abstract void setObjectStatement(PreparedStatement preparedStatement, P object) throws DAOException;

    protected abstract P readObject(ResultSet resultSet) throws DAOException;

    protected abstract String getCountRowsQuery();

    @Override
    public boolean create(P object) throws DAOException {
        String createQuery = getCreateQuery(object);

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(createQuery)) {

            setObjectStatement(statement, object);

            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new DAOException("Problem with creating the object!");
            }

        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public P read(K id) throws DAOException {
        P object;

        String selectByIdQuery = getSelectByIdQuery();
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectByIdQuery)) {

            setIdStatement(statement, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                object = readObject(resultSet);
            } else {
                throw new DAOException("Couldn't find an object with such ID!");
            }

        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return object;
    }

    @Override
    public void update(P object) throws DAOException {
        String updateQuery = getUpdateQuery();

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            setObjectStatement(statement, object);

            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new DAOException("Problem with updating the object!");
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(K id) throws DAOException {
        String deleteQuery = getDeleteQuery();

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            setIdStatement(statement, id);
            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new DAOException("Problem with deleting the object!");
            }

        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    public int countEntities() throws DAOException {
        String countRowsQuery = getCountRowsQuery();
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(countRowsQuery)) {

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DAOException("Problem counting entities!");
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }
}
