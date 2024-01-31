/*За потребите на модулот за онлајн плаќања на системот iknow потребно е да напишете класа OnlinePayments со следните методи:
    default конструктор
    void readItems (InputStream is) - метод за вчитување на сите ставки кои се платени преку модулот. Секоја ставка е во нов ред и е во следниот формат STUDENT_IDX ITEM_NAME PRICE.
    void printStudentReport (String index, OutputStream os) - метод за печатење на извештај за студентот со индекс id. Во извештајот треба да се испечати нето износот на сите платени ставки, наплатената банкарска провизија, вкупниот износ кој е наплатен од студентите, како и нумерирана листа од сите ставки кои се платени од студентите сортирани во опаѓачки редослед според цената.
        Провизијата се пресметува врз вкупниот износ на ставките кои студентот ги плаќа и изнесува 1.14% (но најмалку 3 денари, а најмногу 300). Децималните износи се заокрузуваат со Math.round.
*/

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class  Item implements Comparable<Item>{
    String index;
    String item;
    int price;

    public Item(String index, String item, int price) {
        this.index = index;
        this.item = item;
        this.price = price;
    }

    public String getIndex() {
        return index;
    }

    public String getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(o.price,this.price);
    }

    @Override
    public String toString() {
        return String.format("%s %d", item, price);
    }
}

class Student{
    String index;
    List<Item> items;

    public Student(String index) {
        this.index = index;
        items=new ArrayList<>();
    }
    public void addItem(Item item){
        items.add(item);
    }

    public int net(){
        return items.stream().mapToInt(i->i.getPrice()).sum();
    }
    public int fee(){
        int provizija= (int) Math.round(net()*0.0114);
        if(provizija<=3){
            return 3;
        }else if(provizija>=300){
            return 300;
        }else {
            return provizija;
        }
    }

    public int total(){
        return net()+fee();
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        int counter=1;
        sb.append(String.format("Student: %s Net: %d Fee: %d Total: %d\n",index,net(),fee(),total()));
        sb.append("Items:\n");
        List <Item> sortedItems=items.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < items.size()-1; i++) {
            sb.append(counter).append(". ");
            sb.append(sortedItems.get(i).toString());
            sb.append("\n");
            counter++;
        }
        sb.append(counter).append(". ");
        sb.append(sortedItems.get(sortedItems.size()-1).toString());
        return sb.toString();
    }
}
class OnlinePayments{
    Map<String, Student> payments;
    Comparator<Item> comparator=Comparator.comparing(Item::getPrice).reversed();

    public OnlinePayments() {
        payments=new HashMap<>();
    }
    void readItems (InputStream is){
        Scanner sc=new Scanner(is);
        while (sc.hasNextLine()){
            String line=sc.nextLine();
            String[]parts=line.split(";");
            String index=parts[0];
            String item=parts[1];
            int price=Integer.parseInt(parts[2]);
            payments.putIfAbsent(index,new Student(index));
            payments.get(index).addItem(new Item(index,item,price));
        }
    }

    void printStudentReport (String index, OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        Student s=payments.get(index);
        if (s == null) {
            pw.println(String.format("Student %s not found!", index));
        }else{
            pw.println(s);
        }
        pw.flush();
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}
