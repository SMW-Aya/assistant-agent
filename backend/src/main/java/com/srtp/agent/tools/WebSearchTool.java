package com.srtp.agent.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class WebSearchTool {

    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";
    @Value("${searchapi.api-key:}")
    private String apiKey;

    @Tool(description = "Search web pages using SearchApi Baidu engine and return concise results")
    public String searchWeb(
            @ToolParam(description = "Search keyword") String query,
            @ToolParam(description = "Number of results to return, recommend 1-10") Integer num,
            @ToolParam(description = "Page number, starts from 1") Integer page
    ) {
        if (StrUtil.isBlank(query)) {
            return error("query must not be blank");
        }

        if (StrUtil.isBlank(apiKey)) {
            return error("missing config: searchapi.api-key");
        }

        int safeNum = num == null ? 5 : Math.max(1, Math.min(num, 10));
        int safePage = page == null ? 1 : Math.max(1, page);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("engine", "baidu");
        params.put("q", query);
        params.put("num", safeNum);
        params.put("page", safePage);
        params.put("api_key", apiKey);

        try (HttpResponse response = HttpRequest.get(SEARCH_API_URL)
                .form(params)
                .timeout(15000)
                .execute()) {
            if (response.getStatus() != 200) {
                return error("search api request failed, status=" + response.getStatus() + ", body=" + response.body());
            }

            JSONObject raw = JSONUtil.parseObj(response.body());
            JSONArray organicResults = raw.getJSONArray("organic_results");
            JSONArray items = new JSONArray();
            if (organicResults != null) {
                for (int i = 0; i < organicResults.size(); i++) {
                    JSONObject item = organicResults.getJSONObject(i);
                    JSONObject one = new JSONObject();
                    one.set("title", item.getStr("title"));
                    one.set("link", item.getStr("link"));
                    one.set("snippet", item.getStr("snippet"));
                    items.add(one);
                }
            }

            JSONObject result = new JSONObject();
            result.set("query", query);
            result.set("engine", "baidu");
            result.set("page", safePage);
            result.set("num", safeNum);
            result.set("count", items.size());
            result.set("items", items);
            return result.toStringPretty();
        } catch (Exception e) {
            return error("search failed: " + e.getMessage());
        }
    }

    private String error(String message) {
        JSONObject obj = new JSONObject();
        obj.set("success", false);
        obj.set("message", message);
        return obj.toString();
    }
}
