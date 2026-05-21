package com.srtp.agent.config;

import com.srtp.agent.tools.FileOperationTool;
import com.srtp.agent.tools.PDFGenerationTool;
import com.srtp.agent.tools.ResourceDownloadTool;
import com.srtp.agent.tools.TerminalOperationTool;
import com.srtp.agent.tools.WebScrapingTool;
import com.srtp.agent.tools.WebSearchTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {

    @Bean
    public ToolCallback[] allTools(
            WebSearchTool webSearchTool,
            WebScrapingTool webScrapingTool,
            ResourceDownloadTool resourceDownloadTool,
            TerminalOperationTool terminalOperationTool,
            PDFGenerationTool pdfGenerationTool
    ) {
        return ToolCallbacks.from(
                new FileOperationTool(),
                webSearchTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGenerationTool
        );
    }
}
