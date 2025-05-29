package com.namingplugin.naming.settings;

import com.intellij.openapi.options.Configurable;

import javax.swing.*;

public class NamingPluginConfigurable implements Configurable {
    private NamingPluginSettingsComponent settingsComponent;

    @Override
    public String getDisplayName() {
        return "智能重命名";
    }

    @Override
    public JComponent createComponent() {
        settingsComponent = new NamingPluginSettingsComponent();
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        NamingPluginSettings settings = NamingPluginSettings.getInstance();
        return !settingsComponent.getPromptTemplate().equals(settings.promptTemplate) ||
                !settingsComponent.getApiKey().equals(settings.apiKey) ||
                !settingsComponent.getModelName().equals(settings.modelName);
    }

    @Override
    public void apply() {
        NamingPluginSettings settings = NamingPluginSettings.getInstance();
        settings.promptTemplate = settingsComponent.getPromptTemplate();
        settings.apiKey = settingsComponent.getApiKey();
        settings.modelName = settingsComponent.getModelName();
    }

    @Override
    public void reset() {
        NamingPluginSettings settings = NamingPluginSettings.getInstance();
        settingsComponent.setPromptTemplate(settings.promptTemplate);
        settingsComponent.setApiKey(settings.apiKey);
        settingsComponent.setModelName(settings.modelName);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }

    @Override
    public String getHelpTopic() {
        return "使用说明：\n1. 输入阿里云API Key\n2. 选择或输入阿里云模型名称\n3. 根据需要自定义提示词模板";
    }
}