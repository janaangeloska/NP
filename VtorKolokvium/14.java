/*Да се напише класа за автомобил Car во која се чува:
    производител
    модел
    цена
    моќност.
Да се имплементира конструктор со следните аргументи Car(String manufacturer, String model, int price, float power).
Потоа да се напише класа CarCollection во која се чува колекција од автомобили. Во оваа класа треба да се имплментираат следните методи:
    public void addCar(Car car) - додавање автомобил во колекцијата
    public void sortByPrice(boolean ascending) - подредување на колекцијата по цената на автомобилот (во растечки редослед ако аргументот ascending е true, во спротивно, во опаѓачки редослед). Ако цената на автомобилите е иста, сортирањето да се направи според нивната моќноста.
    public List<Car> filterByManufacturer(String manufacturer) - враќа листа со автомобили од одреден производител (споредбата е според името на производителот без да се води сметка за големи и мали букви во името). Автомобилите во оваа листата треба да бидат подредени според моделот во растечки редослед.
    public List<Car> getList() - ја враќа листата со автомобили од колекцијата.
*/

// package kolokviumski2_ispitni._14;

import java.util.*;
import java.util.stream.Collectors;

class Car{
    String manufacturer;
    String model;
    int price;
    float power;
    Map<String,String> car;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }
    public String getManufacturerToLowerCase() {
        return manufacturer.toLowerCase();
    }

    @Override
    public String toString() {
        return String.format("%s %s (%dKW) %d",manufacturer,model,(int)power,price);
    }
}
class CarCollection{
    List <Car> cars;
    Comparator <Car> comparator=Comparator.comparing(Car::getPrice).thenComparing(Car::getPower);
    Comparator <Car> comparator2=Comparator.comparing(Car::getModel);

    public CarCollection() {
        cars = new ArrayList<>();
    }

    public void addCar(Car car){
        cars.add(car);
    }

    public void sortByPrice(boolean ascending){
        List<Car> sortedCars;
        if(ascending){
            sortedCars=cars.stream().sorted(comparator).collect(Collectors.toList());
        }else{
            sortedCars=cars.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        }
        cars=sortedCars;
    }

    public List<Car> filterByManufacturer(String manufacturer){
        return cars.stream().filter(i->i.getManufacturerToLowerCase().contains(manufacturer.toLowerCase())).sorted(comparator2).collect(Collectors.toList());
    }


    public List<Car> getList(){
        return cars;
    }
}

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}
