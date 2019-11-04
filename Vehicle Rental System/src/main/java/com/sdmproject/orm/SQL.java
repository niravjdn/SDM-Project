package com.sdmproject.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQL {

    Connection getConnection() throws SQLException;
    ResultSet read(String queryString,Object... parameters);
    void write(String queryString,Object... parameters);
    void close(ResultSet resultSet);

}
