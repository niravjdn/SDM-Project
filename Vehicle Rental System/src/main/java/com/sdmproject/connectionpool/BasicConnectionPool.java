package com.sdmproject.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// this class is singleton

public class BasicConnectionPool implements ConnectionPool {

	static Logger logger = LoggerFactory.getLogger(BasicConnectionPool.class);
	
    private final String url;
    private final String user;
    private final String password;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 2;
    
    private final int MAX_POOL_SIZE = 5;
    private static List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
    private static BasicConnectionPool instance;
    
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    
    public static BasicConnectionPool create(String url, String user, String password) throws SQLException {
        
        
        if(instance == null) {
        	instance = new BasicConnectionPool(url, user, password, pool);

            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                pool.add(createConnection(url, user, password));
            }
        }
        return instance;
    }

    public static BasicConnectionPool  getInstance() {
    	return instance;
    }
    
    // to make it singleton
    private BasicConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() throws SQLException {
//    	System.err.println("Asking for new Connection");
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password));
            } else {
                throw new RuntimeException("Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
    	logger.info("Releasing");
    	
        connectionPool.add(connection);
//        System.out.println("connectionPool " + connectionPool.size());
//    	System.out.println("usedConnections " + usedConnections.size() + usedConnections.contains(connection));
//        
        return usedConnections.remove(connection);
    }
    

    private static Connection createConnection(String url, String user, String password) throws SQLException {
    	logger.debug("Creating new connection");
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    @Override
    public List<Connection> getConnectionPool() {
        return connectionPool;
    }
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }
    
    @PreDestroy
    public void cleanUp() {
    	
    	System.err.println("--------- Cleaning up Connections --------");
    	
    	connectionPool.forEach(connection -> {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    	usedConnections.forEach(connection -> {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }


	public static void closeAll() {
		getInstance().cleanUp();
	}
}
