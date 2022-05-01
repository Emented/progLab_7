package emented.lab7.server.interfaces;

import emented.lab7.common.exceptions.DatabaseException;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException, DatabaseException;
}
