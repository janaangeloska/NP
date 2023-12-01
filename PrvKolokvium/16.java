/*Да се имплементира класа MojDDV која што од влезен тек ќе чита информации за скенирани фискални сметки од страна на еден корисник на истоимената апликација. Податоците за фискалните сметки се во следниот формат:
ID item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
На пример: 12334 1789 А 1238 B 1222 V 111 V
Постојат три типа на данок на додадена вредност и тоа:
    А (18% од вредноста)
    B (5% од вредноста)
    V (0% од вредноста)
Повратокот на ДДВ изнесува 15% од данокот на додадената вредност за артикалот.
Да се имплементираат методите:
    void readRecords (InputStream inputStream)- метод којшто ги чита од влезен тек податоците за фискалните сметки. Доколку е скенирана фискална сметка со износ поголем од 30000 денари потребно е да се фрли исклучок од тип AmountNotAllowedException. Дефинирајте каде ќе се фрла исклучокот, и каде ќе биде фатен, на начин што оваа функција, ќе може да ги прочита сите фискални коишто се скенирани. Исклучокот треба да испечати порака “Receipt with amount [сума на сите артикли] is not allowed to be scanned”.
    void printTaxReturns (OutputStream outputStream) - метод којшто на излезен тек ги печати сите скенирани фискални сметки во формат ID SUM_OF_AMOUNTS TAX_RETURN, каде што SUM_OF_AMOUNTS e збирот на сите артикли во фискалната сметка, а TAX_RETURN е пресметаниот повраток на ДДВ за таа фискална сметка.
дополнително:
    void printStatistics (OutputStream outputStream) - метод којшто на излезен тек печати статистики за повратокот на ДДВ од сите скенирани фискални сметки во формат min: MIN max: MAX sum: SUM count: COUNT average: AVERAGE, при што секоја од статистиките е во нов ред, а пак вредноста на статистиката е оддалечена со таб од името на статистиката (погледнете тест пример). Децималните вредности се печатат со 5 места, од кои 3 се за цифрите после децималата. Целите вредности се пишуваат со 5 места (порамнети на лево).
    печатењето на вредностите во методот printTaxReturns се врши на тој начин што:
    сите информации се одвоени со таб
    id-то i сумата на фискалната сметка се печатат со 10 места
    повратокот на ДДВ со 10 места, од кои 5 се за цифрите после децималата.
*/
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

enum Type {
    A, B, V
}

class Produkt {
    int cena;
    Type type;

    public Produkt(int cena, Type type) {
        this.cena = cena;
        this.type = type;
    }

    public int getCena() {
        return cena;
    }

    public Type getType() {
        return type;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double DDV() {
        if (type == Type.A) {
            return cena * 0.15 * 0.18;
        } else if (type == Type.B) {
            return cena * 0.15 * 0.05;
        }
        return 0;
    }
}

class Smetka {
    int id;
    ArrayList<Produkt> produkti;

    public Smetka() {
        id = 0;
        produkti = new ArrayList<>();
    }

    public Smetka(int id, ArrayList<Produkt> produkti) {
        this.id = id;
        this.produkti = produkti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addProdukt(Produkt produkt) {
        produkti.add(produkt);
    }

    public void check() throws AmountNotAllowedException {
        if (totalPrice() > 30000) {
            throw new AmountNotAllowedException(totalPrice());
        }
    }

    public int totalPrice() {
        return produkti.stream().mapToInt(i -> i.getCena()).sum();
    }

    public double taxReturn() {
        return produkti.stream().mapToDouble(i -> i.DDV()).sum();
    }

    @Override
    public String toString() {
        //12334 1789 А 1238 B 1222 V 111 V
        return String.format("%10s\t%10d\t%10.5f", id, totalPrice(), taxReturn());
    }
}

class MojDDV {
    ArrayList<Smetka> smetki;

    public MojDDV() {
        smetki = new ArrayList<>();
    }

    void readRecords(InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (in.hasNextLine()) {
            String s = in.nextLine();
            String[] parts = s.split("\\s+");
            Smetka smetka = new Smetka();
            int id = Integer.parseInt(parts[0]);
            smetka.setId(id);
            for (int i = 1; i < parts.length; i += 2) {
                int cena = Integer.parseInt(parts[i]);
                Type tip = Type.valueOf(parts[i + 1]);
                smetka.addProdukt(new Produkt(cena, tip));
            }
            try {
                smetka.check();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            smetki.add(smetka);
        }
    }

    void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        smetki.forEach(i -> System.out.println(i));
        pw.flush();
    }

    public void printStatistics(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        DoubleSummaryStatistics summaryStatistics = smetki.stream().mapToDouble(i -> i.taxReturn()).summaryStatistics();
        pw.println(String.format("min:\t%.3f", summaryStatistics.getMin()));
        pw.println(String.format("max:\t%.3f", summaryStatistics.getMax()));
        pw.println(String.format("sum:\t%.3f", summaryStatistics.getSum()));
        pw.println(String.format("count:\t%d", summaryStatistics.getCount()));
        pw.println(String.format("avg:\t%.3f", summaryStatistics.getAverage()));
        pw.flush();
    }
}


public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}
