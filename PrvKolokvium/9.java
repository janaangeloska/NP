/*Да се имплемнтира генеричка класа Triple (тројка) од нумерички вредности (три броја). За класата да се имплементираат:
    конструктор со 3 аргументи,
    double max() - го враќа најголемиот од трите броја
    double average() - кој враќа просек на трите броја
    void sort() - кој ги сортира елементите во растечки редослед
    да се преоптовари методот toString() кој враќа форматиран стринг со две децимални места за секој елемент и празно место помеѓ*/


import java.util.Scanner;
class Triple <T extends Number>{
    T br1;
    T br2;
    T br3;

    public Triple(T br1, T br2, T br3) {
        this.br1 = br1;
        this.br2 = br2;
        this.br3 = br3;
    }

    public double max(){
        double max=br1.doubleValue();
        if(br2.doubleValue()>max){
            max= br2.doubleValue();
        }
        if (br3.doubleValue()>max) {
            max=br3.doubleValue();
        }
        return max;
    }

    public double avarage(){
        return (br1.doubleValue()+br2.doubleValue()+br3.doubleValue())/3;
    }

    public void sort(){
        if(br1.doubleValue()>br2.doubleValue()){
            T tmp=br2;
            br2=br1;
            br1=tmp;
        }
        if(br2.doubleValue()>br3.doubleValue()){
            T tmp=br3;
            br3=br2;
            br2=tmp;
        }
        if(br1.doubleValue()>br2.doubleValue()){
            T tmp=br2;
            br2=br1;
            br1=tmp;
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f",br1.doubleValue(),br2.doubleValue(),br3.doubleValue());
    }
}
public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
