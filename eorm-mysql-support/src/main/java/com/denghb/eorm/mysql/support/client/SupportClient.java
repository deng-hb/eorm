package com.denghb.eorm.mysql.support.client;

import com.denghb.eorm.Eorm;
import com.denghb.eorm.impl.EormImpl;
import com.denghb.eorm.mysql.support.client.table.DatabaseTableModel;
import com.denghb.eorm.mysql.support.client.table.TableView;
import com.denghb.eorm.mysql.support.generate.Generate;
import com.denghb.eorm.mysql.support.generate.GenerateException;
import com.denghb.eorm.mysql.support.generate.model.ConnectionModel;
import com.denghb.eorm.mysql.support.generate.model.DatabaseModel;
import com.denghb.eorm.mysql.support.generate.model.GenerateModel;
import com.denghb.eorm.mysql.support.generate.utils.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * TODO 可选覆盖文件？新完善的注解
 * Created by denghb on 2017/2/19.
 */
public class SupportClient extends JFrame {

    JPanel panel = new JPanel();

    List<DatabaseModel> result = new ArrayList<DatabaseModel>();
    Vector<DatabaseModel> data = new Vector<DatabaseModel>();
    DatabaseTableModel model = new DatabaseTableModel(data);
    TableView table = new TableView(model);

    JPanel loadPanel;

    public SupportClient() {
        this.setTitle("Generate Entity from MySQL!!!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭按钮的动作为退出窗口
        this.setSize(800, 600);// 大小
        this.setResizable(false);// 禁止缩放
        this.setLocationRelativeTo(null);// 屏幕居中显示，需要先设置frame大小

        panel.setLayout(null);
        panel.setBounds(0, 0, 800, 600);
        this.add(panel);


        loadPanel = new JPanel();
        loadPanel.setLayout(null);
        loadPanel.setEnabled(false);
//        loadPanel.setBackground(Color.lightGray);
//        loadPanel.setOpaque(true);
        loadPanel.setBounds(0, 0, this.getWidth(), this.getHeight());

        java.net.URL imageURL = getClass().getClassLoader().getResource("images/loading.gif");
        ImageIcon imageIcon = new ImageIcon(imageURL);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(imageIcon);
        imageIcon.setImageObserver(iconLabel);
        iconLabel.setBounds((loadPanel.getWidth() - imageIcon.getIconWidth()) / 2, (loadPanel.getHeight() - imageIcon.getIconHeight()) / 2, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        loadPanel.add(iconLabel);
        loadPanel.setVisible(false);
        panel.add(loadPanel);

        // 操作面板
        ControlPanel controlPanel = new ControlPanel();
        controlPanel.setBounds(0, 0, this.getWidth(), 280);
        controlPanel.setConnectionHandler(new ControlPanel.ControlConnectionHandler() {
            @Override
            public boolean execute(ConnectionModel model) {

                try {
                    DbUtils.init(model);
                } catch (GenerateException e) {
                    e.printStackTrace();
                    return false;
                }

                Eorm eorm = new EormImpl(DbUtils.getConnection());
                String sql = "select table_name,table_comment from information_schema.tables where table_schema=? ";

                result = eorm.select(DatabaseModel.class, sql, model.getDatabase());

                data.removeAllElements();
                for (DatabaseModel dbInfo : result) {
                    data.add(dbInfo);
                }
                table.reloadData();

                return true;
            }
        });
        controlPanel.setSearchHandler(new ControlPanel.ControlSearchHandler() {
            @Override
            public void search(String string) {

                data.removeAllElements();
                for (DatabaseModel model : result) {
                    if (model.getTableName().contains(string)) {
                        data.add(model);
                    }
                }

                table.reloadData();
            }
        });
        controlPanel.setSelectAllHandler(new ControlPanel.SelectAllHandler() {
            @Override
            public void execute(boolean select) {

                for (DatabaseModel model : data) {
                    model.setChecked(select);
                }

                table.reloadData();
            }
        });
        controlPanel.setExecuteHandler(new ControlPanel.ControlExecuteHandler() {
            @Override
            public void execute(final GenerateModel generateModel) {

                if (data.isEmpty()) {
                    System.out.println("data is null");
                    return;
                }


                loadPanel.setVisible(true);
                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {

                        for (DatabaseModel model : data) {

                            try {
                                if (model.isChecked()) {
                                    Generate.create(DbUtils.getConnection(), model, generateModel);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }

                        }
                        return null;
                    }

                    protected void done() {
                        System.out.println("execute ok");
                        loadPanel.setVisible(false);
                    }
                }.execute();

            }
        });
        panel.add(controlPanel);
        // 表格
        table.setBounds(30, 280, 740, 280);
        panel.add(table);

        this.setVisible(true);// 显示
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    new SupportClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean isEmpty(String text) {
        return null == text || 0 == text.trim().length();
    }

    /**
     * 获取控件最右端
     *
     * @param component
     * @return
     */
    private static int getRight(JComponent component) {
        return component.getX() + component.getWidth();
    }

    /**
     * 获取控件最下端
     *
     * @param component
     * @return
     */
    private static int getBottom(JComponent component) {
        return component.getY() + component.getHeight();
    }
}
