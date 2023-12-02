/*Потребно е да се дефинираат два распоредувачи на задачи кои го имплеметираат интерфејсот TaskScheduler. TaskScheduler е генерички интерфејс за распоредување на задачи (Task) со еден метод schedule кој прима низа од задачи а како резултат се очекува да врати листа од истите тие задачи распоредени согласно критериумите за распоредување.
Задача (Task) е интерфејс кој имплемeнтира метод кој го дава редниот број на задачата за извршување. Постојат да вида на задачи (TimedTask и PriorityTask). Редниот број на извршување на задачата кај TimedTask е дефиниран преку времето на извршување (time), додека кај PriorityTask тој е дефиниран преку приоритетот на извршување (priority).
Првиот распоредувач ги распоредува задачите на тој начин што истите ги сортира според нивниот реден број. Неговата имплементација потребно е да биде дадена со анонимна класа (во рамки на методот getOrdered од класата Schedulers).
Вториот распоредувач го задржува редоследот на добиените задачи, но, ги филтрира (отфрла) сите задачи со поголем реден број од даден праг (order). Неговата имплементација потребно е да биде дадена со ламбда израз (во рамки на методот getFiltered од класата Schedulers).
Распоредените задачи се стартуваат со помош на генеричката класа TaskRunner. За оваа класа потребно е да го дефинирате само параметарскиот тип. */

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * I Partial exam 2016
 */
public class TaskSchedulerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Task[] timeTasks = new Task[n];
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            timeTasks[i] = new TimedTask(x);
        }
        n = scanner.nextInt();
        Task[] priorityTasks = new Task[n];
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            priorityTasks[i] = new PriorityTask(x);
        }
        Arrays.stream(priorityTasks).forEach(System.out::println);
        TaskRunner<Task> runner = new TaskRunner<>();
        System.out.println("=== Ordered tasks ===");
        System.out.println("Timed tasks");
        runner.run(Schedulers.getOrdered(), timeTasks);
        System.out.println("Priority tasks");
        runner.run(Schedulers.getOrdered(), priorityTasks);
        int filter = scanner.nextInt();
        System.out.printf("=== Filtered time tasks with order less then %d ===\n", filter);
        runner.run(Schedulers.getFiltered(filter), timeTasks);
        System.out.printf("=== Filtered priority tasks with order less then %d ===\n", filter);
        runner.run(Schedulers.getFiltered(filter), priorityTasks);
        scanner.close();
    }
}


class TaskRunner<T extends Task> {
    public void run(TaskScheduler<T> scheduler, T[] tasks) {
        List<T> order = scheduler.schedule(tasks);
        order.forEach(System.out::println);
    }
}


interface TaskScheduler<T extends Task> {
    List<T> schedule(T[] tasks);
}


interface Task {
    //dopolnete ovde
    int getOrder();
}

class PriorityTask implements Task {
    private final int priority;

    public PriorityTask(int priority) {
        this.priority = priority;
    }


    @Override
    public String toString() {
        return String.format("PT -> %d", getOrder());
    }

    @Override
    public int getOrder() {
        return priority;
    }
}

class TimedTask implements Task {
    private final int time;

    public TimedTask(int time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return String.format("TT -> %d", getOrder());
    }

    @Override
    public int getOrder() {
        return time;
    }
}

class Schedulers {
    public static <T extends Task> TaskScheduler<T> getOrdered() {

        return new TaskScheduler<T>() {
            @Override
            public List<T> schedule(T[] tasks) {
                return Arrays.stream(tasks)
                        .sorted((t1, t2) -> Integer.compare(t1.getOrder(), t2.getOrder()))
                        .collect(Collectors.toList());
            }
        };
    }

    // vashiot kod ovde (annonimous class)
    public static <T extends Task> TaskScheduler<T> getFiltered(int order) {

        return tasks ->
                Arrays.stream(tasks)
                        .filter(t -> t.getOrder() <= order)
                        .collect(Collectors.toList());

        // vashiot kod ovde (lambda expression)

    }
}
