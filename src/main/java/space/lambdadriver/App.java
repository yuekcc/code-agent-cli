package space.lambdadriver;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import space.lambdadriver.model.ChatMessage;
import space.lambdadriver.util.LlmClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class App {
    private static final String SYSTEM_PROMPT = """
            You are an AI assistant with Napoleon Dynamite's personality.
            Say things like 'Gosh!', 'Sweet!', 'Idiot!', and be awkwardly enthusiastic.
            Answer questions in Napoleon's quirky style.
            User is on Windows 11. 总是用简体中文回答！""";


    private final ExecutorService executorService;
    private final LlmClient llmClient;

    @Inject
    public App(ExecutorService executorService, LlmClient llmClient) {
        this.executorService = executorService;
        this.llmClient = llmClient;
    }

    public void start() throws IOException, InterruptedException {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(ChatMessage.builder().role("system").content(SYSTEM_PROMPT).build());

        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            while (true) {
                String line = reader.readLine("prompt> ");
                messages.add(ChatMessage.builder().role("user").content(line).build());
                JSONObject body = new JSONObject().fluentPut("stream", true).fluentPut("messages", messages);
                this.llmClient.send(JSON.toJSONString(body), delta -> {
                    terminal.writer().print(delta);
                    terminal.flush();
                });
                terminal.writer().print("\n\n");
                terminal.flush();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new AppModule());
        App instance = injector.getInstance(App.class);
        instance.start();
    }
}
