package org.example;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Main {
    static final int size = 1_000_000_000; // 1 GB

    // any argument will trigger off heap allocation
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);


        System.out.println("allocating");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("SHUTDOWN HOOK CALLED");
            }
        });
        try {
            if (args.length == 0) {
                // heap test
                var x = new byte[size]; // 1 GB
                System.out.printf("done allocating on heap, first byte=%d, last byte=%d\n", x[0], x[x.length - 1]);
            } else {
                // off heap
                long x = unsafe.allocateMemory(size);
                byte b = 0;
                for (int i = 0; i < size; i++) {
                    unsafe.putByte(x + i, b);
                }
                System.out.printf("off heap allocated., first byte=%d, last byte=%d\n", unsafe.getByte(x), unsafe.getByte(x + size - 1));
            }
        } catch (Throwable t) {
            System.err.println("Exception caught...");
            t.printStackTrace();
        }
        System.exit(0);
    }
}