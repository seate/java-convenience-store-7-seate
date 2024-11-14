package store;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import store.back.BackApplication;
import store.front.FrontApplication;

public class Application {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        Thread frontThread = new Thread(FrontApplication::run);
        EXECUTOR_SERVICE.submit(frontThread);

        BackApplication.run();
        try {
            frontThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
