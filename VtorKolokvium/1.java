/*Да се имплементира класа за аудиција Audition со следните методи:
    void addParticpant(String city, String code, String name, int age) додава нов кандидат со код code, име и возраст за аудиција во даден град city. Во ист град не се дозволува додавање на кандидат со ист код како некој претходно додаден кандидат (додавањето се игнорира, а комплексноста на овој метод треба да биде $O(1)$)
    void listByCity(String city) ги печати сите кандидати од даден град подредени според името, а ако е исто според возраста (комплексноста на овој метод не треба да надминува $O(n*log_2(n))$, каде $n$ е бројот на кандидати во дадениот град).
*/

import com.sun.source.tree.Tree;

import java.util.*;

class Participants{
    String code;
    String name;
    int age;

    public Participants(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s",code,name,age);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participants that = (Participants) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
class Audition{
    
    Map<String, TreeSet<Participants>> participantsByCity=new HashMap<>();
    Map<String,HashSet<String>> codesByCity=new HashMap<>();
   
    public void addParticpant(String city, String code, String name, int age) {
        
        participantsByCity.putIfAbsent(city,new TreeSet<>(Comparator.comparing(Participants::getName).thenComparing(Participants::getAge).thenComparing(Participants::getCode)));
        codesByCity.putIfAbsent(city,new HashSet<>());

        Participants participant=new Participants(code,name,age);


        if(!codesByCity.get(city).contains(participant.code)){
            participantsByCity.get(city).add(participant);
        }

        codesByCity.get(city).add(participant.code);
    }

    public void listByCity(String city) {
        participantsByCity.get(city).forEach(p-> System.out.println(p));
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
