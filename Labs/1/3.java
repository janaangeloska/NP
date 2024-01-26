/*Треба да се креира апликација за банка која ќе управуваа со сметките на повеќе корисниците и ќе врши трансакции помеѓу нив. Банката работи само со долари и притоа сите суми на пари се претставуваат како стрингови со знакот $ на крај, и точка помеѓу бројот на центи и бројот на долари без празни места. Бројот на центи треба да се состои од две цифри без разлика на износот.
Пример:
10 долари  10.00$
15 долари и 50 центи 15.50$
За потребите на ваквата апликација треба да се напишат класите Account,Transaction и Bank. Класата Account претставува една сметка на еден корисник и треба да ги чува следните податоци:
    Име на корисникот,
    единствен идентификационен број (long)
    тековното салдо на сметката.
Оваа класа исто така треба да ги имплементира и следниве методи
    Account(String name, String balance) – конструктор со параметри (id-то треба да го генерирате сами со помош на класата java.util.Random)
    getBalance():String
    getName():String
    getId():long
    setBalance(String balance)
    toString():String – враќа стринг во следниот формат, \n означува нов ред
    Name:Andrej Gajduk\n
    Balance:20.00$\n
Класата Transaction претставува трансакција (префрлување пари од една на друга сметка), од страна на банката за што честопати се наплаќа провизија. За почеток треба да се напише класата Transaction со податочни членови за идентификационите броеви на две сметки, едната од која се одземаат парите и друга на која се додаваат парите, текстуален опис и износ на трансакцијата.
За оваа класа треба да ги имплементирате методите:
    Transaction(long fromId, long toId, Stirng description, String amount) – конструктор со параметри
    getAmount():String
    getFromId():long
    getToId():long
Оваа класа треба да е immutable, а можете и да ја направите и апстрактна бидејќи не е наменета директно да се користи туку само како основна класа за изведување на други класи.
Како што споменавме претходно банката наплаќа провизија за одредени трансакции. Има два типа на провизија, фискна сума и процент. Кај фиксна сума за било која трансакција без разлика на износот на трансакцијата се наплаќа исто провизија (пример 10$). Кај процент за секој еден долар од трансакцијата банката наплаќа одреден процент провизија (на пример 5%, или 5 центи на секој долар – процентите секогаш се целобројни и провизија се наплаќа само на цели долари).
За да се прави разлика меѓу различните типови на провизија, треба да напишете уште две класи кои ќе наследуваат од Transaction кои треба да ги именувате FlatAmountProvisionTransaction и FlatPercentProvisionTransaction.
Првата класа FlatAmountProvisionTransaction треба да содржи соодветен конструктор
    FlatAmountProvisionTransaction(long fromId, long toId,String amount, String flatProvision) кој го иницијализира полето за опис на "FlatAmount" и соодветен get метод
    getFlatAmount():String
Слично и класата FlatPercentProvisionTransaction треба да има соодветен конструктор
    FlatPercentProvisionTransaction (long fromId, long toId, String amount, int centsPerDolar) кој го иницијализира полето за опис на "FlatPercent" и соодветен get метод
    getPercent():int
Исто така треба да се преоптовари equals(Object o):boolean методот и за двете класи.
За крај треба да ја имплементирате класата Bank која ги чува сметките од своите корисници и дополнително врши трансакции. Класата освен сметките на своите корисници, треба да ги чува и сопственото име и вкупната сума на трансфери како и вкупната наплатена провизија од страна на банката за сите трансакции.
Класата Bank треба да ги нуди следните методи:
    Bank(String name, Account accounts[]) – конструктор со соодветните параметри (направете сопствена копија на низата од сметки)
    makeTransaction(Transaction t):boolean – врши проверка дали корисникот ги има потребните средства на сметка и дали и двете сметки на кои се однесува трансакцијата се нависитина во банката и ако и двата услови се исполнето ја извршува трансакцијата и враќа true, во спротивно враќа false
    totalTransfers():String – ја дава вкупната сума на пари кои се префрлени во сите трансакции до сега
    totalProvision():String – ја дава вкупната провизија наплатена од банката за сите извршени трансакции до сега
    toString():String - го враќа името на банката во посебна линија во формат
    Name:Banka na RM\n
    \n
    по што следат податоците за сите корисници.
Провизијата се наплаќа така што на основната сума на трансакцијата се додава вредноста не провизијата и таа сума се одзема од првата сметка.
За сите класи да се напишат соодветни equals и hashCode методи.*/

