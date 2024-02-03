/*Треба да се развие генеричка класа за работа со дропки. Класата GenericFraction има два генерички параметри T и U кои мора да бидат од некоја класа која наследува од класата Number. GenericFraction има две променливи:
    numerator - броител
    denominator - именител.
Треба да се имплементираат следните методи:
    GenericFraction(T numerator, U denominator) - конструктор кој ги иницијализира броителот и именителот на дропката. Ако се обидиме да иницијализираме дропка со 0 вредност за именителот треба да се фрли исклучок од тип ZeroDenominatorException
    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) - собирање на две дропки
    double toDouble() - враќа вредност на дропката како реален број
    toString():String - ја печати дропката во следниот формат [numerator] / [denominator], скратена (нормализирана) и секој со две децимални места.
*/


// package kolokviumski1.edinaesetta;

import java.util.Scanner;

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class GenericFraction<T extends Number, U extends Number> {
    T numerator;
    U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0) {
            throw new ZeroDenominatorException("Denominator cannot be zero");
        } else {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }

    public double gcd(double a, double b) {
        if(b==0){
            return a;
        }
        if(a<b){
            return gcd(a,b-a);
        }
        else{
            return gcd(b,a-b);
        }
    }
    public double gcd() {
        return gcd(this.numerator.doubleValue(), this.denominator.doubleValue());
    }


    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        return new GenericFraction<Double,Double>(numerator.doubleValue()*gf.denominator.doubleValue()+denominator.doubleValue()*gf.numerator.doubleValue(),
                                                    denominator.doubleValue()*gf.denominator.doubleValue());
    }

    public double toDouble() {
        return numerator.doubleValue() / denominator.doubleValue();

    }

    @Override
    public String toString() {
        return String.format("%.2f / %.2f",numerator.doubleValue()/gcd(),denominator.doubleValue()/gcd());
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch (ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

