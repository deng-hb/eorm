package com.denghb.eorm;

import java.util.List;

public interface Eorm {

    public int execute(String sql, Object... args);

    public <T> List<T> select(Class<T> clazz, String sql, Object... args);

    public <T> int insert(T domain);

    public <T> int update(T domain);

    public <T> int delete(T domain);

    public <T> int delete(Class<T> clazz, Object... ids);

    public <T> T selectOne(Class<T> clazz, String sql, Object... args);
}
