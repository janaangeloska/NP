/*Да се имплементира класа Canvas на која ќе чуваат различни форми. За секоја форма се чува:
    id:String
    color:Color (enum дадена)
Притоа сите форми треба да имплментираат два интерфејси:
    Scalable - дефиниран со еден метод void scale(float scaleFactor) за соодветно зголемување/намалување на формата за дадениот фактор
    Stackable - дефиниран со еден метод float weight() кој враќа тежината на формата (се пресметува како плоштина на соодветната форма)
Во класата Canvas да се имплементираат следните методи:
    void add(String id, Color color, float radius) за додавање круг
    void add(String id, Color color, float width, float height) за додавање правоаголник
При додавањето на нова форма, во листата со форми таа треба да се смести на соодветното место според нејзината тежина. Елементите постојано се подредени според тежината во опаѓачки редослед.
    void scale(String id, float scaleFactor) - метод кој ја скалира формата со даденото id за соодветниот scaleFactor. Притоа ако има потреба, треба да се изврши преместување на соодветните форми, за да се задржи подреденоста на елементите.
Не смее да се користи сортирање на листата.
    toString() - враќа стринг составен од сите фигури во нов ред. За секоја фигура се додава:
    C: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно] ако е круг
    R: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно] ако е правоаголник
Користење на instanceof ќе се смета за неточно решение*/

//package kolokviusmski1.shesta;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.Collectors;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}
class Shape implements Scalable,Stackable{
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void scale(float scaleFactor) {}

    @Override
    public float weight() {return 0;}
}
class Circle extends Shape{

    float r;
    public Circle(String id, Color color,float r) {
        super(id, color);
        this.r=r;
    }

    @Override
    public void scale(float scaleFactor) {
        r=r*scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (r*r*Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f",id,color,weight());
    }
}
class Rectangle extends Shape{
    float width;
    float height;

    public Rectangle(String id, Color color,float width, float height) {
        super(id, color);
        this.width=width;
        this.height=height;
    }

    @Override
    public void scale(float scaleFactor) {
        width=width*scaleFactor;
        height=height*scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f",id,color,weight());
    }
}
class Canvas {
        List<Shape> shapes;

    public Canvas() {
        shapes=new ArrayList<>();
    }
    public void add(String id, Color color, float radius){
        Circle c=new Circle(id,color,radius);
    addIt(c);
    }

    public void add(String id, Color color, float width, float height){
        Rectangle r=new Rectangle(id,color,width,height);
    addIt(r);
    }

    void addIt(Shape shape){
        for (int i = 0; i < shapes.size(); i++) {
            if(shapes.get(i).weight()<shape.weight()){
                shapes.add(i, shape);
                return;
            }
        }
        shapes.add(shape);
    }
    void scale(String id, float scaleFactor){
        for (int i = 0; i < shapes.size(); i++) {
            if (id.equals(shapes.get(i).getId())) {
            Shape s=shapes.get(i);
            shapes.remove(s);
            s.scale(scaleFactor);
            addIt(s);
            break;
            }
        }
    }

    @Override
    public String toString() {
return shapes.stream().map(Shape::toString).collect(Collectors.joining("\n"))
                + "\n";    }
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
