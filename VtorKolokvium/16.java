/*Да се имплементира генеричка класа за блок контејнер BlockContainer. Контејнерот треба да има блоковска структура, со тоа што секој блок содржи N елементи. Контејнерот треба да ги задоволува следните услови:
    константно време на пристап до секој блок $O(1)$
    логаритамско време на пристап до елементите во блокот $O(log N)$
    елементите во секој блок треба да бидат сортирани.
Класата треба да ги имплементира следните методи:
    public BlockContainer(int n) - конструктор со еден аргумент, максималниот број на елементи во блокот
    public void add(T a) - метод за додавање елемент во последниот блок од контејнерот (ако блокот е полн, се додава нов блок)
    public boolean remove(T a) - метод за бришње на елемент од последниот блок (ако се избришат сите елементи од еден блок, тогаш и блокот се брише)
    public void sort() - метод за сортирање на сите елементи во контејнерот
    public String toString() - препокривање на методот да враќа String во следниот формат: пример: [7, 8, 9],[1, 2, 3],[5, 6, 12],[4, 10, 8]
*/


import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>{
    List<Set<T>> container;
    int n;

    public BlockContainer(int n) {
        this.n=n;
        container=new ArrayList<Set<T>>();
    }
    public void add(T a){
       if(container.isEmpty()){
           Set<T> set=new TreeSet<T>();
           set.add(a);
           container.add(set);
       }else{
           Set<T> set=container.get(container.size()-1);
           if(set.size()>=n){
               set=new TreeSet<T>();
               set.add(a);
               container.add(set);
           }else{
               set.add(a);
           }
       }
    }
    public boolean remove(T a){
        //ko da go nema delov vo kodov implementirano
        boolean flag=false;
        Set<T> lastSet = container.get(container.size() - 1);
        lastSet.remove(a);
        if(lastSet.isEmpty()){
            container.remove(lastSet);
        }
        return flag;
    }

    public void sort(){
        List<T> collect = container.stream().flatMap(i -> i.stream()).sorted().collect(Collectors.toCollection(() -> new ArrayList<T>()));
        container.clear();
        collect.forEach(i->add(i));
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < container.size(); i++) {
            sb.append(container.get(i).toString());
            if(i<container.size()-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}



