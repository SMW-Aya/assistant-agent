package com.srtp.agent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileOperationToolTest {

    @Test
    public void testReadFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "编程导航.txt";
        String result = tool.readFile(fileName);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testWriteFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "编程导航-test.txt";
        String content = "https://www.codefather.cn 程序员编程学习交流社区";
        String writeResult = tool.writeFile(fileName, content);
        assertNotNull(writeResult);
        assertTrue(writeResult.contains("successfully"));

        String readResult = tool.readFile(fileName);
        assertEquals(content, readResult);
    }
}
