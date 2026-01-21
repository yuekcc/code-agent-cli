package space.lambdadriver.util;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LlmClient {
    private static final String OPENAI_ENDPOINT_URL = "http://127.0.0.1:9981/v1/chat/completions";
    private static final String API_KEY = "sk-1234";

    private final java.net.http.HttpClient httpClient;

    public LlmClient() throws Exception {
        // 创建 HttpClient
        this.httpClient = HttpClientUtils.create();
    }

    public String send(String body) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_ENDPOINT_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        StringBuilder buf = new StringBuilder();
        this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofLines()).body().forEach(line -> {
            readLine(line, buf);
        });

        return buf.toString();
    }

    private static void readLine(String line, StringBuilder buf) {
        if (line == null || line.isBlank()) {
            return;
        }

        if (!line.startsWith("data:")) {
            return;
        }

        String content = line.substring(5).trim();
        if (content.equals("[DONE]")) {
            return;
        }

        ReplyMessage message = JSON.parseObject(content, ReplyMessage.class);
        String delta = message.getChoices().getFirst().getDelta().getContent();
        if (delta != null) {
            buf.append(delta);

            // TODO
            System.out.print(delta);
        }
    }

    @Data
    private static class ReplyMessage {
        private List<ChoiceItem> choices;
    }

    @Data
    private static class ChoiceItem {
        private ContentDelta delta;
    }

    @Data
    private static class ContentDelta {
        private String content;
    }
}
