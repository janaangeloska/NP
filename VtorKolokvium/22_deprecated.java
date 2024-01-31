/*Да се имплементира класа за календар на настани EventCalendar. Секој настан е дефиниран со:
    име
    локација
    време (Date).
Класата треба да ги овозможува следните функционалности:
    public EventCalendar(int year) - конструктор со еден аргумент годината на календарот
    public void addEvent(String name, String location, Date date) - додава нов настан зададен со име, локација и време. Ако годината на настанот не се совпаѓа со годината на календарот да се фрли исклучок од вид WrongDateException со порака Wrong date: [date].
    public void listEvents(Date date) - ги печати сите настани на одреден датум (ден) подредени според времето на одржување во растечки редослед (ако два настани имаат исто време на одржување, се подредуваат лексикографски според името). Добивањето колекција од настани на одреден датум треба да биде во константно време $O(1)$, а печатењето во линеарно време $O(n)$ (без сортирање, само изминување)! Форматот на печатење настан е dd MMM, YYY HH:mm at [location], [name].
    public void listByMonth() - ги печати сите месеци (1-12) со бројот на настани во тој месец.
*/

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class  WrongDateException extends Exception{
    public WrongDateException(String message) {
        super(message);
    }
}
class Event{
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM, yyyy HH:mm");
        String formattedDate=dateFormat.format(date);
        return String.format("%s at %s, %s",formattedDate,getLocation(),getName());
    }
}
class EventCalendar{
    int year;
    Set<Event> events;
    Comparator<Event> comparator=Comparator.comparing(Event::getDate).thenComparing(Event::getName);

    public EventCalendar(int year) {
        this.year=year;
        events=new TreeSet<>(comparator);
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        if(c.get(Calendar.YEAR)!=year){
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            String formattedDate = dateFormat.format(date);
            throw new WrongDateException(String.format("Wrong date: %s.",formattedDate));
        }
        events.add(new Event(name,location,date));
    }

    public void listEvents(Date date) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy");
        String input=simpleDateFormat.format(date);
        List<Event> eventsOnDate=events.stream()
                .filter(i -> simpleDateFormat.format(i.getDate()).equals(input))
                .collect(Collectors.toList());
        if (eventsOnDate.isEmpty()) {
            System.out.println("No events on this day!");
        } else {
        eventsOnDate.forEach(System.out::println);
        }
    }

    public void listByMonth(){
        IntStream.range(0,12).forEach(i-> {
            long count = events.stream().filter(event -> event.getDate().getMonth() == i).count();
            System.out.printf("%d : %d\n",(1 + i),count);
        });
    }
}
public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
