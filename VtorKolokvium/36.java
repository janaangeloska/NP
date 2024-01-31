/*Да се имплементира класа DeliveryApp која ќе моделира една апликација за нарачки и достава на храна од ресторани. Во класата да се имплементираат следните методи:
    Конструктор DeliveryApp (String name)
    Метод void registerDeliveryPerson (String id, String name, Location currentLocation) - методот за регистрирање на слободен доставувач кој сака да работи за апликацијата.
    Метод void addRestaurant (String id, String name, Location location) - метод за додавање на ресторан кој сака да овозможи достава на ставките од своето мени
    Метод void addUser (String id, String name) - метод за регистрирање на корисник кој сака да ја користи апликацијата за нарачка и достава на храна
    Метод void addAddress (String id, String addressName, Location location) - метод за додавање на адреса на корисникот со ИД id. Еден корисник може да има повеќе адреси (пр. Дома, работа и сл.)
    метод void orderFood(String userId, String userAddressName, String restaurantId, float cost) - метод за нарачка на храна на корисникот со ID userID на неговата адреса userAddressName од ресторантот со ID restaurantId.
        При процесирање на нарачката потребно е прво да се најде доставувач кој ќе ја достави нарачката до клиентот. Нарачката се доделува на доставувачот кој е најблиску до ресторанот. Во случај да има повеќе доставувачи кои се најблиску до ресторанот - се избира доставувачот со најмалку извршени достави досега.
        По доделување на нарачката на определен доставувач, се менува неговата моментална локација во локацијата на клиентот кому му се доставува нарачката.
        Доставувачот заработува од нарачката така што добива 90 денари за секоја нарачка, и дополнителни 10 денари на секои10 единици растојание од ресторанот до клиентот (пр. ако растојанието е 35 единици = 90+3х10 = 120)
    метод void printUsers() - метод кој ги печати сите корисници на апликацијата сортирани во опаѓачки редослед според потрошениот износ за нарачка на храна преку апликацијата
    метод void printRestaurants() - метод кој ги печати сите регистрирани ресторани во апликацијата, сортирани во опаѓачки редослед според просечната цена на нарачките наплатени преку апликацијата
    метод void printDeliveryPeople() - метод кој ги печати сите регистрирани доставувачи сортирани во опачки редослед според заработениот износ од извршените достави.
*/

import java.util.*;

class Address{
    String name;
    Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
class User{
    String id;
    String name;
    Map<String,Address> addresses;
    List<Float> moneySpent;

    public double totalMoneySpent(){
        return moneySpent.stream().mapToDouble(i->i).sum();
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        addresses=new HashMap<>();
        moneySpent=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public void processOrder(float cost){
        moneySpent.add(cost);
    }

    public double averageMoneySpent() {
        if (moneySpent.isEmpty()) {
            return 0;
        } else {
            return totalMoneySpent() / moneySpent.size();
        }
    }
    @Override
    public String toString() {
        //ID: 1 Name: Morino Total orders: 1 Total amount earned: 450.00 Average amount earned: 450.00
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id,name,moneySpent.size(),totalMoneySpent(),averageMoneySpent());
    }

    public void addAddress(String addressName, Location location) {
    addresses.put(addressName,new Address(addressName,location));
    }
}

class DeliveryPerson{
    String id;
    String name;
    Location currentLocation;

    List<Float> moneyEarned;

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;

        moneyEarned=new ArrayList<>();
    }
    public double totalMoneyEarned(){
        return moneyEarned.stream().mapToDouble(i->i).sum();
    }

    public double averageMoneyEarned() {
        if (moneyEarned.isEmpty()) {
            return 0;
        } else {
            return totalMoneyEarned() / moneyEarned.size();
        }
    }
    public int compareDistanceToRestaurant(DeliveryPerson other,Location restaurantLocation){
        int currDistance=currentLocation.distance(restaurantLocation);
        int otherDistance=other.currentLocation.distance(restaurantLocation);
        if(currDistance==otherDistance){
            return Integer.compare(this.moneyEarned.size(),other.moneyEarned.size());
        }else{
            return currDistance-otherDistance;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
    public void processOrder(int distance, Location location){
        currentLocation=location;
        moneyEarned.add((float) (90+(distance/10)*10));

    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id,name,moneyEarned.size(),totalMoneyEarned(),averageMoneyEarned());
    }
}

class Restaurant{
    String id;
    String name;
    Location location;
    List<Float> moneyEarned;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;

        moneyEarned=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public double totalMoneyEarned(){
        return moneyEarned.stream().mapToDouble(i->i).sum();
    }


    public void processOrder(float cost){
        moneyEarned.add(cost);
    }
    public double averageMoneyEarned() {
        if (moneyEarned.isEmpty()) {
            return 0;
        } else {
            return totalMoneyEarned() / moneyEarned.size();
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
               id, name,moneyEarned.size(), totalMoneyEarned(),averageMoneyEarned());
    }
}

class DeliveryApp{
    String appName;
    Map<String,User> users;
    Map<String,DeliveryPerson> deliveryPerson;
    Map<String,Restaurant> restaurant;

    public DeliveryApp(String appName) {
        this.appName = appName;

        users=new HashMap<>();
        deliveryPerson=new HashMap<>();
        restaurant=new HashMap<>();
    }
    void addRestaurant (String id, String name, Location location){
        restaurant.put(id,new Restaurant(id,name,location));
    }

    void addUser (String id, String name){
        users.put(id,new User(id,name));
    }
    void registerDeliveryPerson (String id, String name, Location currentLocation){
        deliveryPerson.put(id,new DeliveryPerson(id,name,currentLocation));
    }


    void addAddress (String id, String addressName, Location location){
    users.get(id).addAddress(addressName,location);
    }

    void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        User user=users.get(userId);
        Address address=user.getAddresses().get(userAddressName);
        Restaurant restaurant1=restaurant.get(restaurantId);

        DeliveryPerson deliveryPerson1=deliveryPerson.values().stream()
                .min((l,r)->l.compareDistanceToRestaurant(r,restaurant1.getLocation())).get();
        
        //od delivery person do restaurant(greshka vo tekstot na zadachata)

        int distance = deliveryPerson1.getCurrentLocation().distance(restaurant1.getLocation());
        deliveryPerson1.processOrder(distance,address.getLocation());
        user.processOrder(cost);
        restaurant1.processOrder(cost);
    }

    void printUsers(){
        users.values().stream().sorted(Comparator.comparing(User::totalMoneySpent).thenComparing(User::getId).reversed()).forEach(System.out::println);
    }

    void printRestaurants(){
        restaurant.values().stream().sorted(Comparator.comparing(Restaurant::averageMoneyEarned).thenComparing(Restaurant::getId).reversed()).forEach(System.out::println);

    }

    void printDeliveryPeople(){
        deliveryPerson.values().stream().sorted(Comparator.comparing(DeliveryPerson::totalMoneyEarned).thenComparing(DeliveryPerson::getId).reversed()).forEach(System.out::println);

    }


}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
