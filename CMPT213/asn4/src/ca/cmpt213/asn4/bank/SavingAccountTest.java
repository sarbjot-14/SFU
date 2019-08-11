package ca.cmpt213.asn4.bank;

import junit.framework.TestCase;

/**
 * This class is responsible for testing the use cases for BankAccount class as
 * well as SavingAccount class
 */
public class SavingAccountTest extends TestCase {
    SavingAccount testAccount;

    @Override
    protected void setUp() throws Exception {
        testAccount = new SavingAccount(100, 0.6);

    }

    @Override
    protected void tearDown() throws Exception {
        testAccount = null;
    }
    //TESTING CONSTRUCTOR
    //init with negative bank account
    public void testNegativeBalanceInit(){
        try {
            SavingAccount testBankAccountInit = new SavingAccount(-100, 0.05);
            fail();
        }
        catch(IllegalArgumentException iae){
            assertTrue(true);
        }
    }
    //init with negative interest rate
    public void testNegativeInterestRateInit(){
        try {
            SavingAccount testBankAccountInit = new SavingAccount(100, -0.05);
            fail();
        }
        catch(IllegalArgumentException iae){
            assertTrue(true);
        }
    }
    //init with valid arguments
    public void testValidArgInit(){
        SavingAccount testBankAccountInit = new SavingAccount(100, 0.05);
        if(testBankAccountInit.balance != 100.0 | testBankAccountInit.annualInterestRate != 0.05){
            fail();
        }
        if(testBankAccountInit.numWithdrawals !=0){
            fail();
        }
        if(testBankAccountInit.numDepositsThisMonth !=0){
            fail();
        }
        if(testBankAccountInit.monthlyServiceCharges !=0){
            fail();
        }



    }
    //TESTING DEPOSITS

    public void testSetNegativeDeposit() {
        //test if deposit is negative value
        try {
            testAccount.deposit(-5);
            fail();
        }catch(IllegalArgumentException iae){
            assertTrue(true);
        }

    }
    public void testDeposit(){
        //test if deposited and added to balance correctly
        double oldBalance = testAccount.balance;
        testAccount.deposit(5);
        assertEquals(oldBalance + 5, testAccount.balance);
    }
    public void testDepositActivatesAccount(){
        //test if made account active if balance is more thant $25
        testAccount.deposit(25);
        assertTrue(testAccount.isActive);
    }

    //TESTING WITHDRAWALS

    public void testNegativeWithdrawal(){
        testAccount.balance = 30;
        try {
            testAccount.withdraw(-5);
            fail();
        }catch(IllegalArgumentException iae){
            assertTrue(testAccount.balance == 30.0);
        }
    }

    public void testWithdrawNotEnoughFunds(){
        testAccount.balance = 50;

        testAccount.withdraw(100);

        assertTrue(testAccount.balance == 50.0);

    }

    public void testWithdrawInactiveAccount(){
        testAccount.balance = 20;
        try {
            testAccount.withdraw(10);
            fail();
        }
        catch(IllegalCallerException ice) {
            assertEquals(testAccount.balance, 20.0);
        }
    }

    public void testValidWithdraw() {
        testAccount.balance = 100;
        testAccount.withdraw(50);
        assertEquals(50.0, testAccount.balance);
    }

    //TEST INTEREST CALCULATIONS
    public void testCalcInterest() {
        testAccount.annualInterestRate = 0.6;
        testAccount.balance = 100;
        testAccount.calcInterest();
        assertEquals(100 + 0.05*100, testAccount.balance);


    }
    //TEST MONTHLY PROCESS

    //test monthly process if withdrawals <= 4
    public void testWithdrawalsUnderFour() {
        double testBalance= testAccount.balance;
        int testNumWith = -1;
        double testMonthlyInterest = testAccount.annualInterestRate/12;
        for(int i = 0 ; i<= 4 ; i++){
            testNumWith ++;
            testAccount.withdraw(1);
            testBalance = testAccount.balance;
            testAccount.monthlyProcess();
            if(testNumWith <= 4){
                assertTrue(testAccount.balance == testBalance + testBalance * testMonthlyInterest);
            }
        }
    }
    //tests monthly process if withdrawal >4
    public void testWithdrawalsOverFour() {
        double testBalance= testAccount.balance;
        int testNumWith = 0;
        double testMonthlyInterest = testAccount.annualInterestRate/12;
        //check when withdrawals is 5
        for(int i = 0 ; i<= 5 ; i++){
            testNumWith++;
            testBalance--;
            testAccount.withdraw(1);
        }

        testAccount.monthlyProcess();
        testBalance = testBalance - (testNumWith - 4);

        if(testAccount.balance != testBalance + testBalance * testMonthlyInterest){
            fail();
        }
        //check when number of withdrawals is 20

        testBalance= testAccount.balance;
        testNumWith = 0;
        for(int i = 0 ; i<= 20 ; i++){
            testNumWith++;
            testBalance--;
            testAccount.withdraw(1);
        }
        testAccount.monthlyProcess();
        testBalance = testBalance - (testNumWith - 4);
        if(testAccount.balance != testBalance + testBalance * testMonthlyInterest){
            fail();
        }


    }

    //tests monthly process, set number of withdrawals, number of deposits,
    // and monthly service charge to zero
    public void testResetWithdrawalDepositsMonthlyServiceCount(){
        for(int i = 0 ; i<= 20 ; i++){
            testAccount.deposit(5);
            testAccount.withdraw(1);
        }
        testAccount.monthlyProcess();;
        if(testAccount.numWithdrawals !=0){
            fail();
        }
        if(testAccount.numDepositsThisMonth !=0){
            fail();
        }
        if(testAccount.monthlyServiceCharges !=0){
            fail();
        }


    }

    //tests if account is inactive if balance falls below 25
    public void testInactiveWhenBelow25(){
        for(int i = 0 ; i < 14 ; i++){
            testAccount.withdraw(1);
        }
        testAccount.monthlyProcess();
        if(!testAccount.isActive){
            fail();
        }

        for(int i = 0 ; i < 33 ; i++){
            testAccount.withdraw(1);
        }
        testAccount.monthlyProcess();

        if(testAccount.isActive){
            fail();
        }
    }

    //test for when monthly fee brings accaount balance below 0
    public void testMonthlyFeeExceedsBalance(){
        for(int i = 0 ; i < 60 ; i++){
            testAccount.withdraw(1);
        }
        double balanceBeforeFees = testAccount.balance;
        testAccount.monthlyProcess();

        assertEquals(-16.0, testAccount.balance);


    }




}