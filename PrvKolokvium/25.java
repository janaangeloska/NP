/*Да се дефинира класа ShoppingCart за репрезентација на една потрошувачка кошничка во која може да се наоѓаат ставки од 2 типа (ставка која содржи продукт кој се купува во целост, или ставка која содржи продукт кој се купува на грам).
За класата ShoppingCart да се имплементираат следните методи:
    конструктор
    void addItem(String itemData) - метод за додавање на ставка во кошничката. Податоците за ставката се дадени во текстуална форма и може да бидат во следните два формати согласно типот на ставката:
        WS;productID;productName;productPrice;quantity (quantity е цел број, productPrice се однесува на цена на 1 продукт)
        PS;productID;productName;productPrice;quantity (quantity е децимален број - во грамови, productPrice се однесува на цена на 1 кг продукт)
            Со помош на исклучок од тип InvalidOperationException да се спречи додавање на ставка со quantity 0.
    void printShoppingCart(OutputStream os) - метод за печатење на кошничката на излезен поток. Потребно е да се испечатат сите ставки од кошничката подредени според вкупната цена во опаѓачки редослед. Вкупната цена е производ на цената на продуктот кој е во ставката и квантитетот кој е купен по таа цена.
    void blackFridayOffer(List<Integer> discountItems, OutputStream os) - метод којшто ќе ја намали цената за 10% на сите продукти чиј што productID се наоѓа во листата discountItems. Потоа, треба да се испечати извештај за вкупната заштеда на секоја ставка каде има продукт на попуст (види тест пример). Да се фрли исклучок од тип InvalidOperationException доколку листата  со продукти на попуст е празна.
Напомена: Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети. Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети со најмногу 80% од поените.*/


import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(String.format("The quantity of the product with id %s can not be 0.",message));
    }
    InvalidOperationException() {
        super("There are no products with discount.");
    }
}
abstract class Stavka implements Comparable<Stavka>{
    String type;
    String id;
    String name;
    double price;
    public Stavka(String type, String id, String name,double price) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.price=price;
    }

    public String getId() {
        return id;
    }
    abstract double productPrice();

abstract public double discount();
    @Override
    public int compareTo(Stavka o) {
        return Double.compare(this.productPrice(),o.productPrice());
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f",id,productPrice());
    }
}
//WS
class Celost extends Stavka{
    double quantity;
    public Celost(String type, String id, String name, double price, double quantity) {
        super(type, id, name,price);
        this.quantity=quantity;
    }

    @Override
    double productPrice() {
        return price*quantity;
    }

    @Override
    public double discount() {
        return productPrice()*0.1;
    }
}
//PS
class Gramazha extends Stavka{
    double quantity;

    public Gramazha(String type, String id, String name,double price,double quantity) {
        super(type, id, name,price);
        this.quantity=quantity;
    }

    @Override
    double productPrice() {
        return price*(quantity/1000);
    }

    @Override
    public double discount() {
        return productPrice()*0.1;
    }
}
class ShoppingCart{
    ArrayList<Stavka> stavki;

    public ShoppingCart() {
        stavki=new ArrayList<>();
    }
    void addItem(String itemData) throws InvalidOperationException {
        String[]parts=itemData.split(";");
        String type=parts[0];
        String id=parts[1];
        String name=parts[2];
        double price =Double.parseDouble(parts[3]);
        double quantity=Double.parseDouble(parts[4]);
        if(quantity==0){
            throw new InvalidOperationException(id);
        }
        if(type.equals("WS")){
            stavki.add(new Celost(type,id,name,price,quantity));
        }else{
            stavki.add(new Gramazha(type,id,name,price,quantity));
        }
    }
    
    void printShoppingCart(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        stavki.stream().sorted(Comparator.reverseOrder()).forEach(i->pw.println(i));
        pw.flush();
    }
    
    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if(discountItems.isEmpty()){
            throw new InvalidOperationException();
        }
        PrintWriter pw = new PrintWriter(os);
        ArrayList<Stavka> discounted = new ArrayList<>();

        for(Stavka stavka : stavki) {
            for(Integer discountItem : discountItems) {
                if (stavka.getId().equals(String.valueOf(discountItem))) {
                    discounted.add(stavka);
                }
            }
        }

        for(Stavka stavka : discounted) {
            pw.println(String.format("%s - %.2f", stavka.getId(), stavka.discount()));
        }

        pw.flush();
    }
}


public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
