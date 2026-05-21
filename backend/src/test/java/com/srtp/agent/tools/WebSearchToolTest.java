package com.srtp.agent.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WebSearchToolTest {

    @Test
    public void testSearchWebWithoutApiKey() {
        WebSearchTool tool = new WebSearchTool();
        String result = tool.searchWeb("java", 3, 1);
        JSONObject obj = JSONUtil.parseObj(result);
        assertFalse(obj.getBool("success", true));
        assertEquals("missing config: searchapi.api-key", obj.getStr("message"));
    }
}
