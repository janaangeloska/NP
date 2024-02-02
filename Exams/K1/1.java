/*За потребите на Скопскиот маратон, да се имплементира класа TeamRace со еден статички метод void findBestTeam (InputStream is, OutputStream os). 
Методот од влезен поток ги чита информациите за сите учесници од една ИТ компанија на трка на 5 километри. За секој учесник од тимот информациите се зададени во нов ред во следниот формат:
ID START_TIME END_TIME (пр. 1234 08:00:05 08:31:26)
Методот треба да формира еден тим од најдобрите 4 учесници од компанијата и истиот да го испечати на излезен поток заедно со вкупното време на трчање на членовите на најдобриот тим. Четирите најдобри учесници треба да се сортирани во растечки редослед според времето на трчање. 
Напомена: Во сите тест примери ќе има најмалку 4 учесници од компанијата.
--
For the needs of the Skopje Marathon, implement a class TeamRace with a static method void findBestTeam(InputStream is, OutputStream os).
The method reads information from the input stream for all participants from an IT company in a 5-kilometer race. For each team participant, the information is provided in a new line in the following format:
ID START_TIME END_TIME (ex. 1234 08:00:05 08:31:26)
The method should form a team of the best 4 participants from the company and print it to the output stream along with the total running time of the members of the best team. The four best participants should be sorted in ascending order according to their running time.
Note: In all test cases, there will be at least 4 participants from the company.
*/


import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Participant{
    String id;
    String startTime;
    String endTime;

    public Participant(String id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public int getParsed(String time){
        String [] parts=time.split(":");
        int hr=Integer.parseInt(parts[0]);
        int min=Integer.parseInt(parts[1]);
        int sec=Integer.parseInt(parts[2]);
        return hr*3600+min*60+sec;
    }
    public String getRacingTime(){
        //sec od start
        int start=getParsed(startTime);
        //sec od end
        int end=getParsed(endTime);
        int diff=end-start;
        int hr=diff/3600;
        int min=diff%3600/60;
        int sec=diff%60;
        return String.format("%02d:%02d:%02d",hr,min,sec);
    }

    @Override
    public String toString() {
        return String.format("%s %s",id,getRacingTime());
    }

    public String getId() {
        return id;
    }
}
class TeamRace{
 List<Participant> participants;
 Comparator<Participant> comparator=Comparator.comparing(Participant::getRacingTime).thenComparing(Participant::getId);

    public TeamRace() {
        participants=new ArrayList<>();
    }

    void findBestTeam (InputStream is, OutputStream os){
        Scanner sc=new Scanner(is);
        while (sc.hasNextLine()){
            String line=sc.nextLine();
            String [] parts=line.split("\\s+");
            String index=parts[0];
            String startTime=parts[1];
            String endTime=parts[2];
            participants.add(new Participant(index,startTime,endTime));
        }
        sc.close();

        PrintWriter pw=new PrintWriter(os);

        List<Participant> sorted=participants.stream().sorted(comparator).limit(4).collect(Collectors.toList());
        sorted.forEach(pw::println);
        int sum=0;
        for (int i = 0; i < 4; i++) {
            String time= sorted.get(i).getRacingTime();
            int timeNr=sorted.get(i).getParsed(time);
            sum+=timeNr;
        }
        int hr=sum/3600;
        int min=sum%3600/60;
        int sec=sum%60;
        pw.println(String.format("%02d:%02d:%02d",hr,min,sec));
        pw.flush();
    }
}
public class RaceTest {
    public static void main(String[] args) {
//        try {
            TeamRace teamRace = new TeamRace();
            teamRace.findBestTeam(System.in, System.out);
//            TeamRace.findBestTeam(System.in, System.out);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
