package com.srtp.agent.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component     只是用来测试ai大模型连通性的，如果想测试连通信可以删除注释
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好，我是老江，测试一下连接"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}
