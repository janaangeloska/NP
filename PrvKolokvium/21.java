/*Да се имплементира класа F1Race која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за времињата од последните 3 круга на неколку пилоти на Ф1 трка. Податоците се во следниот формат:
Driver_name lap1 lap2 lap3, притоа lap е во формат mm:ss:nnn каде mm се минути ss се секунди nnn се милисекунди (илјадити делови од секундата). Пример:
Vetel 1:55:523 1:54:987 1:56:134.
Ваша задача е да ги имплементирате методите:
    F1Race() - default конструктор
    void readResults(InputStream inputStream) - метод за читање на податоците
    void printSorted(OutputStream outputStream) - метод кој ги печати сите пилоти сортирани според нивното најдобро време (најкраткото време од нивните 3 последни круга) во формат Driver_name best_lap со 10 места за името на возачот (порамнето од лево) и 10 места за времето на најдобриот круг порамнето од десно. Притоа времето е во истиот формат со времињата кои се читаат.
*/

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class F1Racer{
    String name;
    List<String> vreminja;

    public F1Racer() {
        this.name="";
        this.vreminja=new ArrayList<>();
    }

    public F1Racer(String name, List<String> vreminja) {
        this.name = name;
        this.vreminja = vreminja;
    }

    public String getName() {
        return name;
    }

    public List<String> getVreminja() {
        return vreminja;
    }
    public String getBestLap(){
        return vreminja.stream().min(Comparator.comparing(this::parseTime)).orElse("");
    }
    public long parseTime(String vreme){
        String[]parts=vreme.split(":");
        long min = Integer.parseInt(parts[0]);
        long sec = Integer.parseInt(parts[1]);
        long ms = Integer.parseInt(parts[2]);
        return min*60000+sec*1000+ms;
    }

    @Override
    public String toString() {
    return String.format("%-10s%10s",name,getBestLap());
    }
}
class F1Race {
    List<F1Racer> f1racers;

    public F1Race() {
        this.f1racers = new ArrayList<>();
    }
    public void readResults(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while (sc.hasNextLine()){
            List<String> laps=new ArrayList<>();
            String line=sc.nextLine();
            String [] parts=line.split("\\s+");
            String name=parts[0];
            for (int i = 1; i < 4; i++) {
                laps.add(parts[i]);
            }
            f1racers.add(new F1Racer(name,laps));
        }
    }
    public void printSorted(OutputStream outputStream){
        PrintWriter pw=new PrintWriter(outputStream);
        List<F1Racer> list = f1racers.stream().sorted(Comparator.comparing(F1Racer::getBestLap)).collect(Collectors.toList());
        for (int i = 0; i < f1racers.size(); i++) {
            pw.printf("%d. %s\n",i+1,list.get(i));
        }
        pw.flush();
    }
}
public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

