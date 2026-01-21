package space.lambdadriver;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import space.lambdadriver.util.LlmClient;

import java.util.concurrent.ExecutorService;

public class App {

    private final ExecutorService executorService;
    private final LlmClient llmClient;

    @Inject
    public App(ExecutorService executorService, LlmClient llmClient) {
        this.executorService = executorService;
        this.llmClient = llmClient;
    }

    public void start() {
        //        System.out.println(App.class.getCanonicalName());
        //        this.executorService.submit(() -> {
        //            System.out.println("call in thread");
        //        });

        JSONArray messages = new JSONArray().fluentAdd(new JSONObject().fluentPut("role", "user").fluentPut("content", "简单介绍python3"));
        JSONObject body = new JSONObject().fluentPut("stream", true).fluentPut("messages", messages);
        try {
            String res = this.llmClient.send(JSON.toJSONString(body));
        } catch (Exception e) {
            System.err.println("异常，" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());
        App instance = injector.getInstance(App.class);
        instance.start();
    }
}
