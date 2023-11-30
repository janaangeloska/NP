/*Да се имплементира класа TimeTable која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за времиња во 24-часовен формат. Сите времиња се разделени со едно празно место, а во самото време часот и минутите може да бидат разделени со : или .. Пример за форматот на податоците:
11:15 0.45 23:12 15:29 18.46
Ваша задача е да ги имплементирате методите:
    TimeTable() - default конструктор
    void readTimes(InputStream inputStream) - метод за читање на податоците
    void writeTimes(OutputStream outputStream, TimeFormat format) - метод кој ги печати сите времиња сортирани во растечки редослед во зададениот формат (24 часовен или AM/PM).
    Методот за читање readTimes фрла исклучоци од тип UnsupportedFormatException ако времињата се разделени со нешто друго што не е : или . и InvalidTimeException ако времето (часот или минутите) е надвор од дозволениот опсег (0-23, 0-59). И двата исклучоци во пораката getMessage() треба да го вратат влезниот податок кој го предизвикал исклучокот. Сите времиња до моментот кога ќе се фрли некој од овие два исклучоци треба да си останат вчитани.
Правила за конверзија од 24-часовен формат во AM/PM:
    за првиот час од денот (0:00 - 0:59), додадете 12 и направете го "AM"
    од 1:00 до 11:59, само направето го "AM"
    од 12:00 до 12:59, само направето го "PM"
    од 13:00 до 23:59 одземете 12 и направете го "PM"
*/

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class InvalidTimeException extends Throwable {
    public InvalidTimeException(String line) {
        super(line);
    }
}

class UnsupportedFormatException extends Throwable {
    public UnsupportedFormatException(String vreme) {
        super(vreme);
    }
}

class Vreme implements Comparable<Vreme>{
    int cas;
    int min;

    public Vreme(int cas, int min) {
        this.cas = cas;
        this.min = min;
    }

    public int getCas() {
        return cas;
    }

    public int getMin() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d",cas,min);
    }

    public String toAMPM(){
        String part="AM";
        int h=cas;
        if(h==0){
            h=12;
            part="AM";
        }
        else if(h>=0&&h<12){
            part="AM";
        }
        else if(h==12){
            h=12;
            part="PM";
        }
        else if(h>12&&h<24){
            h-=12;
            part="PM";
        }
        return String.format("%2d:%02d %s",h,min,part);
    }

    @Override
    public int compareTo(Vreme o) {
        if(cas==o.cas){
            return Integer.compare(min,o.min);
        }else{
            return Integer.compare(cas,o.cas);
        }
    }
}

class TimeTable {
    ArrayList<Vreme> vreminja;

    public TimeTable() {
        vreminja=new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNext()){
            String line=sc.next();

            if(!line.contains(".")&&!line.contains(":")){
                throw new UnsupportedFormatException(line);
            }

            String [] parts=line.split("[: .]");
            if(Integer.parseInt(parts[0])>23 || Integer.parseInt(parts[0])<0 || Integer.parseInt(parts[1])>59 || Integer.parseInt(parts[1])<0){
                throw new InvalidTimeException(line);
            }
            vreminja.add(new Vreme(Integer.parseInt(parts[0]),Integer.parseInt(parts[1])));

        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format){
        PrintWriter pw = new PrintWriter(outputStream);
        vreminja.sort(Vreme::compareTo);
        for (Vreme vreme:vreminja) {
            if(format==TimeFormat.FORMAT_24){
                pw.println(vreme);
            }
            else{
                pw.println(vreme.toAMPM());
            }
        }
        pw.flush();
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}
