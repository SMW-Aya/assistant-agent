package com.srtp.agent.tools;

import cn.hutool.core.io.FileUtil;
import com.srtp.agent.constant.FileConstant;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "编程导航原创项目.pdf";
        String content = "编程导航原创项目 https://www.codefather.cn";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
        assertTrue(result.contains("PDF generated successfully to:"));

        String filePath = FileConstant.FILE_SAVE_DIR + "/pdf/" + fileName;
        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(FileUtil.size(file) > 0);
    }
}
