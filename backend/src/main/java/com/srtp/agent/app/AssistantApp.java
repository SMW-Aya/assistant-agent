package com.srtp.agent.app;

import com.srtp.agent.advisor.BannedWordAdvisor;
import com.srtp.agent.advisor.MyLoggerAdvisor;
import com.srtp.agent.chatmemory.RedisChatMemoryRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AssistantApp {

    private static final String SYSTEM_PROMPT = "你是专业Java助教。你的唯一任务是解决用户的Java编程问题。请严格执行：1.先确认学生的具体报错或环境；2.通过引导提问引导学生思考，不直接给答案；3.回答末尾必须有2个技术深度追问。保持专业Office Hour风格";
    private static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";

    private final ChatClient chatClient;

    @Resource
    private VectorStore loveAppVectorStore;

    @Autowired
    public AssistantApp(ChatModel dashscopeChatModel,
                        RedisChatMemoryRepository chatMemoryRepository,
                        BannedWordAdvisor bannedWordAdvisor) {
        this(createChatClient(dashscopeChatModel, chatMemoryRepository, bannedWordAdvisor));
    }

    AssistantApp(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    AssistantApp(ChatClient chatClient, VectorStore AssistantAppVectorStore) {
        this.chatClient = chatClient;
        this.loveAppVectorStore = AssistantAppVectorStore;
    }

    private static ChatClient createChatClient(ChatModel dashscopeChatModel,
                                               ChatMemoryRepository chatMemoryRepository,
                                               BannedWordAdvisor bannedWordAdvisor) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();

        return ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        bannedWordAdvisor,
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    public String doChatWithRag(String message, String chatId) {
        RetrievalAugmentationAdvisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(loveAppVectorStore)
                        .topK(10)
                        .build())
                .build();

        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .advisors(new MyLoggerAdvisor())
                .advisors(ragAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}
