package com.denghb.eorm.mysql.support.generate.model;

/**
 * @author denghb
 * @since 2019-05-27 23:59
 */
public class GenerateModel {

    private String database;

    private String packageName;

    private String targetDir;

    private boolean swagger2;

    private boolean lombok;

    private boolean override;

    private boolean writeDatabase;

    private boolean writeDDL;

    private boolean writeGenerateTime;

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

    public boolean isSwagger2() {
        return swagger2;
    }

    public void setSwagger2(boolean swagger2) {
        this.swagger2 = swagger2;
    }

    public boolean isLombok() {
        return lombok;
    }

    public void setLombok(boolean lombok) {
        this.lombok = lombok;
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }

    public boolean isWriteDatabase() {
        return writeDatabase;
    }

    public void setWriteDatabase(boolean writeDatabase) {
        this.writeDatabase = writeDatabase;
    }

    public boolean isWriteDDL() {
        return writeDDL;
    }

    public void setWriteDDL(boolean writeDDL) {
        this.writeDDL = writeDDL;
    }

    public boolean isWriteGenerateTime() {
        return writeGenerateTime;
    }

    public void setWriteGenerateTime(boolean writeGenerateTime) {
        this.writeGenerateTime = writeGenerateTime;
    }
}
