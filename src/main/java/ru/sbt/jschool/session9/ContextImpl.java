package ru.sbt.jschool.session9;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ContextImpl implements Context {

    private static int completedTaskCount;

    private static int failedTaskCount;

    private static int interruptedTaskCount;

    private List<Future> futuresList;

    private List<Runnable> tasksList;

    ExecutorService executor;

    private int size;

    public ContextImpl(List<Future> futuresList, List<Runnable> tasksList, Runnable callback, ExecutorService executor) {
        size = futuresList.size();
        this.futuresList = futuresList;
        this.tasksList = tasksList;
        this.executor = executor;
        CallbackImpl callbackImpl = (CallbackImpl) callback;
        ((CallbackImpl)callback).setExecutor(executor);
        callbackImpl.setFuturesList(futuresList);
    }

    private void computeCountsTask() {
        synchronized (futuresList){
        if (futuresList != null && tasksList != null)
            for (int i = 0; i < futuresList.size(); i++) {
                Future future = futuresList.get(i);
                if (future.isDone()) {
                    if (future.isCancelled()) {
                        interruptedTaskCount++;
                        futuresList.remove(i);
                        tasksList.remove(i);
                        i--;
                    }
                    else {
                        try {
                            future.get();
                            completedTaskCount++;
                            futuresList.remove(i);
                            tasksList.remove(i);
                            i--;
                        }
                        catch (Exception e) {
                            failedTaskCount++;
                            futuresList.remove(i);
                            tasksList.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getCompletedTaskCount() {
        computeCountsTask();
        return completedTaskCount;
    }

    @Override
    public int getFailedTaskCount() {
        computeCountsTask();
        return failedTaskCount;
    }

    @Override
    public int getInterruptedTaskCount() {
        computeCountsTask();
        return interruptedTaskCount;
    }

    @Override
    public void interrupt() {
        for (int i = 0; i < tasksList.size(); i++) {
            MyTask task = (MyTask)tasksList.get(i);
            synchronized (futuresList) {
                if (task.isRunning == false)
                    futuresList.get(i).cancel(true);
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (completedTaskCount==size||interruptedTaskCount==size){
            executor.shutdown();
            return true;
        }
        return false;
    }

}