// package labs.prv.treta;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Random;


class Account{
    String name;
    long id;
    String balance;


    public Account(String name, String saldo) {
        this.name = name;
        this.balance = saldo;
        Random random=new Random();
        id=random.nextLong();
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Name: "    + name    +"\n"+
                "Balance: " + balance +"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(name, account.name) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, balance);
    }
}

abstract class Transaction{
    private final long fromId;
    private final long toId;
    private final String description;
    private final String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public abstract double getProvision();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId && toId == that.toId && Objects.equals(description, that.description) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }
}

class FlatAmountProvisionTransaction extends Transaction{

    String flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision=flatProvision;
    }

    @Override
    public double getProvision() {
        String stringProvision=flatProvision;
        String toNr4=stringProvision.substring(0,stringProvision.length()-1);
        return Double.parseDouble(toNr4);
    }

    public String getFlatProvision() {
        return flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Objects.equals(flatProvision, that.flatProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatProvision);
    }


}


class FlatPercentProvisionTransaction extends Transaction{

    int percentProvision;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int percentProvision) {
        super(fromId, toId, "FlatPercent", amount);
        this.percentProvision=percentProvision;
    }

    public int getPercentProvision() {
        return percentProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return percentProvision == that.percentProvision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(percentProvision);
    }
    @Override
    public double getProvision() {
        String Balance=getAmount();
        String toNr2=Balance.substring(0,Balance.length()-1);
        double provision=Double.parseDouble(toNr2);
        return (int)provision*(percentProvision/100.0);
    }
}

class Bank{
    String name;
    Account []accounts;
    double totalTransfer;
    double totalProvision;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts,accounts.length);
        this.totalProvision=0;
        this.totalTransfer=0;
    }

    public int ID(long id){
        for (int i = 0; i < accounts.length; i++) {
            if(accounts[i].getId()==id){
                return i;
            }
        }
        return -1;
    }
    public boolean makeTransaction(Transaction t){
        int from=ID(t.getFromId());
        int to=ID(t.getToId());
        if(from==-1||to==-1){
            return false;
        }

        String accFrom=accounts[from].getBalance();
        String toNr=accFrom.substring(0,accFrom.length()-1);
        double balanceFrom=Double.parseDouble(toNr);

        String accFromBalance=t.getAmount();
        String toNr2=accFromBalance.substring(0,accFromBalance.length()-1);
        double transaction=Double.parseDouble(toNr2);

        if(balanceFrom<transaction){
            return false;
        }

        String accTo=accounts[to].getBalance();
        String toNr3=accTo.substring(0,accTo.length()-1);
        double balanceTo=Double.parseDouble(toNr3);
        double provision=t.getProvision();


        totalTransfer+=transaction;
        totalProvision+=provision;

        if(from==to){
            accounts[from].setBalance(String.format("%.2f$",balanceFrom-provision));
        }
        else{
            accounts[from].setBalance(String.format("%.2f$",balanceFrom-provision-transaction));
            accounts[to].setBalance(String.format("%.2f$",balanceTo+transaction));
        }
        return true;
    }

    public String totalTransfers(){
        return String.format("%.2f$",totalTransfer);
    }

    public String totalProvision(){
        return String.format("%.2f$",totalProvision);
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Name: ").append(name).append("\n\n");
        for (Account account:accounts) {
            sb.append(account.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(totalTransfer, bank.totalTransfer) == 0 && Double.compare(totalProvision, bank.totalProvision) == 0 && Objects.equals(name, bank.name) && Arrays.equals(accounts, bank.accounts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, totalTransfer, totalProvision);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }
}
