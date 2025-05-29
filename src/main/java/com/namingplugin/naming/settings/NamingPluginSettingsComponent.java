package com.namingplugin.naming.settings;

import com.intellij.ide.BrowserUtil;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NamingPluginSettingsComponent {
    private final JPanel mainPanel;
    private final JTextArea promptTemplateArea = new JTextArea();
    private final JPasswordField apiKeyField = new JPasswordField();
    private final JComboBox<String> modelNameComboBox;

    // 定义常用的阿里云模型列表
    private final String[] aliyunModels = {
            //基础模型
            "qwen-turbo",
            "qwen-plus",
            "qwen-max",
            "qwen-max-2025-01-25",
            "qwen-plus-2025-01-12"
    };

    public NamingPluginSettingsComponent() {
        promptTemplateArea.setRows(5);
        promptTemplateArea.setLineWrap(true);
        JScrollPane promptScrollPane = new JScrollPane(promptTemplateArea);

        // 创建模型选择下拉框
        modelNameComboBox = new JComboBox<>(aliyunModels);
        modelNameComboBox.setEditable(true); // 允许用户输入自定义模型名称

        // 创建警告标签
        JLabel warningLabel = new JBLabel("为了运行的流畅性，请不要使用思考型大模型");
        warningLabel.setForeground(Color.RED);
        warningLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        // 创建链接标签
        HyperlinkLabel linkLabel = new HyperlinkLabel("查看完整阿里云模型列表");
        linkLabel.setHyperlinkTarget("https://help.aliyun.com/zh/model-studio/getting-started/models");
        linkLabel.addHyperlinkListener(e -> BrowserUtil.browse(e.getURL()));

        // 创建链接面板
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linkPanel.add(linkLabel);
        linkPanel.setBorder(JBUI.Borders.emptyBottom(10));

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("API Key:"), apiKeyField, 1, false)
                .addLabeledComponent(new JBLabel("模型名称:"), modelNameComboBox, 1, false)
                .addComponent(warningLabel)
                .addComponent(linkPanel)
                .addSeparator()
                .addLabeledComponent(new JBLabel("提示词模板:"), promptScrollPane, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public String getPromptTemplate() {
        return promptTemplateArea.getText();
    }

    public void setPromptTemplate(String text) {
        promptTemplateArea.setText(text);
    }

    public String getApiKey() {
        return new String(apiKeyField.getPassword());
    }

    public void setApiKey(String key) {
        apiKeyField.setText(key);
    }

    public String getModelName() {
        return modelNameComboBox.getEditor().getItem().toString();
    }

    public void setModelName(String modelName) {
        modelNameComboBox.setSelectedItem(modelName);
    }
}