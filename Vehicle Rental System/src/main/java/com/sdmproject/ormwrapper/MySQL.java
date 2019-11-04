package com.sdmproject.ormwrapper;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.sdmproject.Config.HikariCPDataSource;
import com.sdmproject.connectionpool.BasicConnectionPool;
import com.sdmproject.connectionpool.ConnectionPool;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebSecurity
public class MySQL extends BaseSQL {

    private Connection c = null;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    @Autowired
    private DataSource dataSource;

    
    public MySQL(String ho, int po, String db, String un, String pw) {
        host=ho;
        port=po;
        database=db;
        username=un;
        password=pw;
    }
    
    public MySQL() {
	// TODO Auto-generated constructor stub
    	host = "127.0.0.1";
    	port = 3306;
    	database = "mydb";
    	username = "root";
    	password = "";
    }
    
    public Connection getConnection() throws SQLException{
//        try {
//            if(c==null||c.isClosed()){
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");
//                    c = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?user=" + this.username + "&password=" + this.password + "&autoReconnect=" + true + "&failOverReadOnly=false&maxReconnects=" + 5 + "&UseUnicode=yes&characterEncoding=UTF-8");
//                } catch (SQLException e) {
//                    System.out.println("Error: bei getConnection()[MySQL.java]  SQLException   " + e.getMessage());
//                } catch (ClassNotFoundException e) {
//                    System.out.println("Error: bei getConnection()[MySQL.java]  ClassNotFoundException");
//                }
//            }
//        } catch (SQLException e) {e.printStackTrace();}
//        try {
//            if(c!=null&&c.isClosed())
//                return null;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return c;
    	
    	System.out.println(dataSource + " 00000000 ");
//    	ConnectionPool connectionPool = BasicConnectionPool
//    		      .create("jdbc:mysql://localhost:3306/zadmin_sdm2", "root", "");
    	
    	return HikariCPDataSource.getConnection();
    }



}

