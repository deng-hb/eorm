package com.denghb.eorm.mysql.support.client;


import com.denghb.eorm.mysql.support.generate.model.ConnectionModel;
import com.denghb.eorm.mysql.support.generate.model.GenerateModel;
import com.denghb.eorm.mysql.support.utils.ConfigUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * host      username   password
 * database  port       connection
 */
public class ControlPanel extends Panel implements ActionListener {

    private TextField _hostField;
    private TextField _portField;
    private TextField _databaseField;

    private TextField _usernameField;
    private JPasswordField _passwordField;
    private TextField _packageField;

    private JCheckBox _selectAll;
    private JCheckBox _override;
    private JCheckBox _enableSwagger2;
    private JCheckBox _enableLombok;

    public interface ControlConnectionHandler {
        boolean execute(ConnectionModel model);
    }

    public interface ControlSearchHandler {
        void search(String string);
    }

    public interface ControlExecuteHandler {
        void execute(GenerateModel model);
    }

    public interface SelectAllHandler {
        void execute(boolean select);
    }

    private static int _X = 30;
    private static int _WIDTH = 230;
    private static int _HEIGHT = 28;

    private ControlConnectionHandler connectionHandler;
    private ControlSearchHandler searchHandler;
    private ControlExecuteHandler executeHandler;

    private SelectAllHandler selectAllHandler;

    public ControlPanel() {

        this.setLayout(null);
        JLabel label = new JLabel("MySQL Config:");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBounds(_X, 0, _WIDTH, _HEIGHT);
        this.add(label);

        // 主机名
        Label hostLabel = new Label("Host:");
        hostLabel.setBounds(_X, getBottom(label), _WIDTH, _HEIGHT - 8);
        this.add(hostLabel);

        _hostField = new TextField();
        _hostField.setBounds(_X, getBottom(hostLabel), _WIDTH, _HEIGHT);
        this.add(_hostField);

        // 用户名
        Label usernameLabel = new Label("Username:");
        usernameLabel.setBounds(getRight(hostLabel) + _X, getBottom(label), _WIDTH, _HEIGHT - 8);
        this.add(usernameLabel);

        _usernameField = new TextField();
        _usernameField.setBounds(usernameLabel.getX(), getBottom(usernameLabel), _WIDTH, _HEIGHT);
        this.add(_usernameField);

        // 密码
        Label passwordLabel = new Label("Password:");
        passwordLabel.setBounds(getRight(usernameLabel) + _X, getBottom(label), _WIDTH, _HEIGHT - 8);
        this.add(passwordLabel);

        _passwordField = new JPasswordField();
        Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
        Border empty = new EmptyBorder(0, 5, 0, 0);
        CompoundBorder border = new CompoundBorder(line, empty);
        _passwordField.setBorder(border);
        _passwordField.setBounds(passwordLabel.getX(), getBottom(passwordLabel), _WIDTH, _HEIGHT);
        this.add(_passwordField);

        // 数据库
        Label databaseLabel = new Label("Database:");
        databaseLabel.setBounds(_X, getBottom(_hostField), _WIDTH, _HEIGHT - 8);
        this.add(databaseLabel);

        _databaseField = new TextField();
        _databaseField.setBounds(_X, getBottom(databaseLabel), _WIDTH, _HEIGHT);
        this.add(_databaseField);

        // 端口
        Label portLabel = new Label("Port:");
        portLabel.setBounds(getRight(databaseLabel) + _X, getBottom(_hostField), _WIDTH, _HEIGHT - 8);
        this.add(portLabel);

        _portField = new TextField();
        _portField.setBounds(portLabel.getX(), getBottom(portLabel), _WIDTH, _HEIGHT);
        this.add(_portField);

        TextField connField = new TextField();
        connField.setEnabled(false);
        connField.setBounds(getRight(_portField) + _X, _portField.getY(), _WIDTH, _HEIGHT);
        this.add(connField);

        JButton connButton = new JButton("1. Pull tables");
        connButton.setBorderPainted(false);
        connButton.setBounds(0, 0, _WIDTH, _HEIGHT);
        connButton.setBackground(Color.lightGray);
        connButton.addActionListener(this);
        connField.add(connButton);


        // package
        Label packageLabel = new Label("Package:");
        packageLabel.setBounds(_X, getBottom(_databaseField), _WIDTH, _HEIGHT - 8);
        this.add(packageLabel);

        _packageField = new TextField();
        _packageField.setBounds(_X, getBottom(packageLabel), _WIDTH, _HEIGHT);
        this.add(_packageField);

        // targetDir
        final Label targetDirLabel = new Label("Target Directory:");
        targetDirLabel.setBounds(getRight(packageLabel) + _X, getBottom(_databaseField), _WIDTH, _HEIGHT - 8);
        this.add(targetDirLabel);

        final JTextField targetField = new JTextField();
        targetField.setBounds(targetDirLabel.getX(), getBottom(targetDirLabel), _WIDTH * 2 + _X, _HEIGHT);

        Border line2 = BorderFactory.createLineBorder(Color.GRAY);
        Border empty2 = new EmptyBorder(0, 5, 0, 40);
        CompoundBorder border2 = new CompoundBorder(line2, empty2);
        targetField.setBorder(border2);

        this.add(targetField);

        JButton chooseButton = new JButton("...");
        chooseButton.setBorderPainted(false);
        chooseButton.setHorizontalAlignment(SwingConstants.CENTER);
        chooseButton.setBackground(Color.lightGray);
        chooseButton.setBounds(targetField.getWidth() - 40, 0, 40, _HEIGHT);
        targetField.add(chooseButton);
        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(new JLabel(), "Choose");
                File file = jfc.getSelectedFile();
                if (null != file) {
                    if (file.isDirectory()) {
                        targetField.setText(file.getAbsolutePath());
                    } else {
                        targetField.setText(file.getParent());
                    }
                }

                if (isEmpty(targetField.getText())) {
                    showError2(targetField);
                }
            }
        });
        // _______________________________________

        // Search
        Label searchLabel = new Label("Search Table:");
        searchLabel.setBounds(_X, getBottom(_packageField) + 10, _WIDTH, _HEIGHT - 8);
        this.add(searchLabel);
        final TextField searchField = new TextField();
