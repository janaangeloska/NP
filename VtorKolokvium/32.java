/*Да се дефинира класа MapOps со единствен генерички статички метод merge кој ќе прими две мапи (прва и втора) и стратегија за спојување на вредностите на мапите (објект од тип MergeStrategy), а како резултат ќе врати нова мапа во која ќе бидат споени двете мапи предадени како првите два аргументи на методот со употреба на стратегијата предадена како трет аргумент.
Мапите се спојуваат на следниот начин:
    Ако определен клуч од втората мапа постои во првата мапа, резултатот ќе биде нов елемент/пар во резултантната мапа од истиот тој клуч и вредност која се добива со спојување на вредностите од првата и втората мапа согласно логиката во стратегијата за спојување. 
    Сите клучеви кои се јавуваат точно еднаш во првата и втората мапа, се додаваат во резултантната мапа со нивната вредност без промени.
Влезните мапи имаат ист тип на клуч и ист тип на вредност. Клучевите во мапите треба да бидат споредливи објекти. 
Во резултантната мапа, паровите клуч-вредност да се сортирани во растечки редослед според клучот.
Да се имплементираат потребните 4 стратегии за спојување согласно описот во TODO коментарот во main класата. */

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface MergeStrategy<V>{
    V merge(V v1,V v2);
}
class MapOps{


    public static <K, V> Map<K,V> merge(Map<K, V> mapLeft, Map<K, V> mapRight, MergeStrategy<V> mergeStrategy) {
        Map<K,V> mergedMap=new TreeMap<>(mapLeft);
        mapRight.forEach((k,v)->mergedMap.merge(k,v,mergeStrategy::merge));
        return mergedMap;
    }
}

public class GenericMapTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Mergeable integers
            Map<Integer, Integer> mapLeft = new HashMap<>();
            Map<Integer, Integer> mapRight = new HashMap<>();
            readIntMap(sc, mapLeft);
            readIntMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two Integer objects into a new Integer object which is their sum
            MergeStrategy <Integer> mergeStrategy = (v1, v2) -> v1+v2;

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 2) { // Mergeable strings
            Map<String, String> mapLeft = new HashMap<>();
            Map<String, String> mapRight = new HashMap<>();
            readStrMap(sc, mapLeft);
            readStrMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two String objects into a new String object which is their concatenation
            MergeStrategy <String> mergeStrategy = (v1, v2) -> v1.concat(v2);

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 3) {
            Map<Integer, Integer> mapLeft = new HashMap<>();
            Map<Integer, Integer> mapRight = new HashMap<>();
            readIntMap(sc, mapLeft);
            readIntMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two Integer objects into a new Integer object which will be the max of the two objects
            MergeStrategy <Integer> mergeStrategy = (v1, v2) -> Math.max(v1,v2);

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 4) {
            Map<String, String> mapLeft = new HashMap<>();
            Map<String, String> mapRight = new HashMap<>();
            readStrMap(sc, mapLeft);
            readStrMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two String objects into a new String object which will mask the occurrences of the second string in the first string

            MergeStrategy<String> mergeStrategy = (v1,v2)->v1.replace(v2,"*".repeat(v2.length()));
            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        }
    }

    private static void readIntMap(Scanner sc, Map<Integer, Integer> map) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            int k = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            map.put(k, v);
        }
    }

    private static void readStrMap(Scanner sc, Map<String, String> map) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            map.put(parts[0], parts[1]);
        }
    }

    private static void printMap(Map<?, ?> map) {
        map.forEach((k, v) -> System.out.printf("%s -> %s%n", k.toString(), v.toString()));
    }
}
