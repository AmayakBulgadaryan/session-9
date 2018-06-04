package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutionManagerImpl implements  ExecutionManager{

    private ExecutorService executor;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
            ExecutionManager executionManager = new ExecutionManagerImpl();
            Runnable[] tasks = new Runnable[50];
            for (int i = 0; i < tasks.length; i++)
                tasks[i] = new MyTask();
            Runnable callback = new CallbackImpl();
            Context context = executionManager.execute(callback, tasks);
            System.out.println(context.getCompletedTaskCount());
            System.out.println(context.getFailedTaskCount());
            System.out.println(context.getInterruptedTaskCount());
            Thread.sleep(1000);
            System.out.println(context.getCompletedTaskCount());
            System.out.println(context.getFailedTaskCount());
            System.out.println(context.getInterruptedTaskCount());
    }

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        List<Runnable> tasksList = new ArrayList<>();
        List<Future> futuresList = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(2);
        executor.submit(callback);
        for (int i = 0; i < tasks.length ; i++) {
            tasksList.add(tasks[i]);
            Future future = executor.submit(tasks[i]);
            futuresList.add(future);
        }
//        new Thread(callback).start();
        Context context = new ContextImpl(futuresList, tasksList, callback, executor);
        return context;
    }
}
