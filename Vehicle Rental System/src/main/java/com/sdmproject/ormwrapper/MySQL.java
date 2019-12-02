package com.sdmproject.ormwrapper;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.sdmproject.configuration.DBConfig;
import com.sdmproject.connectionpool.BasicConnectionPool;
import com.sdmproject.connectionpool.ConnectionPool;

@Configuration
@EnableWebSecurity
public class MySQL extends BaseSQL {

    private Connection c = null;
    private DBConfig data = new DBConfig();
	@Autowired
    private DataSource dataSource;

    public ConnectionPool getPool() {
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
    	
    	ConnectionPool connectionPool = null;
		try {
			jdbc:mysql://localhost:3306/zadmin_sdm2
				System.out.println(DBConfig.HOST+"://"+":"+DBConfig.PORT+"/"+DBConfig.DBNAME);
			connectionPool = BasicConnectionPool.create(DBConfig.HOST+":"+DBConfig.PORT+"/"+DBConfig.DBNAME, DBConfig.USERNAME, DBConfig.PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return connectionPool;
    }

	@Override
	public void close(ConnectionPool connectionPool, Connection connection) {
		if(connection != null)
			connectionPool.releaseConnection(connection);
//		System.out.println("Closing in mysql");
	}
}

