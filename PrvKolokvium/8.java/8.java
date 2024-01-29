/*Да се имплементира класа ArchiveStore во која се чува листа на архиви (елементи за архивирање).
Секој елемент за архивирање Archive има:
    id - цел број
    dateArchived - датум на архивирање.
Постојат два видови на елементи за архивирање, LockedArchive за кој дополнително се чува датум до кој не смее да се отвори dateToOpen и SpecialArchive за кој се чуваат максимален број на дозволени отварања maxOpen. За елементите за архивирање треба да се обезбедат следните методи:
    LockedArchive(int id, LocalDate dateToOpen) - конструктор за заклучена архива
    SpecialArchive(int id, int maxOpen) - конструктор за специјална архива
За класата ArchiveStore да се обезбедат следните методи:
    ArchiveStore() - default конструктор
    void archiveItem(Archive item, LocalDate date) - метод за архивирање елемент item на одреден датум date
    void openItem(int id, LocalDate date) - метод за отварање елемент од архивата со зададен id и одреден датум date. Ако не постои елемент со даденото id треба да се фрли исклучок од тип NonExistingItemException со порака Item with id [id] doesn't exist.
    String getLog() - враќа стринг со сите пораки запишани при архивирањето и отварањето архиви во посебен ред.
За секоја акција на архивирање во текст треба да се додаде следната порака Item [id] archived at [date], додека за секоја акција на отварање архива треба да се додаде Item [id] opened at [date]. При отварање ако се работи за LockedArhive и датумот на отварање е пред датумот кога може да се отвори, да се додаде порака Item [id] cannot be opened before [date]. Ако се работи за SpecialArhive и се обидиеме да ја отвориме повеќе пати од дозволениот број (maxOpen) да се додаде порака Item [id] cannot be opened more than [maxOpen] times.*/

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

class NonExistingItemException extends Exception{
    int id;
    public NonExistingItemException(int id) {
        super();
        this.id=id;
    }

    @Override
    public String getMessage() {
        return String.format("Item with id %d doesn't exist",id);
    }
}
abstract class Archive{
    int id;
    LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
    abstract public String openItem(int id,LocalDate localDate);
}
class LockedArchive extends Archive{

    LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String openItem(int id, LocalDate localDate) {
        if(localDate.isBefore(dateToOpen)){
            return String.format("Item %d cannot be opened before %s\n",id,dateToOpen);
        }else{
            return String.format("Item %d opened at %s\n",id,localDate);
        }
    }
    
}

class SpecialArchive extends Archive{
    int maxOpen;
    int counter;

    public SpecialArchive(int id,int maxOpen) {
        super(id);
        this.maxOpen=maxOpen;
        counter=0;
    }
    
    @Override
    public String openItem(int id, LocalDate localDate) {
        if(maxOpen==counter){
            return String.format("Item %d cannot be opened more than %d times\n",id,maxOpen);
        }else{
            counter++;
            return String.format("Item %d opened at %s\n",id,localDate);
        }
    }
}

class ArchiveStore{
    List<Archive> archives;
    String sb;

    public ArchiveStore() {
        archives=new ArrayList<>();
        sb="";
    }

    public void archiveItem(Archive item, LocalDate date){
        item.setDateArchived(date);
        sb+=String.format("Item %d archived at %s\n",item.id,date);
        archives.add(item);
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        Optional<Archive> itemId = archives.stream().filter(i->i.getId()==id).findFirst();
        if(itemId.isEmpty()){
            throw new NonExistingItemException(id);
        }else{
            sb+=itemId.get().openItem(id,date);
        }
    }

    public String getLog() {
        return sb;
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}
