package com.srtp.agent.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好，我是射命丸文，测试一下连接"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}
