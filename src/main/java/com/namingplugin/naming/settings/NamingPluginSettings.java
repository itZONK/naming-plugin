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
    public String promptTemplate = "你是一位精通Java命名规范的资深开发专家。请根据我提供的描述或中文名称，生成符合Java开发规范的标识符名称：\n\n" +
            "1. 函数命名规则：\n" +
            "   - 使用动词开头（如get、find、calculate）\n" +
            "   - 采用小驼峰命名法(camelCase)\n" +
            "   - 名称应精确反映函数功能\n\n" +
            "2. 变量命名规则：\n" +
            "   - 使用名词，不以动词开头\n" +
            "   - 采用小驼峰命名法(camelCase)\n" +
            "   - 布尔变量以is/has/should等开头\n\n" +
            "3. 常量命名规则：\n" +
            "   - 全大写，下划线分隔(UPPER_SNAKE_CASE)\n\n" +
            "命名长度控制在2-4个单词内，优先使用常见编程术语。如果是专业领域名词，保留其专业准确性。\n\n" +
            "识别逻辑：\n" +
            "- 函数：当描述中包含\"函数\"、\"方法\"、\"处理\"、\"计算\"、\"获取\"、\"查询\"等动词性词语，或描述是一个动作\n" +
            "- 常量：当描述中包含\"常量\"、\"固定值\"、\"静态变量\"、\"配置项\"、\"最大值\"、\"最小值\"等词语\n" +
            "- 变量：不符合上述条件的情况，默认按变量处理\n\n" +
            "只返回命名结果，无需任何解释或额外文字。";


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