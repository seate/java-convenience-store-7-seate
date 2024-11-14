package store.back;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import store.back.global.support.ApplicationContext;
import store.back.global.support.FrontController;

public class BackApplication {

    public static void run() {
        ApplicationContext.init();

        FrontController.run();
    }
}
