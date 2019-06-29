package com.denghb.eorm.mysql.support.generate.model;

/**
 * @Auther: denghb
 * @Date: 2019-05-27 23:59
 */
public class GenerateModel {

    private String database;

    private String packageName;

    private String targetDir;

    private Boolean swagger2;

    private Boolean lombok;

    private Boolean override;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public Boolean getSwagger2() {
        return swagger2;
    }

    public void setSwagger2(Boolean swagger2) {
        this.swagger2 = swagger2;
    }

    public Boolean getLombok() {
        return lombok;
    }

    public void setLombok(Boolean lombok) {
        this.lombok = lombok;
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }
}
