package edu.hei.school.restaurant.dao;

import java.sql.*;

public class PostgresNextValId {

    public Long nextID(String tableName, Connection connection) throws SQLException {
        final String columnName = "id";

        String sequenceQuery = "select pg_get_serial_sequence('" + tableName + "', '" + columnName + "')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sequenceQuery)) {

            try (PreparedStatement prepareStatement = connection.prepareStatement(sequenceQuery);
                 ResultSet resultSet = prepareStatement.executeQuery()) {

                String sequenceName = resultSet.getString(1);
                String nextvalQuery = "select nextval('" + sequenceName + "')";

                try (PreparedStatement nextValStmt = connection.prepareStatement(nextvalQuery);
                     ResultSet nextValRs = nextValStmt.executeQuery()) {
                    if (nextValRs.next()) {
                        return nextValRs.getLong(1);
                    }
                }
            }
        }
        throw new SQLException("Unable to find sequence for table " + tableName);
    }
}
