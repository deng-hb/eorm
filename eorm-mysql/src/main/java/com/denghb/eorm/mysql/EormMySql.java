package com.denghb.eorm.mysql;

import com.denghb.eorm.mysql.domain.Paging;
import com.denghb.eorm.mysql.domain.PagingResult;
import com.denghb.eorm.Eorm;

import java.util.List;

public interface EormMySql extends Eorm {

    public <T> int batchInsert(List<T> list);

    public <T> PagingResult<T> list(Class<T> clazz, StringBuffer sql, Paging paging);
}
