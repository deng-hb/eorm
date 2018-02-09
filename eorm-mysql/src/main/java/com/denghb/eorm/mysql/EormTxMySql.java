package com.denghb.eorm.mysql;

public interface EormTxMySql {

    public interface Handler {

        public void doTx(EormMySql eorm);
    }

    public void doTx(Handler handler);
}
