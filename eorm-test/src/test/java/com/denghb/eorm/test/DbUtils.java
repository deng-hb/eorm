package com.denghb.eorm.test;



import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtils {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "";

    static Connection connection = null;

    static {

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection(){

        return connection;
    }
}
