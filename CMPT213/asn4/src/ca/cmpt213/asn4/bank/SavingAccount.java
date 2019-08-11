package ca.cmpt213.asn4.bank;


/**
 * SavingAccount class is responsible for a Savings Account which is a type of BankAccount.
 * This Account can be active only when balance is above 25 dollars. And calculates monthly fee
 * for the monthly process, along with the other use cases of BankAccount class.
 */
public class SavingAccount extends BankAccount {
    public boolean isActive = false;

    SavingAccount(double balance, double interestRate){
        super(balance, interestRate);

    }

    @Override
    public void withdraw(double withdrawal) {
        //check if account is active before withdrawing
        if(balance >= 25){ //should I access balance using super or this or nothing?
            isActive = true;
        }
        else{
            isActive = false;
        }
        if(isActive){
            super.withdraw(withdrawal);

        }
        else{
            throw new IllegalCallerException();
        }
        if(balance <25){
            isActive = false;
        }

    }

    @Override
    public void deposit(double deposit) {
        if(balance + deposit >= 25){ //should I access balance using super or this or nothing?
            isActive = true;
        }
        else{
            isActive = false;
        }
        //make deposit
        super.deposit(deposit);
        //System.out.println("deposit is " + deposit + "isActive is " + isActive + " balance is " + balance);

    }

    @Override
    public void monthlyProcess() {

        if(numWithdrawals > 4){
            monthlyServiceCharges = numWithdrawals - 4;
        }
        //System.out.println("numWithdrawals " + numWithdrawals);
        super.monthlyProcess();

        if(balance >= 25){
            isActive = true;
        }
        else{
            isActive = false;
        }

    }
}
