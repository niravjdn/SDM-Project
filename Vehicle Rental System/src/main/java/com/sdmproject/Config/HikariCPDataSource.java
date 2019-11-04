package com.sdmproject.Config;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
     
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    static String url;
    @Value("${spring.datasource.url}")
    public void setDatabase(String url) {
        this.url = url;
        System.out.println(url + "000000");
    }
    
    static {
    	System.err.println(url+"000000000000000000000");
        config.setJdbcUrl("jdbc:h2:mem:test");
        config.setUsername("sa");
        config.setPassword("");
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