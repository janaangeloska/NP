/*Да се имплементира класа Discounts за обработка на информации за цени и цени на попуст на одредени производи во неколку продавници (објекти од класа Store). Потребно е да се имплементираат следните методи:
    public int readStores(InputStream inputStream) - метод за вчитување на податоците за продавниците и цените на производите. Податоците за секоја продавница се во посебен ред во формат [ime] [cena_na_popust1:cena1] [cena_na_popust2:cena2] ... (погледнете пример). Методот враќа колку продавници се вчитани.
    public List<Store> byAverageDiscount() - метод кој враќа листа од 3-те продавници со најголем просечен попуст (просечна вредност на попустот за секој производ од таа продавница). Попустот (намалувањето на цената) е изразен во цел број (проценти) и треба да се пресмета од намалената цена и оригиналната цена. Ако две продавници имаат ист попуст, се подредуваат според името лексикографски.
    public List<Store> byTotalDiscount() - метод кој враќа листа од 3-те продавници со намал вкупен попуст (сума на апсолутен попуст од сите производи). Апсолутен попуст е разликата од цената и цената на попуст. Ако две продавници имаат ист попуст, се подредуваат според името лексикографски.
Дополнително за класата Store да се имплементира стринг репрезентација, односно методот:
public String toString() кој ќе враќа репрезентација во следниот формат:
[Store_name]
Average discount: [заокружена вредност со едно децимално место]%
Total discount: [вкупен апсолутен попуст]
[процент во две места]% [цена на попуст]/[цена]
...
при што продуктите се подредени според процентот на попуст (ако е ист, според апсолутниот попуст) во опаѓачки редослед. */



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.*;

class Price{
    int discountedPrice;
    int originalPrice;

    public Price(int discountedPrice, int originalPrice) {
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }
    public int discountPercentage(){
        return (int)Math.floor(100.0 - (100.0 / originalPrice * discountedPrice));
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d",discountPercentage(),discountedPrice,originalPrice);
    }
}
class Store{
    String name;
    List<Price> prices;
    Comparator <Price> comparing=Comparator.comparing(Price::discountPercentage).thenComparing(Price::getOriginalPrice).reversed();

    public Store(String name, List<Price> prices) {
        this.name = name;
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public List<Price> getPrices() {
        return prices;
    }
    public double average(){
        return prices.stream().mapToInt(Price::discountPercentage).average().orElse(0);
    }
    public int absolute(){
        return prices.stream().mapToInt(i->Math.abs(i.originalPrice-i.discountedPrice)).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        String getDiscounts=prices.stream().sorted(comparing).map(i->String.format("%s",i)).collect(Collectors.joining("\n"));
        sb.append(name).append("\n");
        sb.append(String.format("Average discount: %.1f",average())).append("%").append("\n");
        sb.append(String.format("Total discount: %d",absolute())).append("\n");
        sb.append(getDiscounts);
        return sb.toString();
    }
}
class Discounts{
    List<Store> items;
    Comparator<Store> comparator=Comparator.comparing(Store::average).reversed();
   Comparator<Store>comparator2=Comparator.comparing(Store::absolute);
    public Discounts() {
        items=new ArrayList<>();
    }
    public int readStores(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        int counter=0;
        while (sc.hasNextLine()){
            counter++;
            String line=sc.nextLine();
            String [] parts=line.split("\\s+");
            String storeName=parts[0];
            List<Price> prices=new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String [] price=parts[i].split(":");
                prices.add(new Price(Integer.parseInt(price[0]),Integer.parseInt(price[1])));
            }
            items.add(new Store(storeName,prices));
        }
        return counter;
    }
    public List<Store> byAverageDiscount(){
        List<Store> top3=new ArrayList<>();
        top3=items.stream().sorted(comparator).limit(3).collect(Collectors.toList());
        return top3;
    }

    public List<Store> byTotalDiscount(){
        List<Store> top3=new ArrayList<>();
        top3=items.stream().sorted(comparator2).limit(3).collect(Collectors.toList());
        return top3;
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
