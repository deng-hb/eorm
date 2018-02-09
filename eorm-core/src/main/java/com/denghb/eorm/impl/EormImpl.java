package com.denghb.eorm.impl;

import com.denghb.eorm.Eorm;
import com.denghb.eorm.annotation.Ecolumn;
import com.denghb.eorm.utils.EormUtils;
import com.denghb.eorm.utils.JdbcUtils;
import com.denghb.eorm.utils.ReflectUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EormImpl implements Eorm {

    private Connection connection;

    public EormImpl() {

    }

    public EormImpl(Connection connection) {
        this.connection = connection;
    }

    public EormImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public int execute(String sql, Object... args) {
        int res = 0;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);

            int i = 1;
            for (Object object : args) {
                ps.setObject(i, object);
                i++;
            }
            res = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(ps);
        }
        return res;
    }

    @Override
    public <T> List<T> select(Class<T> clazz, String sql, Object... args) {
        List<T> list = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            int i = 1;
            for (Object object : args) {
                ps.setObject(i, object);
                i++;
            }

            rs = ps.executeQuery();
            ResultSetMetaData data = rs.getMetaData();

            // map
            if (clazz.equals(Map.class)) {
                list = (List<T>) buildMap(rs, data);
            } else if (JdbcUtils.isSingleClass(clazz)) {
                // 基本类型
                list = new ArrayList<T>();
                while (rs.next()) {
                    String columnName = data.getColumnName(1);
                    Object value = rs.getObject(columnName);

                    if (value instanceof java.lang.Number) {
                        value = clazz.getConstructor(String.class).newInstance(String.valueOf(value));
                    }
//                    clazz.is
                    list.add((T) value);
                }

            } else {
                list = new ArrayList<T>();

                // 所有的字段
                List<Field> fields = ReflectUtils.getFields(clazz);

                int columnCount = data.getColumnCount();
                while (rs.next()) {
                    Object object = ReflectUtils.createInstance(clazz);

                    list.add((T) object);
                    for (int j = 1; j <= columnCount; j++) {

                        String columnName = data.getColumnName(j);
                        Object value = rs.getObject(columnName);

                        // 找到对应字段
                        for (Field field : fields) {
                            // 比较表字段名
                            Ecolumn ecolumn = field.getAnnotation(Ecolumn.class);
                            if (null != ecolumn) {
                                if (ecolumn.name().equalsIgnoreCase(columnName)) {
                                    ReflectUtils.setFieldValue(field, object, value);
                                }
                            }
                            // 比较字段名
                            columnName = columnName.replace("_", "");
                            if (field.getName().equalsIgnoreCase(columnName)) {
                                ReflectUtils.setFieldValue(field, object, value);
                            }
                        }

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(ps);
            JdbcUtils.close(rs);
        }
        return list;
    }

    @Override
    public <T> int insert(T domain) {

        EormUtils.TableInfo table = EormUtils.getTableInfo(domain);

        List<Object> params = new ArrayList<Object>();

        StringBuilder csb = new StringBuilder();
        StringBuilder vsb = new StringBuilder();

        List<EormUtils.Column> columns = table.getAllColumns();
        for (int i = 0; i < columns.size(); i++) {

            EormUtils.Column column = columns.get(i);

            if (i > 0) {
                csb.append(", ");
                vsb.append(", ");
            }
            csb.append('`');
            csb.append(column.getName());
            csb.append('`');

            vsb.append("?");

            params.add(column.getValue());
        }

        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(table.getTableName());
        sb.append(" (");
        sb.append(csb);
        sb.append(") values (");
        sb.append(vsb);
        sb.append(")");

        String sql = sb.toString();
        int res = this.execute(sql, params.toArray());
        return res;
    }

    @Override
    public <T> int update(T domain) {

        EormUtils.TableInfo table = EormUtils.getTableInfo(domain);
        List<Object> params = new ArrayList<Object>();

        List<EormUtils.Column> commonColumns = table.getCommonColumns();
        StringBuilder ssb = new StringBuilder();
        for (int i = 0; i < commonColumns.size(); i++) {
            EormUtils.Column column = commonColumns.get(i);

            if (i > 0) {
                ssb.append(", ");
            }

            ssb.append("`");
            ssb.append(column.getName());
            ssb.append("` = ?");

            params.add(column.getValue());
        }

        List<EormUtils.Column> primaryKeyColumns = table.getPrimaryKeyColumns();
        StringBuilder wsb = new StringBuilder();
        for (int i = 0; i < primaryKeyColumns.size(); i++) {
            EormUtils.Column column = primaryKeyColumns.get(i);
            if (i > 0) {
                wsb.append(" and ");
            }
            wsb.append("`");
            wsb.append(column.getName());
            wsb.append("` = ?");

            params.add(column.getValue());
        }

        StringBuilder sb = new StringBuilder("update ");
        sb.append(table.getTableName());
        sb.append(" set ");
        sb.append(ssb);
        sb.append(" where ");
        sb.append(wsb);

        String sql = sb.toString();
        int res = this.execute(sql, params.toArray());
        return res;
    }

    @Override
    public <T> int delete(T domain) {
        EormUtils.TableInfo table = EormUtils.getTableInfo(domain);
        List<Object> params = new ArrayList<Object>();

        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(table.getTableName());
        sb.append(" where ");

        List<EormUtils.Column> primaryKeyColumns = table.getPrimaryKeyColumns();
        for (int i = 0; i < primaryKeyColumns.size(); i++) {
            EormUtils.Column column = primaryKeyColumns.get(i);
            if (i > 0) {
                sb.append(" and ");
            }
            sb.append("`");
            sb.append(column.getName());
            sb.append("` = ?");

            params.add(column.getValue());
        }

        String sql = sb.toString();
        int res = this.execute(sql, params.toArray());
        return res;
    }

    @Override
    public <T> int delete(Class<T> clazz, Object... ids) {

        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(EormUtils.getTableName(clazz));
        sb.append(" where ");

        List<String> primaryKeyNames = EormUtils.getPrimaryKeyNames(clazz);
        for (int i = 0; i < primaryKeyNames.size(); i++) {
            if (i > 0) {
                sb.append(" and ");
            }
            sb.append("`");
            sb.append(primaryKeyNames.get(i));
            sb.append("` = ?");
        }

        String sql = sb.toString();
        int res = this.execute(sql, ids);
        return res;
    }

    @Override
    public <T> T selectOne(Class<T> clazz, String sql, Object... args) {

        List<T> list = select(clazz, sql, args);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private List<Map> buildMap(ResultSet rs, ResultSetMetaData data) throws SQLException {

        List<Map> list = new ArrayList<Map>();

        int columnCount = data.getColumnCount();

        while (rs.next()) {

            Map map = new HashMap();
            for (int j = 1; j <= columnCount; j++) {
                String columnName = data.getColumnName(j);
                map.put(columnName, rs.getObject(columnName));
            }

            list.add(map);
        }
        return list;
    }
}
