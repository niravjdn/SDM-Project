package com.sdmproject.ormwrapper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.sdmproject.connectionpool.ConnectionPool;
import com.sdmproject.orm.SQL;

public abstract class BaseSQL implements SQL {

    private Map<ResultSet,Statement> statementMap = new HashMap<>();

    public abstract ConnectionPool getPool();

    public void write(String queryString,Object... parameters){
        try(Connection conn = getPool().getConnection();
        		PreparedStatement ps = setParams(conn.prepareStatement(queryString),parameters);) {
            System.out.println(ps.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {e.printStackTrace();}
    }

    public ResultSet read(String queryString, Connection connection, Object... parameters){
        try {
            PreparedStatement ps = setParams(connection.prepareStatement(queryString),parameters);
            System.out.println(ps.toString());
            ResultSet rs = ps.executeQuery();
            statementMap.put(rs,ps);
            return rs;
        } catch (Exception e) {e.printStackTrace(); System.out.println("E");}
        return null;
    }

    private PreparedStatement setParams(PreparedStatement st, Object... parameters) throws SQLException {
        int i = 1;
        for(Object object : parameters){
            Class type = object.getClass();
            if(type.equals(String.class))
                st.setString(i,(String)object);
            else if(type.equals(int.class)||type.equals(Integer.class))
                st.setInt(i,(int)object);
            else if(type.equals(double.class)||type.equals(Double.class))
                st.setDouble(i,(double)object);
            else if(type.equals(long.class)||type.equals(Long.class))
                st.setLong(i,(long)object);
            else if(type.equals(short.class)||type.equals(Short.class))
                st.setShort(i,(short)object);
            else if(type.equals(float.class)||type.equals(Float.class))
                st.setFloat(i,(float)object);
            else if(type.equals(Timestamp.class))
                st.setTimestamp(i,(Timestamp) object);
            else if(type.equals(Date.class))
                st.setDate(i,(Date)object);
            else if(type.equals(Time.class))
                st.setTime(i,(Time)object);
            else
                System.out.println("[SQL] Could not set type: "+object.getClass().getName());
            i++;
        }
        return st;
    }

    public void close(ResultSet rs){
        if(statementMap.containsKey(rs)){
            try {
                statementMap.get(rs).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statementMap.remove(rs);
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp(){
        for(ResultSet rs : statementMap.keySet())
            close(rs);
    }

}
