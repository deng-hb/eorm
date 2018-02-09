package com.denghb.eorm.mysql.impl;

import com.denghb.eorm.mysql.EormTxMySql;

import java.sql.Connection;

public class EormTxMySqlImpl implements EormTxMySql {


    private Connection connection;

    public EormTxMySqlImpl() {

    }

    public EormTxMySqlImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void doTx(Handler handler) {

        try {
            connection.setAutoCommit(false);
            handler.doTx(new EormMySqlImpl(connection));
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }
}
