package ca.cmpt213.asn4.bank;

/**
 * This class is responsible for holding balance, withdrawing, depositing, calculating interest
 * and monthly process.
 */
abstract class BankAccount {
    protected double balance;
    public int numDepositsThisMonth;
    public int numWithdrawals;
    public double annualInterestRate;
    public int monthlyServiceCharges;

    BankAccount(double balance, double interestRate){
        //make sure balance is positive
        if(balance <= 0){
            System.out.println("Sorry, please enter positive balance amount");
            throw new IllegalArgumentException();
        }
        //make sure interestRate is positive
        if(interestRate <=0){
            System.out.println("Sorry, please enter positive interest rate");
            throw new IllegalArgumentException();
        }
        this.balance = balance;
        this.annualInterestRate = interestRate;
        this.numWithdrawals = 0;
        this.numDepositsThisMonth =0;
        this.monthlyServiceCharges =0;

    }
    public void deposit(double deposit){
        //if deposit is negative throw exception
        if(deposit <0){
            System.out.println("Sorry, please enter positive deposit amount");
            throw new IllegalArgumentException();
        }

        balance += deposit;
        numDepositsThisMonth++;
    }
    public void withdraw(double withdrawal){
        //check if the withdrawal is negative
        if(withdrawal<0){
            System.out.println("Sorry, please enter positive withdrawal amount");
            throw new IllegalArgumentException();
        }
        //check if enough in balance for withdrawal
        if((balance - withdrawal)<0){
            System.out.println("Sorry, not enough funds available for withdrawal\nPlease deposit more funds to meet withdrawal request\nCurrent balance is: "+ balance);

        }
        else {
            balance = balance - withdrawal;
            numWithdrawals++;
        }
        //System.out.println("withdrawal made of " + withdrawal + " balance is now " + balance);
    }
    protected void calcInterest(){
        double monthlyInterest;
        double monthlyInterestRate = annualInterestRate/12;
        if(balance<0){
            System.out.println("Balance below 0. No interest is added");
            monthlyInterest = 0;
        }
        else{
            monthlyInterest = balance*monthlyInterestRate;
        }

        balance =  balance + monthlyInterest;

    }
    public void monthlyProcess(){
        //it is okay if the monthly charge brings the balance below 0 dollars

        balance = balance - monthlyServiceCharges;

        calcInterest();
        numWithdrawals =0;
        numDepositsThisMonth =0;
        monthlyServiceCharges = 0;


    }
}
