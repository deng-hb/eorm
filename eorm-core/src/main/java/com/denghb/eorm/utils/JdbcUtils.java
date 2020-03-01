package com.denghb.eorm.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class JdbcUtils {


    static Set<Class> SINGLE_CLASSES = new HashSet<Class>();

    static {

        SINGLE_CLASSES.add(Integer.class);
        SINGLE_CLASSES.add(String.class);
        SINGLE_CLASSES.add(Float.class);
        SINGLE_CLASSES.add(Long.class);
        SINGLE_CLASSES.add(Double.class);
        SINGLE_CLASSES.add(Number.class);
        SINGLE_CLASSES.add(Boolean.class);

        SINGLE_CLASSES.add(java.math.BigDecimal.class);
        SINGLE_CLASSES.add(java.math.BigInteger.class);

        SINGLE_CLASSES.add(java.util.Date.class);

        SINGLE_CLASSES.add(java.sql.Date.class);
        SINGLE_CLASSES.add(java.sql.Time.class);
        SINGLE_CLASSES.add(java.sql.Timestamp.class);
    }

    /**
     * 判断简单对象
     */
    public static boolean isSingleClass(Class clazz) {
        return SINGLE_CLASSES.contains(clazz);
    }

    /**
     * 关闭
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 关闭
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 关闭
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 驼峰格式转换为下划线格式
     */
    public static String humpToUnderline(String name) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (i > 0 && Character.isUpperCase(ch)) {// 首字母是大写不需要添加下划线
                builder.append('_');
            }
            builder.append(ch);
        }

        int startIndex = 0;
        if (builder.charAt(0) == '_') {//如果以下划线开头则忽略第一个下划线
            startIndex = 1;
        }
        return builder.substring(startIndex).toLowerCase();
    }


    /**
     * 下划线格式转换为驼峰格式
     */
    public static String underlineToHump(String name, boolean firstCharToUpper) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (i == 0 && firstCharToUpper) {
                builder.append(Character.toUpperCase(ch));
            } else {
                if (i > 0 && ch == '_') {// 首字母是大写不需要添加下划线
                    i++;
                    ch = name.charAt(i);
                    builder.append(Character.toUpperCase(ch));
                } else {
                    builder.append(ch);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取数据库名
     *
     * @param jdbc
     * @return
     */
    public static String getDatabase(String jdbc) {
        if (null == jdbc) {
            return null;
        }
        int start = jdbc.lastIndexOf("/");
        int end = jdbc.indexOf("?");
        if (-1 == start || -1 == end) {
            return null;
        }
        return jdbc.substring(start + 1, end);
    }

}
