package com.sdmproject.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sdmproject.connectionpool.ConnectionPool;

public interface SQL {

	ConnectionPool getPool();
    ResultSet read(String queryString,Connection connection, Object... parameters);
    void write(String queryString,Object... parameters) throws SQLException;
    void close(ResultSet resultSet);
	void close(ConnectionPool connectionPool, Connection connection);

}
