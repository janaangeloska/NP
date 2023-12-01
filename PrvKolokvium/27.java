/*Да се дефинира класа Risk со единствен метод void processAttacksData (InputStream is).
Методот од влезен поток ги чита информациите за извршените напади на еден играч врз другите играчи во стратешката игра Ризик. 
За секој поединечен напад информациите ќе се дадени во посебен ред и ќе бидат во следниот формат: X1 X2 X3;Y1 Y2 Y3, 
каде што X1, X2 и X3 се броеви добиени со фрлање на 3 коцки (број од 1-6) на напаѓачот, а Y1, Y2 и Y3 се броеви добиени 
со фрлање на 3 коцки (број од 1-6) за одбрана. Потребно е да се испечати бројот на преостанати војници на напаѓачот и нападнатиот, 
по завршувањето на нападот.
Без разлика на редоследот на фрлените коцки, бројот на преостанати војници се смета на следниот начин. 
Се почнува од најголемиот број кај напаѓачот и нападнатиот, се прави споредба и преживува војникот на оној кој има поголем број. 
Оваа постапка се прави и понатаму, додека не се изминат сите фрлени броеви.
Пример за влезот: 5 3 4; 2 4 1 ќе се испечати 3 0,
бидејќи најголемата вредност на напаѓачот (5) е поголема од најголемата вредност на нападнатиот (4) -> значи преживеал +1 војник на напаѓачот, 
втората најголема вредност на напаѓачот (4) е поголема од втората најголема вредност на нападнатиот (2) -> значи преживеал +1 војник на напаѓачот,
третата најголема вредност (3) на напаѓачот е поголема од третата најголемата вредност на нападнатиот (1) -> значи преживеал +1 војник на напаѓачот. Така, преживеале 3 војници на напаѓачот, а 0 на нападнатиот.*/

import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Runda{
    int[]x;
    int[]y;
    public Runda(int[] x, int[] y) {
        this.x = x;
        this.y = y;
    }
    public void game(){
        int counter1=0,counter2=0;
        for (int i = 0; i < 3; i++) {
            if(x[i]>y[i]){
                counter1++;
            }
            else{
                counter2++;
            }
        }
        System.out.println(counter1+" "+counter2);
    }

}
class Risk{
   ArrayList<Runda>rundi;

    public Risk() {
        rundi = new ArrayList<>();
    }

    void processAttacksData (InputStream is){
        Scanner sc=new Scanner(is);
        while (sc.hasNext()){
            String s=sc.nextLine();
            String[]parts=s.split(";");
            //0  1

            String[]napadi=parts[0].split("\\s+");
            String[]odbrani=parts[1].split("\\s+");

            int [] x=new int [3];
            int [] y=new int [3];

            for (int i = 0; i < 3; i++) {
                x[i]=Integer.parseInt(napadi[i]);
                y[i]=Integer.parseInt(odbrani[i]);
            }
            Arrays.sort(x);
            Arrays.sort(y);
            Runda runda = new Runda(x, y);
            rundi.add(runda);
            runda.game();
        }
    }
}

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
