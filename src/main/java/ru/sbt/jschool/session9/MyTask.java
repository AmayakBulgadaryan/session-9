package ru.sbt.jschool.session9;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyTask implements Runnable {

    public static int count;

    public boolean isRunning;

    @Override
    public void run() {
            isRunning = true;
            int x = 0;
            while (x != 1000000) {
                x++;
            }
            if (count == 10 || count == 20 || count == 30 || count == 40) {
                count++;
                throw new NullPointerException();
            }
            count++;
    }
}

