/*Да се имплементира генеричка класа Cluster во која се чуваат елементи кои мора да обезбедат сопствен идентификатор long и да може да се пресметува растојание (double) помеѓу два вакви елементи. Во класата да се имплементираат следните два методи:
    void addItem(Т element) - за додавање нов елемент во кластерот
    void near(long id, int top) - кој ги печати најблиските top елементи до елементот со дадениот идентификатор id подредени според растојанието во опаѓачки редослед.
Потоа да се имплементира класа Point2D која претставува конкретна имплементација на елемент во кластерот за точка во 2Д простор со дадени:
    id - long
    x - float
    y - float
Растојанието помеѓу две Point2D точки се пресметува со формулата за Евклидово растојание $\sqrt{{(x1 - x2)^2} + {(y1 - y2)^2}}$*/

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

interface ICluster<T> {
    long id();

    double distanceBetween(T a);
}

class Cluster<T extends ICluster<T>> {
    Map<Long, T> points;

    public Cluster() {
        points = new HashMap<>();
    }

    public void addItem(T element) {
        points.put(element.id(), element);
    }

    public void near(long id, int top) {
        AtomicInteger i = new AtomicInteger(1);
        T element = points.get(id);
        points.values().stream()
                .filter(index -> index.id() != id)
                .sorted(Comparator.comparing(x -> x.distanceBetween(element)))
                .limit(top)
                .forEach(point -> System.out.printf("%d. %d -> %.3f\n", i.getAndIncrement(), point.id(), point.distanceBetween(element)));
    }
}

class Point2D implements ICluster<Point2D> {

    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public long id() {
        return id;
    }

    /*Растојанието помеѓу две Point2D точки се
    пресметува со формулата за Евклидово растојание
    $\sqrt{{(x1 - x2)^2} + {(y1 - y2)^2}}$*/
    @Override
    public double distanceBetween(Point2D a) {
        return Math.sqrt((x - a.x) * (x - a.x) + (y - a.y) * (y - a.y));
    }
}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}
