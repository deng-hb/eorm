package com.denghb.eorm.test;

import com.denghb.eorm.domain.User;
import com.denghb.eorm.mysql.EormMySql;
import com.denghb.eorm.mysql.EormTxMySql;
import com.denghb.eorm.mysql.impl.EormTxMySqlImpl;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class EormTxMySqlTest {

    EormTxMySql eormtx;

    @Before
    public void setUp() {
        eormtx = new EormTxMySqlImpl(DbUtils.getConnection());
    }

    @Test
    public void testTx() {
        eormtx.doTx(new EormTxMySql.Handler() {
            @Override
            public void doTx(EormMySql eorm) {


                User user = new User();
                user.setAge(1);
                user.setName("cs-eormtx");
                user.setEmail("123@123.com");
                eorm.insert(user);

//                user.setId(user.getId() + 1);
                eorm.insert(user);
            }
        });

    }


    @Test
    public void proxy() {
        //创建被代理的对象(接口类型)
        final EormTxMySql target = new EormTxMySqlImpl();
        //通过代理类Prxoy的newProxyInstance()方法创建代理对象
        EormTxMySql proxy = (EormTxMySql) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        System.out.println("方法【" + method.getName() + "】开始执行, 参数为" + Arrays.asList(args) + "...");
                        long start = System.currentTimeMillis();
                        Object result = method.invoke(target, args);
                        long end = System.currentTimeMillis();
                        System.out.println("方法【" + method.getName() + "】执行完成,运算结果为:" + result + ", 用时" + (end - start) + "毫秒!");
                        return result;
                    }
                });


        proxy.doTx(new EormTxMySql.Handler() {
            @Override
            public void doTx(EormMySql eorm) {


                User user = new User();
                user.setAge(1);
                user.setName("cs-eormtx");
                user.setEmail("123@123.com");
                eorm.insert(user);

//                user.setId(user.getId() + 1);
                eorm.insert(user);
            }
        });
    }

}
