/*Да се дефинира класа ShapesApplication во која се чуваат податоци за повеќе прозорци на кои се исцртуваат геометриски слики во форма на квадрат.
За класата да се дефинира:
    ShapesApplication() - конструктор
    int readCanvases (InputStream inputStream) - метод којшто од влезен поток на податоци ќе прочита информации за повеќе прозорци на кои се исцртуваат квадрати. Во секој ред од потокот е дадена информација за еден прозорец во формат: canvas_id size_1 size_2 size_3 …. size_n, каде што canvas_id е ИД-то на прозорецот, а после него следуваат големините на страните на квадратите што се исцртуваат во прозорецот. Методот треба да врати цел број што означува колку квадрати за сите прозорци се успешно прочитани.
    void printLargestCanvasTo (OutputStream outputStream) - метод којшто на излезен поток ќе го испечати прозорецот чии квадрати имаат најголем периметар. Печатењето да се изврши во форматот canvas_id squares_count total_squares_perimeter.
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Slika{
    private final String id;
    private ArrayList<Integer> sizes;

    public Slika() {
        id="";
        sizes=new ArrayList<>();
    }

    public Slika(String id, ArrayList<Integer> sizes) {
        this.id = id;
        this.sizes = sizes;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Integer> getSizes() {
        return sizes;
    }

    @Override
    public String toString() {
        return id + " " + getSizes().stream().count()+ " " + golemina();
    }
    public int golemina(){
        
        return sizes.stream().mapToInt(i->4*i).sum();
    }

}
class ShapesApplication{

    List<Slika> sliki;

    public ShapesApplication() {
        sliki=new ArrayList<>();
    }

    public void printLargestCanvasTo(PrintStream out) {
        PrintWriter pw=new PrintWriter(out);
        Slika slika=sliki.get(0);
        for (int i = 1; i < sliki.size(); i++) {
            if(sliki.get(i).golemina()>slika.golemina()){
                slika=sliki.get(i);
            }
        }
        pw.println(slika);
        pw.flush();
    }

    public int readCanvases(InputStream in) {
        Scanner sc=new Scanner(in);
        int count=0;
        while(sc.hasNextLine()){
            String line=sc.nextLine();
            String [] parts=line.split("\\s+");
            ArrayList<Integer> sizes=new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                sizes.add(Integer.parseInt(parts[i]));
            count++;
            }
            sliki.add(new Slika(parts[0],sizes));
        }
        return count;
    }
}
public class Shapes1Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);
    }
}
