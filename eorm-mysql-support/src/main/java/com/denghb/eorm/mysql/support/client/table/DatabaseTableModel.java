package com.denghb.eorm.mysql.support.client.table;

import com.denghb.eorm.mysql.support.generate.model.DatabaseModel;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class DatabaseTableModel extends AbstractTableModel {

    String[] n = {"Table", "Comment", "Check"};

    private Vector<DatabaseModel> data;

    public DatabaseTableModel(Vector<DatabaseModel> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return n.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DatabaseModel model = data.get(rowIndex);
        if (0 == columnIndex) {
            return model.getTableName();
        } else if (1 == columnIndex) {
            return model.getTableComment();
        } else if (2 == columnIndex) {
            return model.isChecked();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return n[column];
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;// 复选框可编辑
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        System.out.println("setValueAt" + aValue);
        if (columnIndex == 2) {
            data.get(rowIndex).setChecked((Boolean) aValue);
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
