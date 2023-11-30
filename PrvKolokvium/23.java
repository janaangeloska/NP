/*Да се дефинира класа Quiz за репрезентација на еден квиз во кој може да се наоѓаат повеќе прашања од 2 типа (true/false прашање или прашање со избор на еден точен одговор од 5 понудени одговори A/B/C/D/E). За класата Quiz да се имплементираат следните методи:
    конструктор
    void addQuestion(String questionData) - метод за додавање на прашање во квизот. Податоците за прашањето се дадени во текстуална форма и може да бидат во следните два формати согласно типот на прашањето:
        TF;text;points;answer (answer може да биде true или false)
        MC;text;points;answer (answer е каракатер кој може да ја има вредноста A/B/C/D/E)
            Со помош на исклучок од тип InvalidOperationException да се спречи додавање на прашање со повеќе понудени одговори во кое како точен одговор се наоѓа некоја друга опција освен опциите A/B/C/D/E.
    void printQuiz(OutputStream os) - метод за печатење на квизот на излезен поток. Потребно е да се испечатат сите прашања од квизот подредени според бројот на поени на прашањата во опаѓачки редослед.
    void answerQuiz (List<String> answers, OutputStream os) - метод којшто ќе ги одговори сите прашања од квизот (во редоследот во којшто се внесени) со помош на одговорите од листата answers. Методот треба да испечати извештај по колку поени се освоени од секое прашање и колку вкупно поени се освоени од квизот (види тест пример). Да се фрли исклучок од тип InvalidOperationException доколку бројот на одговорите во `answers е различен од број на прашања во квизот.
        За точен одговор на true/false прашање се добиваат сите предвидени поени, а за неточен одговор се добиваат 0 поени
        За точен одговор на прашање со повеќе понудени одговори се добиваат сите предвидени поени, а за неточен одговор се добиваат негативни поени (20% од предвидените поени).
Напомена: Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети. Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети со најмногу 80% од поените.*/

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

abstract class Question implements Comparable<Question> {
    String text;
    int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    @Override
    public int compareTo(Question o) {
        return Integer.compare(this.points, o.points);
    }

    abstract float answer(String studentAnswer);
}

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

class QuestionFactory {
    static List<String> ALLOWED_ANSWERS = Arrays.asList("A", "B", "C", "D", "E");

    static Question create(String line) throws InvalidOperationException {
        String[] parts = line.split(";");
        String type = parts[0];
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        String answer = parts[3];
        boolean answer2 = Boolean.parseBoolean(parts[3]);
        if (type.equals("MC")) {
            if (!ALLOWED_ANSWERS.contains(answer)) {
                throw new InvalidOperationException(String.format("%s is not allowed option for this question", answer));
            }
            return new MultipleChoiceQuestion(text, points, answer);
        } else { //TF
            return new TrueFalseQuestion(text, points, answer2);
        }
    }
}


class MultipleChoiceQuestion extends Question {
    String choice;

    public MultipleChoiceQuestion(String text, int points, String choice) {
        super(text, points);
        this.choice = choice;
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", text, points, choice);
    }

    @Override
    float answer(String studentAnswer) {
        return choice.equals(studentAnswer) ? points : (points * -0.2f);
    }
}

class TrueFalseQuestion extends Question {
    boolean answer;

    public TrueFalseQuestion(String text, int points, boolean answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", text, points, answer);
    }

    @Override
    float answer(String studentAnswer) {
        return answer == Boolean.parseBoolean(studentAnswer) ? points : 0.0f;
    }
}


class Quiz {

    List<Question> questions;

    Quiz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(String s) {
        try {
            questions.add(QuestionFactory.create(s));
        } catch (InvalidOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printQuiz(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        questions.stream().sorted(Comparator.reverseOrder()).forEach(q -> pw.println(q));

        pw.flush();
    }

    public void answerQuiz(List<String> answers, PrintStream out) throws InvalidOperationException {
        if(questions.size()!=answers.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        PrintWriter pw = new PrintWriter(out);
float sum=0;
        for (int i = 0; i < answers.size(); i++) {
            float points=questions.get(i).answer(answers.get(i));
            pw.println(String.format("%d. %.2f",i+1,points));
        sum+=points;
        }
        pw.println(String.format("Total points: %.2f",sum));
        pw.flush();
    }
}


public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
