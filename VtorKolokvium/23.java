/*Да се напише класата Student во која што ќе се чуваат информации за:
    индекс на студент ФИНКИ (стринг составен од шест бројки)
    листа на поени добиени на лабораториски вежби по некој предмет ФИНКИ. По предметот се изведуваат максимум 10 лабораториски вежби.
За класата да се напише конструктор Student(String index, List<Integer> points).
Да се напише класа LabExercises во која што се чува колекција од студенти. За класата да се напишат следните методи:
    public void addStudent (Student student)- метод за додавање на нов студент во колекцијата
    public void printByAveragePoints (boolean ascending, int n) - метод којшто ќе ги печати првите n студентите сортирани според сумарните поени, а доколку се исти сумарните поени, според индексот, во растечки редослед доколку ascending е true, a во спротивно во опаѓачки.
    сумарните поени се пресметуваат како збирот на поените поделен со 10.
    public List<Student> failedStudents () - метод којшто враќа листа од студенти кои не добиле потпис (имаат повеќе од 2 отсуства), сортирани прво според индексот, а потоа според сумарните поени.
    public Map<Integer,Double> getStatisticsByYear() - метод којшто враќа мапа од просекот од сумарните поени на студентите според година на студирање. Да се игнорираат студентите кои не добиле потпис.
*/



import java.util.*;
import java.util.stream.Collectors;

class Student{
    String index;
    List<Integer> points=new ArrayList<>(11);
    static double COUNT_OF_LAB=10.0;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }
    public double getSummaryPoints(){
        return points.stream().mapToInt(i->i).sum()/COUNT_OF_LAB;
    }

    public String hasSignature(){
        String sign;
        if(points.size()>=8){
            sign="YES";
        }else{
            sign="NO";
        }
        return sign;
    }
    public int getYearOfStudies(){
      return 20-Integer.parseInt(index.substring(0,2));
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f",index, hasSignature(), getSummaryPoints());
    }
}

class LabExercises{
    List<Student> students;

    public LabExercises() {
        students=new ArrayList<>();
    }

    public void addStudent (Student student){
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n){
        Comparator<Student> comparator=Comparator.comparing(Student::getSummaryPoints).thenComparing(Student::getIndex);

        if(!ascending){
            comparator=comparator.reversed();
        }

        students.stream().sorted(comparator).limit(n).forEach(System.out::println);
    }

    public List<Student> failedStudents (){
        Comparator<Student> comparator=Comparator.comparing(Student::getIndex).thenComparing(Student::getSummaryPoints);
    return students.stream().filter(s->s.hasSignature().equals("NO")).sorted(comparator).collect(Collectors.toList());
    }

    public Map<Integer,Double> getStatisticsByYear(){

        return students.stream()
                .filter(s->s.hasSignature().equals("YES"))
                .collect(Collectors.groupingBy(
                Student::getYearOfStudies,
                Collectors.averagingDouble(Student::getSummaryPoints)
                ));

    }
}
public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
