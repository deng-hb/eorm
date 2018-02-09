package com.denghb.eorm.test;

import com.denghb.eorm.Eorm;
import com.denghb.eorm.domain.User;
import com.denghb.eorm.impl.EormImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class EormTest {

    private Eorm eorm;

    @Before
    public void setUp() {
        eorm = new EormImpl(DbUtils.getConnection());
    }

    @Test
    public void executeInsert() {

        String sql = "insert into user (age,name,email,mobile) values (?,?,?,?)";
        int res = eorm.execute(sql, 28, "zhangsan", "zhangsan@denghb.com", "15618525920");

        System.out.println(res);
    }

    @Test
    public void select() {

        String sql = "select * from user limit 0,10";
        List<User> list = eorm.select(User.class, sql);

        System.out.println(list);
    }

    @Test
    public void selectSingle() {

        String sql = "select id from user limit 0,10";
        List<Integer> list = eorm.select(Integer.class, sql);

        System.out.println(list);
    }

    @Test
    public void selectMap() {

        String sql = "select id,name,age from user limit 0,10";
        List<Map> list = eorm.select(Map.class, sql);

        System.out.println(list);
    }

    @Test
    public void selectOne() {

        String sql = "select * from user where id = 1";
        User user = eorm.selectOne(User.class, sql);
        System.out.println(user);
    }

    @Test
    public void insert() {

        User user = new User();
        user.setAge(1);
        user.setName("cs-eorm-core");
        user.setEmail("123@123.com");
        int res = eorm.insert(user);

        Assert.assertEquals(1, res);
        System.out.println(user);
    }

    @Test
    public void update() {

        User user = new User();
        user.setAge(1);
        user.setId(12);
        user.setEmail("121223@123.com");
        int res = eorm.update(user);

        Assert.assertEquals(1, res);
    }

    @Test
    public void delete() {

        User user = new User();
        user.setAge(1);
        user.setId(12);
        user.setEmail("121223@123.com");
        int res = eorm.delete(user);

        Assert.assertEquals(1, res);
    }

    @Test
    public void delete2() {

        int res = eorm.delete(User.class, 13);

        Assert.assertEquals(1, res);
    }
}
