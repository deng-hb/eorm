package com.denghb.eorm.test;

import com.denghb.eorm.Eorm;
import com.denghb.eorm.domain.User;
import com.denghb.eorm.mysql.EormMySql;
import com.denghb.eorm.mysql.impl.EormMySqlImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EormMySqlTest {

    private EormMySql eorm;

    @Before
    public void setUp() {
        eorm = new EormMySqlImpl(DbUtils.getConnection());
    }

    @Test
    public void insert() {

        User user = new User();
        user.setAge(1);
        user.setName("cesi-mysql");
        user.setEmail("123@123.com");
        int res = eorm.insert(user);

        Assert.assertEquals(1, res);
        System.out.println(user);
    }

    @Test
    public void page() {

        java.lang.Integer i = 1;
        if (i instanceof java.lang.Number) {

        }
    }

    @Test
    public void batchInsert() {

        List<User> list = new ArrayList<User>();
        User user = null;
        for (int i = 0; i < 10; i++) {
            user = new User();
            user.setEmail("test" + (i + 1) + "@xx.com");
            user.setMobile("12312321" + i);
            user.setAge(23);
            user.setName("cesi" + i);
            list.add(user);
        }

        eorm.batchInsert(list);
    }

}
