package com.denghb.eorm.mysql.impl;

import com.denghb.eorm.impl.EormImpl;
import com.denghb.eorm.mysql.EormMySql;
import com.denghb.eorm.mysql.domain.Paging;
import com.denghb.eorm.mysql.domain.PagingResult;
import com.denghb.eorm.utils.EormUtils;
import com.denghb.eorm.utils.ReflectUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EormMySqlImpl extends EormImpl implements EormMySql {

    public EormMySqlImpl() {

    }

    public EormMySqlImpl(Connection connection) {
        super(connection);
    }

    public EormMySqlImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public <T> int insert(T domain) {


        int res = super.insert(domain);

        if (1 == res) {
            // 获取自动生成的ID并填充
            EormUtils.TableInfo table = EormUtils.getTableInfo(domain);
            List<Field> fields = table.getAllPrimaryKeyFields();
            if (fields.size() == 1) {// 只适合单个主键
                Field field = fields.get(0);
                Object object = ReflectUtils.getFieldValue(field, domain);
                if (null == object) {
                    Object value = selectOne(field.getType(), "select LAST_INSERT_ID() as id");
                    ReflectUtils.setFieldValue(field, domain, value);
                }
            }
        }

        return res;
    }

    @Override
    public <T> int batchInsert(List<T> list) {
        if (null == list || list.isEmpty()) {
            return 0;
        }

        // 取第一个样本
        T type = list.get(0);
        EormUtils.TableInfo table = EormUtils.getTableInfo(type);

        StringBuilder csb = new StringBuilder();
        List<EormUtils.Column> allColumns = table.getAllColumns();
        int columnSize = allColumns.size();
        for (int i = 0; i < columnSize; i++) {
            EormUtils.Column column = allColumns.get(i);
            if (i > 0) {
                csb.append(", ");
            }
            csb.append('`');
            csb.append(column.getName());
            csb.append('`');

        }

        List<Object> params = new ArrayList<Object>();

        StringBuilder vsb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                vsb.append(", ");
            }

            vsb.append("(");

            table = EormUtils.getTableInfo(list.get(i));

            if (columnSize != table.getAllColumns().size()) {
                throw new RuntimeException("column size difference ...");
            }

            for (int j = 0; j < columnSize; j++) {
                if (j > 0) {
                    vsb.append(", ");
                }
                vsb.append("?");

                params.add(table.getAllColumns().get(j).getValue());
            }

            vsb.append(")");

        }

        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(table.getTableName());
        sb.append(" (");
        sb.append(csb);

        sb.append(") values ");
        sb.append(vsb);

        String sql = sb.toString();
        int res = this.execute(sql, params.toArray());
        return res;
    }

    @Override
    public <T> PagingResult<T> list(Class<T> clazz, StringBuffer sql, Paging paging) {
        PagingResult<T> result = new PagingResult<T>(paging);

        Object[] objects = paging.getParams().toArray();
        // 不分页 start
        long rows = paging.getRows();
        if (0 != rows) {
            // 先查总数
            String totalSql = "select count(*) ";

            String tempSql = sql.toString().toLowerCase();
            totalSql += sql.substring(tempSql.indexOf("from"), sql.length());

            // fix group by
            if (0 < totalSql.indexOf(" group ")) {
                totalSql = "select count(*) from (" + totalSql + ") temp";
            }

            long total = this.selectOne(Long.class, totalSql, objects);

            paging.setTotal(total);
            if (0 == total) {
                return result;
            }
        }
        // 不分页 end

        // 排序
        if (paging.isSort()) {
            // 判断是否有排序字段
            String[] sorts = paging.getSorts();
            if (null != sorts && 0 < sorts.length) {
                int sortIndex = paging.getSortIndex();

                // 大于排序的长度默认最后一个
                if (sortIndex >= sorts.length) {
                    sortIndex = sorts.length - 1;
                }
                sql.append(" order by ");

                sql.append('`');
                sql.append(sorts[sortIndex]);
                sql.append('`');

                // 排序方式
                if (paging.isDesc()) {
                    sql.append(" desc");
                } else {
                    sql.append(" asc");
                }
            }
        }

        if (0 != rows) {
            // 分页
            sql.append(" limit ");
            sql.append(paging.getStart());
            sql.append(",");
            sql.append(rows);
        }

        List<T> list = select(clazz, sql.toString(), objects);
        result.setList(list);

        return result;
    }
}
