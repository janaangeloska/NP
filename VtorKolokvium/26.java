/*Да се имплементира класа StudentRecords која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за студентски досиеа. Податоците содржат (код - единствен стринг), насока (стринг од 3 букви) и низа со оценки (цели броеви од 6 - 10). Сите податоци се разделени со едно празно место. Пример за форматот на податоците:
ioqmx7 MT 10 8 10 8 10 7 6 9 9 9 6 8 6 6 9 9 8
Ваша задача е да ги имплементирате методите:
    StudentRecords() - default конструктор
    int readRecords(InputStream inputStream) - метод за читање на податоците кој враќа вкупно прочитани записи
    void writeTable(OutputStream outputStream) - метод кој ги печати сите записите за сите студенти групирани по насока (најпрво се печати името на насоката), а потоа се печатат сите записи за студентите од таа насока сортирани според просекот во опаѓачки редослед (ако имаат ист просек според кодот лексикографски) во формат kod prosek, каде што просекот е децимален број заокружен на две децимали. Пример jeovz8 8.47. Насоките се сортирани лексикографски. Комплексноста на методот да не надминува $O(N)$ во однос на бројот на записи.
    void writeDistribution(OutputStream outputStream) - метод за печатење на дистрибуцијата на бројот на оценки по насока, притоа насоките се сортирани по бројот на десетки во растечки редослед (прва е насоката со најмногу оценка десет). Дистрибуцијата на оценки се печати во следниот формат:
NASOKA
[оценка со 2 места порамнети во десно] | [по еден знак * на секои 10 оценки] ([вкупно оценки])
Пример:
KNI
 6 | ***********(103)
 7 | ******************(173)
 8 | *******************(184)
 9 | *****************(161)
10 | **************(138)
Комплексноста на овој метод да не надминува $O(N * M*log_2(M))$ за N записи и M насоки.*/


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    String smer;
    List<Integer> oceni;


    public Student(String index, String smer, List<Integer> oceni) {
        this.index = index;
        this.smer = smer;
        this.oceni = oceni;
    }

    public double getAverage() {
        return oceni.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    public String getIndex() {
        return index;
    }


    @Override
    public String toString() {
        return String.format("%s %.2f", index, getAverage());
    }
}

class StudentRecords {
    
    Map<String, List<Student>> grouped;
    Comparator<Student> comparator = Comparator.comparing(Student::getAverage).reversed().thenComparing(Student::getIndex);
    
    public StudentRecords() {
        grouped = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        int counter = 0;
        while (sc.hasNextLine()) {
            List<Integer> grades = new ArrayList<>();
            counter++;
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String index = parts[0];
            String smer = parts[1];
            for (int i = 2; i < parts.length; i++) {
                grades.add(Integer.parseInt(parts[i]));
            }
            Student s = new Student(index, smer, grades);
            grouped.putIfAbsent(smer, new ArrayList<>());
            grouped.get(smer).add(s);
        }
        return counter;
    }

    void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        grouped.entrySet().forEach(entry -> {
            pw.println(entry.getKey());
            entry.getValue().stream().sorted(comparator).forEach(pw::println);
        });
        pw.flush();
    }

    public void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        Map<String, Map<Integer, Integer>> distributionMap = new TreeMap<>();
        for (Map.Entry<String, List<Student>> entry : grouped.entrySet()) {
            String smer = entry.getKey();
            List<Student> students = entry.getValue();

            Map<Integer,Integer> distribution=new TreeMap<>();
            for (int i = 6; i < 11 ; i++) {
                distribution.put(i,0);
            }

            students.forEach(student -> {
                student.oceni.forEach(grade->{
                    int count=distribution.get(grade);
                    distribution.put(grade,count+1);
                });
            });

            distributionMap.put(smer,distribution);
        }

        List<Map.Entry<String, Map<Integer, Integer>>> sortedEntries = distributionMap.entrySet().stream()
                .sorted((entry1, entry2) ->
                        entry2.getValue().get(10) - entry1.getValue().get(10))
                .collect(Collectors.toList());


        sortedEntries.forEach(entry->{
            pw.println(entry.getKey());
            entry.getValue().forEach((grade,count)->{
                int repeator;
                if(count%10==0){
                    repeator=count/10;
                }else{
                    repeator=count/10+1;
                }
                pw.printf("%2d | %s(%d)\n",grade,"*".repeat(repeator),count);
            });
        });

        pw.flush();
    }
}


public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}
