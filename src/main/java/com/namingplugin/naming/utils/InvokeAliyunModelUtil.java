package com.namingplugin.naming.utils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import java.util.Arrays;
import com.namingplugin.naming.settings.NamingPluginSettings;

public class InvokeAliyunModelUtil {
    // 自定义异常类
    public static class NamingException extends Exception {
        private final String userFriendlyMessage;
        
        public NamingException(String message, String userFriendlyMessage, Throwable cause) {
            super(message, cause);
            this.userFriendlyMessage = userFriendlyMessage;
        }
        
        public String getUserFriendlyMessage() {
            return userFriendlyMessage;
        }
    }
    
    public static String RenameWithString(String input) throws NamingException {
        try {
            GenerationResult result = callWithMessage(input);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            System.err.println("错误信息：" + e.getMessage());
            
            // 根据不同的异常类型提供友好的错误信息
            String userFriendlyMessage;
            if (e.getMessage() != null && e.getMessage().contains("API key")) {
                userFriendlyMessage = "API Key无效或已过期，请检查您的API Key设置";
            } else if (e.getMessage() != null && e.getMessage().contains("model")) {
                userFriendlyMessage = "模型名称错误或该模型不可用，请尝试其他模型";
            } else if (e.getMessage() != null && e.getMessage().contains("network")) {
                userFriendlyMessage = "网络连接错误，请检查您的网络连接";
            } else if (e.getMessage() != null && e.getMessage().contains("quota")) {
                userFriendlyMessage = "API调用次数已达上限，请稍后再试";
            } else {
                userFriendlyMessage = "调用AI服务失败: " + e.getMessage();
            }
            
            throw new NamingException(e.getMessage(), userFriendlyMessage, e);
        }
    }

    public static GenerationResult callWithMessage(String input) throws Exception {
        // 从设置中获取配置
        NamingPluginSettings settings = NamingPluginSettings.getInstance();
        String promptTemplate = settings.promptTemplate;
        String apiKey = settings.apiKey;
        String modelName = settings.modelName;

        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new NamingException(
                "API Key为空", 
                "请在设置中配置阿里云API Key", 
                null
            );
        }

        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(promptTemplate)
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(input)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(modelName)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
}
