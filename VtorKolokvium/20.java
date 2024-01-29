/*Да се имплементира класа DailyTemperatures во која се вчитуваат температури на воздухот (цели броеви) за различни денови од годината (број од 1 до 366). Температурите за еден ден се во еден ред во следниот формат (пример): 137 23C 15C 28C. Првиот број претставува денот во годината, а потоа следуваат непознат број на мерења на температури за тој ден во скала во Целзиусови степени (C) или Фаренхајтови степени (F).
Во оваа класа да се имплементираат методите:
    DailyTemperatures() - default конструктор
    void readTemperatures(InputStream inputStream) - метод за вчитување на податоците од влезен тек
    void writeDailyStats(OutputStream outputStream, char scale) - метод за печатање на дневна статистика (вкупно мерења, минимална температура, максимална температура, просечна температура) за секој ден, подредени во растечки редослед според денот. Вториот аргумент scale одредува во која скала се печатат температурите C - Целзиусова, F - Фаренхајтова. Форматот за печатање на статистиката за одреден ден е следниот:
[ден]: Count: [вк. мерења - 3 места] Min: [мин. температура] Max: [макс. температура] Avg: [просек ]
Минималната, максималната и просечната температура се печатат со 6 места, од кои 2 децимални, а по бројот се запишува во која скала е температурата (C/F).
Формула за конверзија од Целзиусуви во Фаренхајтови: $\frac{T * 9}{5} + 32$
Формула за конверзија од Фаренхајтови во Целзиусуви: $\frac{(T - 32) * 5}{9}$
Забелешка: да се постигне иста точност како во резултатите од решението, за пресметување на просекот и конверзијата во различна скала температурите се чуваат со тип Double.*/


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Merenje{
    int id;
    List<String> temperaturi;

    public Merenje(int id, List<String> temperaturi) {
        this.id = id;
        this.temperaturi = temperaturi;
    }

    public List<Double> tempCasted(){
        List<Double> collect = temperaturi.stream()
                .map(tmp -> {
                    String value = tmp.substring(0, tmp.length() - 1);
                    return Double.parseDouble(value);
                })
                .collect(Collectors.toList());
        return collect;
    }
    public int getCount(){
        return temperaturi.size();
    }
    public double getAvg(List<Double> podatoci){
        return podatoci.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double getMin(List<Double> podatoci){
        return podatoci.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }


    public double getMax(List<Double> podatoci){
        return podatoci.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public List<Double> converterToCelsius(){
        return tempCasted().stream().map(this::convertFromFahrenheitToCelsius).collect(Collectors.toList());
    }

    public List<Double> converterToFarenheit(){
        return tempCasted().stream().map(this::convertFromCelsiusToFahrenheit).collect(Collectors.toList());
    }

    private Double convertFromFahrenheitToCelsius(Double temperatureFahrenheit) {
        if (temperaturi.get(0).endsWith("F")) {
            return (temperatureFahrenheit - 32) * 5 / 9;
        } else {
            return temperatureFahrenheit;
        }
    }

    private Double convertFromCelsiusToFahrenheit(Double temperatureCelsius) {
        if (temperaturi.get(0).endsWith("C")) {
            return (temperatureCelsius * 9 / 5) + 32;
        } else {
            return temperatureCelsius;
        }
    }
    public String toCelsius() {
        List<Double> celsiusTemperatures=converterToCelsius();
        double min = getMin(celsiusTemperatures);
        double max = getMax(celsiusTemperatures);
        double avg = getAvg(celsiusTemperatures);
        return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC",id,getCount(),min,max,avg);

    }
    public String toFahrenheit() {
        //lista convertirana vo farenheits
        List<Double> fahrenheitTemperatures=converterToFarenheit();
        double min = getMin(fahrenheitTemperatures);
        double max = getMax(fahrenheitTemperatures);
        double avg = getAvg(fahrenheitTemperatures);
        return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF",id,getCount(),min,max,avg);

    }

    public int getId() {
        return id;
    }
}
class DailyTemperatures{
    List<Merenje> merenja;

    public DailyTemperatures() {
        this.merenja = new ArrayList<>();
    }

    void readTemperatures(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while (sc.hasNextLine()){
            List<String> podatoci=new ArrayList<>();
            String line=sc.nextLine();
            String[]parts=line.split("\\s+");
            int id=Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                podatoci.add(parts[i]);
            }
            merenja.add(new Merenje(id,podatoci));
        }
    }
    void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw=new PrintWriter(outputStream);
        merenja.stream().sorted(Comparator.comparing(Merenje::getId)).forEach(merenje -> {
            if(scale == 'C'){
                pw.println(merenje.toCelsius());
            } else if (scale=='F') {
                pw.println(merenje.toFahrenheit());
            }
        });

        pw.flush();
    }
}
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