//        searchField.setPlaceholder(" Keyword");
        searchField.setBounds(_X, getBottom(searchLabel), _WIDTH, _HEIGHT);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                                                          public void changedUpdate(DocumentEvent e) {//这是更改操作的处理
                                                              String s = searchField.getText().trim();//trim()方法用于去掉你可能误输入的空格号
                                                              if (null != searchHandler) {
                                                                  searchHandler.search(s);
                                                              }
                                                          }

                                                          public void insertUpdate(DocumentEvent e) {//这是插入操作的处理
                                                              String s = searchField.getText().trim();
                                                              if (null != searchHandler) {
                                                                  searchHandler.search(s);
                                                              }
                                                          }

                                                          public void removeUpdate(DocumentEvent e) {//这是删除操作的处理
                                                              String s = searchField.getText().trim();
                                                              if (null != searchHandler) {
                                                                  searchHandler.search(s);
                                                              }
                                                          }
                                                      }
        );

        this.add(searchField);


        TextField executeField = new TextField();
        executeField.setEnabled(false);
        executeField.setBounds(connField.getX(), searchField.getY(), _WIDTH, _HEIGHT);
        this.add(executeField);

        final JButton executeButton = new JButton("2. Execute");
        executeButton.setBorderPainted(false);
        executeButton.setBounds(0, 0, _WIDTH, _HEIGHT);
        executeButton.setBackground(Color.lightGray);
        executeField.add(executeButton);
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String packageName = _packageField.getText();
                if (isEmpty(packageName)) {
                    showError(_packageField);
                    return;
                }

                String targetDir = targetField.getText();
                if (isEmpty(targetDir)) {
                    showError2(targetField);
                    return;
                }
                ConfigUtils.setValue("packageName", packageName);
                ConfigUtils.setValue("targetDir", targetDir);

                boolean selectAll = _selectAll.isSelected();
                ConfigUtils.setValue("selectAll", selectAll ? "true" : "false");
                boolean override = _override.isSelected();
                ConfigUtils.setValue("override", override ? "true" : "false");
                boolean swagger2 = _enableSwagger2.isSelected();
                ConfigUtils.setValue("enableSwagger2", swagger2 ? "true" : "false");
                boolean lombok = _enableLombok.isSelected();
                ConfigUtils.setValue("enableLombok", lombok ? "true" : "false");


                if (null != executeHandler) {
                    GenerateModel generateModel = new GenerateModel();
                    generateModel.setDatabase(_databaseField.getText());
                    generateModel.setLombok(lombok);
                    generateModel.setSwagger2(swagger2);
                    generateModel.setTargetDir(targetDir);
                    generateModel.setPackageName(packageName);
                    generateModel.setOverride(override);
                    executeHandler.execute(generateModel);
                }

            }
        });


        // 选项
        int y5 = getBottom(searchField) + 10;
        _selectAll = new JCheckBox("SelectAll");
        _selectAll.setBounds(_X, y5, 100, _HEIGHT - 10);
        _selectAll.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (null != selectAllHandler) {
                    selectAllHandler.execute(_selectAll.isSelected());
                }
            }
        });
        this.add(_selectAll);

        _override = new JCheckBox("Override exist");
        _override.setBounds(getRight(_selectAll) + _X, y5, 120, _HEIGHT - 10);
        this.add(_override);

        _enableSwagger2 = new JCheckBox("Enable Swagger2");
        _enableSwagger2.setBounds(getRight(_override) + _X, y5, 150, _HEIGHT - 10);
        this.add(_enableSwagger2);

        _enableLombok = new JCheckBox("Enable Lombok");
        _enableLombok.setBounds(getRight(_enableSwagger2) + _X, y5, 150, _HEIGHT - 10);
        this.add(_enableLombok);

        // 初始化数据
        _hostField.setText(ConfigUtils.getValue("host"));
        _usernameField.setText(ConfigUtils.getValue("username"));
        _passwordField.setText(ConfigUtils.getValue("password"));

        _databaseField.setText(ConfigUtils.getValue("database"));
        _portField.setText(ConfigUtils.getValue("port"));

        _packageField.setText(ConfigUtils.getValue("packageName"));
        targetField.setText(ConfigUtils.getValue("targetDir"));

        _selectAll.setSelected("true".equals(ConfigUtils.getValue("selectAll")));
        _override.setSelected("true".equals(ConfigUtils.getValue("override")));
        _enableSwagger2.setSelected("true".equals(ConfigUtils.getValue("enableSwagger2")));
        _enableLombok.setSelected("true".equals(ConfigUtils.getValue("enableLombok")));

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        doConnected();
    }

    private void doConnected() {
        _hostField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // host
        String host = _hostField.getText();
        if (isEmpty(host)) {
            showError(_hostField);
            return;
        }
        // username
        String username = _usernameField.getText();
        if (isEmpty(username)) {
            showError(_usernameField);
            return;
        }

        // password
        String password = _passwordField.getText();

        // database
        String database = _databaseField.getText();
        if (isEmpty(database)) {
            showError(_databaseField);
            return;
        }

        // port
        String port = _portField.getText();
        if (isEmpty(port)) {
            port = "3306";
            _portField.setText(port);
        }


        if (null != connectionHandler) {
            ConnectionModel model = new ConnectionModel();
            model.setDatabase(database);
            model.setHost(host);
            model.setPassword(password);
            model.setPort(port);
            model.setUsername(username);

            connectionHandler.execute(model);
        }


        // 保存配置
        ConfigUtils.setValue("host", host);
        ConfigUtils.setValue("username", username);
        ConfigUtils.setValue("password", password);

        ConfigUtils.setValue("database", database);
        ConfigUtils.setValue("port", port);
    }

    private void showError(JComponent component) {

        Border line = BorderFactory.createLineBorder(Color.RED);
        Border empty = new EmptyBorder(0, 5, 0, 0);
        CompoundBorder border = new CompoundBorder(line, empty);
        component.setBorder(border);
    }

    private void showError2(JComponent component) {

        Border line2 = BorderFactory.createLineBorder(Color.RED);
        Border empty2 = new EmptyBorder(0, 5, 0, 40);
        CompoundBorder border2 = new CompoundBorder(line2, empty2);
        component.setBorder(border2);
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


    public ControlConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(ControlConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public ControlSearchHandler getSearchHandler() {
        return searchHandler;
    }

    public void setSearchHandler(ControlSearchHandler searchHandler) {
        this.searchHandler = searchHandler;
    }

    public ControlExecuteHandler getExecuteHandler() {
        return executeHandler;
    }

    public void setExecuteHandler(ControlExecuteHandler executeHandler) {
        this.executeHandler = executeHandler;
    }

    public SelectAllHandler getSelectAllHandler() {
        return selectAllHandler;
    }

    public void setSelectAllHandler(SelectAllHandler selectAllHandler) {
        this.selectAllHandler = selectAllHandler;
    }

    // 自定义
    private class TextField extends JTextField {
        public TextField() {
            Border line = BorderFactory.createLineBorder(Color.GRAY);
            Border empty = new EmptyBorder(0, 5, 0, 0);
            CompoundBorder border = new CompoundBorder(line, empty);
            this.setBorder(border);
        }


    }

    private class Label extends JLabel {
        public Label(String title) {
            super(title);
            this.setVerticalAlignment(SwingConstants.BOTTOM);
            this.setHorizontalAlignment(SwingConstants.LEFT);
        }
    }
}
