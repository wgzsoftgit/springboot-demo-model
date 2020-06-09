package com.demomodel.query.threadPool.ExecutorsOOM;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//通过指定JVM参数：-Xmx8m -Xms8m 运行以上代码，会抛出OOM:
public class ExecutorsDemoOOM {
    private static ExecutorService executor = Executors.newFixedThreadPool(15);
    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executor.execute(new SubThread());
        }
    }
}
