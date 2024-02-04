/*Да се напише генеричка метода entriesSortedByValues за сортирање на елементи (парови од клуч и вредност) на една мапа според вредноста во опаѓачки редослед. Доколку постојат две или повеќе исти вредности, да се задржи редоследот дефиниран во мапата. Сортираните елементи на мапата да бидат да бидат вратени како SortedSet<Map.Entry<, >>.*/

import java.util.*;

public class MapSortingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> l = readMapPairs(scanner);
        if(n==1){
            Map<String, Integer> map = new HashMap<>();
            fillStringIntegerMap(l, map);
            SortedSet<Map.Entry<String, Integer>> s = entriesSortedByValues(map);
            //added print for the map
            System.out.println(map);
            System.out.println(s);
        } else {
            Map<Integer, String> map = new HashMap<>();
            fillIntegerStringMap(l, map);
            SortedSet<Map.Entry<Integer, String>> s = entriesSortedByValues(map);
            //added print for the map
            System.out.println(map);
            System.out.println(s);
        }

    }

    private static List<String> readMapPairs(Scanner scanner) {
        String line = scanner.nextLine();
        String[] entries = line.split("\\s+");
        return Arrays.asList(entries);
    }

    static void fillStringIntegerMap(List<String> l, Map<String,Integer> map) {
        l.stream()
                .forEach(s -> map.put(s.substring(0, s.indexOf(':')), Integer.parseInt(s.substring(s.indexOf(':') + 1))));
    }

    static void fillIntegerStringMap(List<String> l, Map<Integer, String> map) {
        l.stream()
                .forEach(s -> map.put(Integer.parseInt(s.substring(0, s.indexOf(':'))), s.substring(s.indexOf(':') + 1)));
    }

    public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>>  entriesSortedByValues(Map<K, V> map){
        SortedSet<Map.Entry<K,V>> set=new TreeSet<>(
                (o1, o2) -> {
                    if (o1.getValue().compareTo(o2.getValue()) != 0) {
                        return -(o1.getValue().compareTo(o2.getValue()));
                    } else {
                        return 1;
                    }
                }
        );
        set.addAll(map.entrySet());
        return set;
   }
}
