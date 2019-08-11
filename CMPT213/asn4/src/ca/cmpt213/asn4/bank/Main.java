package ca.cmpt213.asn4.bank;


public class Main {

    public static void main(String[] args) {

        SavingAccount sarbsAccount = new SavingAccount(1, 0.5);

        sarbsAccount.deposit(20);
        sarbsAccount.withdraw(5);
        sarbsAccount.deposit(6);
        sarbsAccount.withdraw(5.5);
        sarbsAccount.deposit(100);
        sarbsAccount.monthlyProcess();
        sarbsAccount.withdraw(2);
        sarbsAccount.withdraw(2);
        sarbsAccount.withdraw(2);


    }
}
