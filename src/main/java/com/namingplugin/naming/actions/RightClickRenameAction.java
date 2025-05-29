package com.namingplugin.naming.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.namingplugin.naming.settings.NamingPluginSettings;
import com.namingplugin.naming.utils.InvokeAliyunModelUtil;
import org.jetbrains.annotations.NotNull;

public class RightClickRenameAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("SelectedRenameAction triggered!");

        // 获取编辑器中选中的文本
        Editor editor = e.getRequiredData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        String selectedText = editor.getSelectionModel().getSelectedText();
        // 当用户选中文本时
        if (selectedText == null || selectedText.isEmpty()) {
            Messages.showWarningDialog(
                    "请先选择要重命名的文本或描述",
                    "未选择文本"
            );
            return;
        }
        // 打印获取到的文本到控制台
        System.out.println("选中的文本: " + selectedText);

        // 检查是否已配置API Key
        NamingPluginSettings settings = NamingPluginSettings.getInstance();
        if (settings.apiKey == null || settings.apiKey.trim().isEmpty()) {
            // 显示对话框，提示用户配置API Key
            int result = Messages.showYesNoDialog(
                    "您尚未配置API Key，是否现在进行配置？",
                    "配置API Key",
                    "去配置",
                    "取消",
                    Messages.getInformationIcon()
            );
            if (result == Messages.YES) {
                // 打开设置页面
                ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), "智能重命名设置");
            }
            return;
        }

        final Document document = editor.getDocument();
        final int startOffset = editor.getSelectionModel().getSelectionStart();
        final int endOffset = editor.getSelectionModel().getSelectionEnd();

        // 使用后台任务执行API调用，避免阻塞UI线程
        ProgressManager.getInstance().run(new Task.Backgroundable(e.getProject(), "正在生成变量名...", false) {
            String result = null;
            
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("正在调用AI模型...");
                
                try {
                    result = InvokeAliyunModelUtil.RenameWithString(selectedText);
                } catch (InvokeAliyunModelUtil.NamingException ex) {
                    // 在UI线程显示错误消息
                    ApplicationManager.getApplication().invokeLater(() -> {
                        Messages.showErrorDialog(
                                ex.getUserFriendlyMessage(), 
                                "重命名失败"
                        );
                    });
                }
            }
            
            @Override
            public void onSuccess() {
                // 如果获取到有效结果，则替换选中的文本
                if (result != null && !result.isEmpty()) {
                    // 使用CommandProcessor来执行文档修改
                    CommandProcessor.getInstance().executeCommand(
                            getProject(),
                            () -> {
                                ApplicationManager.getApplication().runWriteAction(() -> {
                                    document.replaceString(startOffset, endOffset, result);
                                });
                            },
                            "智能重命名", // 命令名称
                            null // 命令组ID
                    );
                }
            }
        });
    }
}
