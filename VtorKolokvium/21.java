/*Да се имплементира класа за именик PhoneBook со следните методи:
    void addContact(String name, String number) - додава нов контакт во именикот. Ако се обидеме да додадеме контакт со веќе постоечки број, треба да се фрли исклучок од класа DuplicateNumberException со порака Duplicate number: [number]. Комплексноста на овој метод не треба да надминува $O(log N)$ за $N$ контакти.
    void contactsByNumber(String number) - ги печати сите контакти кои во бројот го содржат бројот пренесен како аргумент во методот (минимална должина на бројот [number] е 3). Комплексноста на овој метод не треба да надминува $O(log N)$ за $N$ контакти.
    void contactsByName(String name) - ги печати сите контакти кои даденото име. Комплексноста на овој метод треба да биде $O(1)$.
Во двата методи контактите се печатат сортирани лексикографски според името, а оние со исто име потоа според бројот.*/

import java.util.*;

class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String message) {
        super(message);
    }
}
class Contact{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
class PhoneBook{

    HashSet<String> allNumbers;
    Map<String, TreeSet<Contact>> contactsByNameMap;
    static Comparator<Contact> COMPARATOR=Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber);

    public PhoneBook() {
        allNumbers=new HashSet<>();
        contactsByNameMap =new HashMap<>();
    }

    public PhoneBook(HashSet<String> allNumbers) {
        this.allNumbers = allNumbers;
    }

    void addContact(String name, String number) throws DuplicateNumberException {
       if( allNumbers.contains(number)){
        throw new DuplicateNumberException(String.format("Duplicate number: %s",number));
       }
       
        contactsByNameMap.putIfAbsent(name,new TreeSet<>(COMPARATOR));
        contactsByNameMap.getOrDefault(name,new TreeSet<>(COMPARATOR)).add(new Contact(name, number));
        
        allNumbers.add(number);
        
        List<String> subnumbers=getSubstring(number);
        
        for (String subnumber:subnumbers) {
            contactsByNameMap.putIfAbsent(subnumber,new TreeSet<>(COMPARATOR));
            contactsByNameMap.get(subnumber).add(new Contact(name,number));
        }
    }

    private List<String> getSubstring(String phone){
        
        List<String> result=new ArrayList<>();
        for (int len = 3; len <= phone.length(); len++) {
            for (int i = 0; i <= phone.length()-len; i++) {
                result.add(phone.substring(i,i+len));
            }
        }
        return result;
    }
    void contactsByNumber(String number){
        TreeSet<Contact> contacts = contactsByNameMap.get(number);

        if (contacts != null) {
            contacts.forEach(c -> System.out.println(c));
        } else {
            System.out.println("NOT FOUND");
        }
    }

    void contactsByName(String name) {
        TreeSet<Contact> contacts = contactsByNameMap.get(name);

        if (contacts != null) {
            contacts.forEach(c -> System.out.println(c));
        } else {
            System.out.println("NOT FOUND");
        }
    }

}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}
