/*Да се имплементира класа QuizProcessor со единствен метод Map<String, Double> processAnswers(InputStream is).
Методот потребно е од влезниот поток is да ги прочита одговорите на студентите на еден квиз. Информациите за квизовите се дадени во посебни редови и се во следниот формат:
ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An. 
каде што ID е индексот на студентот, Ci е точниот одговор на i-то прашање, а Ai е одговорот на студентот на i-то прашање. Студентот добива по 1 поен за точен одговор, а по -0.25 за секој неточен одговор. Бројот на прашања n може да биде различен во секој квиз.
Со помош на исклучоци да се игнорира квиз во кој бројот на точни одговори е различен од бројот на одговорите на студентот.
Во резултантната мапа, клучеви се индексите на студентите, а вредности се поените кои студентот ги освоил. Пример ако студентот на квиз со 6 прашања, има точни 3 прашања, а неточни 3 прашања, студентот ќе освои 3*1 - 3*0.25 = 2.25.*/



import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;

class QuizEntryCannotBeProcessed extends Exception{
    public QuizEntryCannotBeProcessed() {
        super("A quiz must have same number of correct and selected answers");
    }
}
class Quiz{
    String index;
    List<String> correctAnsw;
    List<String> answered;

    double points;
    public Quiz(String index, List<String> correctAnsw, List<String> answered) throws QuizEntryCannotBeProcessed {
       if(correctAnsw.size() != answered.size()){
           throw new QuizEntryCannotBeProcessed();
       }
        this.index = index;
        this.correctAnsw = correctAnsw;
        this.answered = answered;
        this.points=0.0;
    }

    public double calculateStudentPoints(List<String> correctAnsw, List<String> answered) {
        IntStream.range(0, correctAnsw.size()).forEach(i->{
            if(correctAnsw.get(i).equals(answered.get(i))) {
                points++;
            }
            else{
                points-=0.25;
            }
        });
        return points;
    }

    @Override
    public String toString() {
        return String.format("%s -> %.2f",index,calculateStudentPoints(correctAnsw,answered));
    }
}
class QuizProcessor{

    public static Map<String, Double> processAnswers(InputStream in) {
       //200000;C, D, D, D, A, C, B, D, D;C, D, D, D, D, B, C, D, A
        Scanner sc=new Scanner(in);

        Map<String, Double> tests=new LinkedHashMap<>();
        while (sc.hasNextLine()){
            String line=sc.nextLine();
            String [] parts=line.split(";");
            String index=parts[0];
            String [] correctAnswersParts=parts[1].split(",");
            String [] studentAnswersParts=parts[2].split(",");
            List<String>correctAnswersList=new ArrayList<>(Arrays.asList(correctAnswersParts));
            List<String>studentAnswersList=new ArrayList<>(Arrays.asList(studentAnswersParts));
            try {
                Quiz quiz=new Quiz(index,correctAnswersList,studentAnswersList);
                double points=quiz.calculateStudentPoints(correctAnswersList,studentAnswersList);
                tests.put(index, points);
            } catch (QuizEntryCannotBeProcessed e) {
                System.out.println(e.getMessage());
            }

        }
        return tests;
    }
}
public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}
