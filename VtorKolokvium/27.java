/*Да се имплементира класа Names со следните методи:
    public void addName(String name) - додавање на име
    public void printN(int n) - ги печати сите имиња кои се појавуваат n или повеќе пати, подредени лексикографски според името, на крајот на зборот во загради се печати бројот на појавувања, а по него на крај бројот на уникатни букви во зборот (не се прави разлика на големи и мали)
    public String findName(int len, int x) - го враќа името кое со наоѓа на позиција x (почнува од 0) во листата од уникатни имиња подредени лексикографски, по бришење на сите имиња со големина поголема или еднаква на len. Позицијата x може да биде поголема од бројот на останати имиња, во тој случај се продожува со броење од почетокот на листата. Пример за листа со 3 имиња A, B, C, ако x = 7, се добива B. A0, B1, C2, A3, B4, C5, A6, B7.
*/

import java.util.*;
import java.util.stream.Collectors;

class Name {
    String name;
    int occurrences = 0;
    Set<String> names;

    public Name(String name) {
        this.name = name;
        names = new TreeSet<>();
    }

    public void update() {
        occurrences++;
        names.add(name);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return name.length();
    }

    public int getUniqueLetters(String name) {
        name = name.toLowerCase();
        Set<Character> uniques = new HashSet<>();
        for (char letter : name.toCharArray()) {
            uniques.add(letter);
        }
        return (int) uniques.stream().filter(Objects::nonNull).count();
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, occurrences, getUniqueLetters(name));
    }
}

class Names {

    Map<String, Name> names;
    Comparator<Name> compareNames = Comparator.comparing(Name::getName);

    public Names() {
        names = new HashMap<>();
    }

    public void addName(String name) {
        names.putIfAbsent(name, new Name(name));
        names.get(name).update();
    }

    public void printN(int n) {
        names.values().stream().filter(i -> i.occurrences >= n).sorted(compareNames).forEach(System.out::println);
    }

    public String findName(int len, int x) {
        List<Name> collect = names.values().stream().filter(i -> i.getSize() < len).sorted(compareNames).collect(Collectors.toList());
        boolean flag = true;
        while (flag) {
            if (collect.size() <= x) {
                x -= collect.size();
            } else {
                flag = false;
            }
        }
        return collect.get(x).name;
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
