package org.example;

import java.util.concurrent.CompletableFuture;

public class Main3 {
    static final int size = 1_000_000_000; // 1 GB

    // any argument will trigger off heap allocation
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("allocating");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("SHUTDOWN HOOK CALLED");
            }
        });
        try {
            CompletableFuture.runAsync(() -> {
                // heap test
                var x = new byte[size]; // 1 GB
                System.out.printf("done allocating on heap, first byte=%d, last byte=%d\n", x[0], x[x.length - 1]);
            }).get();
        } catch (Throwable t) {
            System.err.println("Exception caught in MAIN ...");
            t.printStackTrace();
        }

        System.exit(0);
    }
}
