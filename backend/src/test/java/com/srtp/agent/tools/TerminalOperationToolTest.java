package com.srtp.agent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerminalOperationToolTest {

    @Test
    public void testExecuteTerminalCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String command = "pwd";
        String result = tool.executeTerminalCommand(command);
        assertNotNull(result);
        assertTrue(result.trim().length() > 0);
    }
}
