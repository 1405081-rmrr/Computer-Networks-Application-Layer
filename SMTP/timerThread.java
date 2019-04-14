package mySMTP;

import java.util.concurrent.atomic.AtomicBoolean;

public class timerThread implements Runnable{
    private Thread thread;
    private final AtomicBoolean flag = new AtomicBoolean(false);

    public timerThread() {
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        flag.set(false);
    }

    public void run() {
        flag.set(true);
        long start = System.currentTimeMillis();
        long end = start + 20*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end){
            if(!flag.get()){
                try {
                    Thread.sleep(100*10000);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    System.out.println(
                            "Thread was interrupted, Failed to complete operation");
                }
            }
        }
        System.exit(0);

    }
}
