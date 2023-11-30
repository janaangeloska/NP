/*Да се дефинира генеричка класа за правило (Rule) во која ќе се чуваат имплементации на интерфејсите Predicate и Function. Генеричката класа треба да има два генерички параметри - еден за влезниот тип (типот на објектите кои се спроведуваат низ правилото) и еден за излезниот тип (типот на објектите кои се резултат од правилото).
Во класата Rule да се дефинира метод apply кој прима еден аргумент input од влезниот тип, а враќа објект од генеричката класата Optional со генерички параметар ист како излезниот тип на класата Rule. Методот apply треба да врати Optional објект пополнет со резултатот добиен од Function имплементацијата применет на аргументот input само доколку е исполнет предикатот од правилото Rule. Доколку предикатот не е исполнет, методот apply враќа празен Optional.
Дополнително, да се дефинира класа RuleProcessor со еден генерички статички метод process кој ќе прими два аргументи:
    Листа од влезни податоци (објекти од влезниот тип)
    Листа од правила (објекти од класа Rule)
Методот потребно е врз секој елемент од листата на влезни податоци да го примени секое правило од листата на правила и на екран да го испечати резултатот од примената на правилото (доколку постои), а во спротивно да испечати порака Condition not met.
Во главната класа на местата означени со TODO да се дефинираат потребните објекти од класата Rule. Да се користат ламбда изрази за дефинирање на објекти од тип Predicate и Function.
Напомена: Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети. Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети со најмногу 80% од поените.*/

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

class Rule<IN,OUT>{
    Predicate<IN> predicate;
    Function<IN,OUT> function;

    //ako e tocen predikatot pretvori go vlezniot input vo
    //izlezniot objekt od tipot out so maper fjata shto e tuka


    public Rule(Predicate<IN> predicate, Function<IN, OUT> function) {
        this.predicate = predicate;
        this.function = function;
    }
        Optional<OUT>apply(IN input){
    if(predicate.test(input)){
        return Optional.of(function.apply(input));
    }
    else{
        return Optional.empty();
    }
    }

}

class RuleProcessor{
    static <IN, OUT> void process(List<IN> inputs, List<Rule<IN,OUT>> rules){
//        inputs.forEach(input->rules.forEach(rule->rule.apply(input)));
        for (IN input:
             inputs) {
            System.out.println(String.format("Input: %s",input.toString()));
            for (Rule<IN, OUT> rule:
                 rules) {
                Optional<OUT> result = rule.apply(input);
            if(result.isPresent()){
                System.out.println("Result: "+result.get());
            }else{
                System.out.println("Condition not met");
            }
            }
        }
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

/*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
*/
            rules.add(new Rule<>(
                    str->str.contains("NP"),
                    str->str.indexOf("NP")
            ));



            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            rules.add(new Rule<>(
                    str->str.startsWith("NP"),
                    str->str.length()
            ));

            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
rules.add(new Rule<>(
        student -> student.grades.size()>=3,
        student -> student.grades.stream().mapToDouble(i->i).max().getAsDouble()
));

            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0

rules.add(new Rule<>(
   student -> student.id.startsWith("20"),
   student -> student.grades.stream().mapToDouble(i->i).average().orElse(5.0)
));
            List<Student> students = new ArrayList<>();
            while (sc.hasNext()){
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}
