package com.denghb.eorm.mysql.support.generate;

import com.denghb.eorm.Eorm;
import com.denghb.eorm.impl.EormImpl;
import com.denghb.eorm.mysql.support.generate.model.DatabaseModel;
import com.denghb.eorm.mysql.support.generate.model.TableModel;
import com.denghb.eorm.mysql.support.generate.utils.ColumnUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ppd on 2017/2/20.
 */
public class Generate {

    public static void create(Connection conn, String database, String packageName, DatabaseModel model, String targetDir) throws GenerateException {
        if (null == conn) {
            throw new GenerateException("Connection is null");
        }

        // 类名
        String domainName = ColumnUtils.removeAll_AndNextCharToUpperCase(model.getTableName());
        domainName = ColumnUtils.firstCharToUpperCase(domainName);
        // 查询对应数据库对应表的字段信息

        String sql = "SELECT column_Name,data_Type,column_Key,column_Comment FROM information_schema.COLUMNS WHERE table_schema = ? AND table_name = ? ";

        Eorm eorm = new EormImpl(conn);
        List<TableModel> list = eorm.select(TableModel.class, sql, database, model.getTableName());

        try {
            // 查询DDL 只能拼接
            String ddlSql = "show create table `" + database + "`.`" + model.getTableName() + "`";
            Map map = eorm.selectOne(Map.class, ddlSql);

            String ddl = (String) map.get("Create Table");

            Configuration config = new Configuration(Configuration.VERSION_2_3_0);
            config.setClassForTemplateLoading(ClassLoader.class, "/templates/");
            Template template = config.getTemplate("domain.ftl", "UTF-8");
            // 创建数据模型
            Map<String, Object> root = new HashMap<String, Object>();

            root.put("list", list);
            root.put("tableComment", model.getTableComment());
            root.put("domainName", domainName);

            root.put("tableName", model.getTableName());

            root.put("databaseName", database);
            root.put("tableDdl", ddl);

            root.put("generateTime", new Date().toString());
            root.put("packageName", packageName);

            // TODO 路径
            File file = new File(targetDir + "/" + domainName + ".java");

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            template.process(root, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenerateException("Generate Error");
        }
    }

}
