package com.sdmproject.Config;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
     
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    
    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/zadmin_sdm2");
        config.setUsername("root");
        config.setMaximumPoolSize(20);
        config.setPassword("");
        config.setConnectionTimeout(3000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }
     
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
     
    private HikariCPDataSource(){}
}