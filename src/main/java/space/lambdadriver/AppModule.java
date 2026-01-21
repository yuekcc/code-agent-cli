package space.lambdadriver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jakarta.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppModule extends AbstractModule {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public AppModule() {
        super();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.executorService.shutdown();
            try {
                this.executorService.awaitTermination(300, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }));
    }

    @Provides
    @Singleton
    public ExecutorService provideVirtualThreadExecutor() {
        // Java 21 特有的虚拟线程池
        return this.executorService;
    }
}
