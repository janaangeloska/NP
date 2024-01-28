/*Да се имплементира класа Subtitles која ќе чита од влезен тек (стандарден влез, датотека, ...) превод во стандарден srt формат. Секој еден елемент од преводот се состои од реден број, време на почеток на прикажување, време на крај на прикажување и текст и е во следниот формат (пример):
2
00:00:48,321 --> 00:00:50,837
Let's see a real bet.
Делот со текстот може да има повеќе редови. Сите елементи се разделени со еден нов ред.
Ваша задача е да ги имплементирате методите:
    Subtitles() - default конструктор
    int loadSubtitles(InputStream inputStream) - метод за читање на преводот (враќа резултат колку елементи се прочитани)
    void print() - го печати вчитаниот превод во истиот формат како и при читањето.
    void shift(int ms) - ги поместува времињата на сите елементи од преводот за бројот на милисекунди кои се проследува како аргумент (може да биде негативен, со што се поместуваат времињата наназад).
*/

// package kolokviusmski1.devetnaesetta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Subtitle{
    int id;
    String from;
    String to;

    String text;

    public Subtitle() {}

    public Subtitle(int id, String from, String to, String text) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int parseTime(String vreme){
        String [] time=vreme.split("[:,]");
        //00 01 53 468
        int hr=Integer.parseInt(time[0]);
        int min=Integer.parseInt(time[1]);
        int sec=Integer.parseInt(time[2]);
        int ms=Integer.parseInt(time[3]);
        int vkupnoMS=hr*3600000+min*60000+sec*1000+ms;
        return vkupnoMS;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(id).append("\n")
          .append(from).append(" ").append("-->").append(" ").append(to).append("\n").append(text);
        return sb.toString();
    }
}
class Subtitles{
List<Subtitle> subtitles;

    public Subtitles() {
        subtitles=new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        int counter=0;
        while (sc.hasNextLine()){
            String text="";
            String id=sc.nextLine();
            String line=sc.nextLine();
            String [] parts=line.split("\\s+");
            String timeFrom=parts[0];
            String timeTo=parts[2];
            while (sc.hasNextLine()){
                String line1=sc.nextLine();
                if(line1.isEmpty()){
                    break;
                }
                text+=line1+"\n";
            }
            subtitles.add(new Subtitle((Integer.parseInt(id)),timeFrom,timeTo,text));
        counter++;
        }
      return counter;
    }

    void print(){
        for (Subtitle s:subtitles) {
            System.out.println(s);
        }
    }
    void shift(int ms){
        for (int i = 0; i < subtitles.size(); i++) {
            int subtitleFrom=subtitles.get(i).parseTime(subtitles.get(i).from)+ms;
            int subtitleTo=subtitles.get(i).parseTime(subtitles.get(i).to)+ms;
            // int vkupnoMS=hr*3600000+min*60000+sec*1000+ms;

            int hr=subtitleFrom/3600000;
            int min=(subtitleFrom % 3600000) / 60000;
            int sec=((subtitleFrom % 3600000) % 60000) / 1000;
            int milisec=subtitleFrom%1000;
            subtitles.get(i).setFrom(String.format("%02d:%02d:%02d,%03d",hr,min,sec,milisec));

            int hr1=subtitleTo/3600000;
            int min1=(subtitleTo % 3600000) / 60000;
            int sec1=((subtitleTo % 3600000) % 60000) / 1000;
            int milisec1=subtitleTo%1000;
            subtitles.get(i).setTo(String.format(String.format("%02d:%02d:%02d,%03d",hr1,min1,sec1,milisec1)));
        }
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

