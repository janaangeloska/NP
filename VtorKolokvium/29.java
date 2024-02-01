/*Да се имплементира класа FootballTable за обработка од податоците за повеќе фудбласки натпревари од една лига и прикажување на табелата на освоени поени според резултатите од натпреварите. Во класата да се имплементираат:
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) - метод за додавање податоци за одигран натпревар помеѓу тимот со име homeTeam (домашен тим) и тимот со име awayTeam (гостински тим), при што homeGoals претставува бројот на постигнати голови од домашниот тим, а awayGoals бројот на постигнати голови од гостинскиот тим.
    public void printTable() - метод за печатење на табелата според одиграните (внесените) натпревари. Во табелата се прикажуваат редниот број на тимот во табелата, името (со 15 места порамнето во лево), бројот на одиграни натпревари, бројот на победи, бројот на нерешени натпревари, бројот на освоени поени (сите броеви се печатат со 5 места порамнети во десно). Бројот на освоени поени се пресметува како број_на_победи x 3 + број_на_нерешени x 1. Тимовите се подредени според бројот на освоени поени во опаѓачки редослед, ако имаат ист број на освоени поени според гол разликата (разлика од постигнатите голови и примените голови) во опаѓачки редослед, а ако имаат иста гол разлика, според името.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Game {

    String team;
    int draw = 0;
    int lost = 0;
    int won = 0;
    int count = 0;
    int difference=0;

    public Game(String team) {
        this.team = team;
    }


    public void update(int homeGoals, int awayGoals) {
        count++;
        difference+=homeGoals-awayGoals;
        if (homeGoals > awayGoals) {
            won++;
        }
        else if (homeGoals < awayGoals) {
            lost++;
        }
        else {
            draw++;
        }
    }

    public String getTeam() {
        return team;
    }

    public int getDifference() {
        return difference;
    }

    public int getPoints(){
        return won*3+draw;
    }


    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d",team,count,won,draw,lost,getPoints());
    }
}

class FootballTable {
    Map<String, Game> tabela;
    Comparator <Game> comparator=Comparator.comparing(Game::getPoints).thenComparing(Game::getDifference).reversed().thenComparing(Game::getTeam);

    public FootballTable() {
        this.tabela = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        tabela.putIfAbsent(homeTeam, new Game(homeTeam));
        tabela.get(homeTeam).update(homeGoals, awayGoals);
        tabela.putIfAbsent(awayTeam, new Game(awayTeam));
        tabela.get(awayTeam).update(awayGoals, homeGoals);
    }

    public void printTable() {
        List<Game> collect = tabela.values().stream().sorted(comparator).collect(Collectors.toList());

        collect.forEach(i-> System.out.printf("%2d. %s\n",collect.indexOf(i)+1,i.toString()));
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
