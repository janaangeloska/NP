/*Да се напише метод кој ќе прима еден цел број и ќе ја печати неговата репрезентација како Римски број.
Пример
    Aко ако се повика со парамететар 1998, излезот треба да биде MCMXCVIII.
*/


import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    final static TreeMap<Integer, String> map=new TreeMap<>();
    static {
        map.put(1000,"M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }
    public static String toRoman(int n) {
        // your solution here
        int key=map.floorKey(n);
        if(n==key){
            return map.get(n);
        }
        return map.get(key)+toRoman(n-key);
    }

}
