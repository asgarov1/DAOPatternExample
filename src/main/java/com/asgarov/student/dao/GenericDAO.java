package com.asgarov.student.dao;

import com.asgarov.student.exception.DAOException;

public interface GenericDAO<P, K> {
    boolean create(P object) throws DAOException;

    P read(K id) throws DAOException;

    void update(P object) throws DAOException;

    void delete(K id) throws DAOException;
}
