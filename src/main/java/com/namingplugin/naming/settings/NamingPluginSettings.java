package com.namingplugin.naming.settings;


import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "com.namingplugin.naming.settings.NamingPluginSettings",
    storages = {@Storage("namingPluginSettings.xml")}
)
public class NamingPluginSettings implements PersistentStateComponent<NamingPluginSettings> {
    // 默认提示词
    public String promptTemplate = "你是一个好用的起名助手,也是一个精通java开发的开发老手,你有丰富的起名经验." +
            "你要做的事就是根据我输入的描述或者中文名称生成对应的以动词起头,言简意赅表名变量或函数作用的英文名字," +
            "如果描述中有动词和函数的描述那就是给函数起名,如果没有描述那就默认为变量起名,为变量起名不需要加动词起头." +
            "要求驼峰命名,特别注意见文知意和准确性.其他的自由发挥,不要过长,但是短的表达不了意思就可以用长名称." +
            "只需返回变量名，无需任何解释或额外文字.";

    public String apiKey = "";
    public String modelName = "qwen-turbo-2025-02-11";

    public static NamingPluginSettings getInstance() {
        return ServiceManager.getService(NamingPluginSettings.class);
    }

    @Override
    public @Nullable NamingPluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull NamingPluginSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}