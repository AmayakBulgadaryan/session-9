package ru.sbt.jschool.session9;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CallbackImpl implements Runnable{

    private List<Future> futuresList;

    private ExecutorService executor;

    public boolean setExecutor(ExecutorService executor){
        if (this.executor==null){
            this.executor = executor;
            return true;
        }
        return false;
    }

    public boolean setFuturesList(List<Future> futuresList){
        if (this.futuresList==null) {
            this.futuresList = futuresList;
            return true;
        }
        return false;
    }

    private boolean checkTasksDone() {
            if (futuresList == null) {
                return false;
            }
            synchronized (futuresList) {
                int countDoneTasks = 0;
                for (int i = 0; i < futuresList.size(); i++) {
                    if (futuresList.get(i).isDone()) {
                        countDoneTasks++;
                    }
                }
                return futuresList.size() == countDoneTasks;
            }
        }

    @Override
    public void run() {
        while (!checkTasksDone());
        executor.shutdown();
        System.out.println("Time to Callback, my friends!");
    }
}
