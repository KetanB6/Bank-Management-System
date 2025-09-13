package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static BankingApplication.BankingApp.loader;

public class AccountManager {
    private Connection connection;
    private Scanner sc;

    AccountManager(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void depositMoney(long account_number) throws SQLException, InterruptedException {
        int retrieved_pin = 0;
        System.out.println("*** Cash Deposit ***");
        System.out.println("A/c Number: " + account_number);
        System.out.print("PIN: ");
        int pin = sc.nextInt();
        if(verifyPIN(account_number, pin)){
            System.out.print("Amount: ");
            double amount = sc.nextInt();
            int affected_rows = addMoney(account_number, amount);
            if(affected_rows > 0){
                System.out.print("Transaction Initiated");
                loader();
                System.out.println("\nAmount Deposited Successfully!");
            } else{
                System.out.println("Amount Deposit Failed");
            }
        } else {
            System.out.println("Invalid PIN!!!");
        }
    }

    private int addMoney(long account_number, double amount) throws SQLException{
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, amount);
        preparedStatement.setLong(2, account_number);
        return preparedStatement.executeUpdate();
    }

    public void withdrawMoney(long account_number) throws SQLException, InterruptedException {
        String retrieve_acc_details = "SELECT account_pin FROM accounts WHERE account_number = ?;";
        PreparedStatement preparedStatement1 = connection.prepareStatement(retrieve_acc_details);
        preparedStatement1.setLong(1, account_number);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        int retrieved_pin = 0;
        if(resultSet1.next()) {
            retrieved_pin = resultSet1.getInt("account_pin");
        }
        System.out.println("*** Cash Withdraw ***");
        System.out.println("A/c Number: " + account_number);
        System.out.print("PIN: ");
        int pin = sc.nextInt();
        if(verifyPIN(account_number, pin)){
            System.out.print("Amount: ");
            double amount = sc.nextInt();
            int affected_rows = subtractMoney(account_number, amount);
            if(affected_rows > 0){
                System.out.print("Transaction Initiated");
                loader();
                System.out.println("\nAmount Withdrawn Successfully!");
            } else{
                System.out.println("Amount Withdraw Failed");
            }
        } else {
            System.out.println("Invalid PIN!!!");
        }
    }

    private int subtractMoney(long account_number, double amount) throws SQLException{
        String query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, amount);
        preparedStatement.setLong(2, account_number);
        return preparedStatement.executeUpdate();
    }

    private boolean verifyPIN(long account_number, int pin) throws SQLException{
        String retrieve_acc_details = "SELECT account_pin FROM accounts WHERE account_number = ?;";
        PreparedStatement preparedStatement1 = connection.prepareStatement(retrieve_acc_details);
        preparedStatement1.setLong(1, account_number);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        int retrievedPIN = 0;
        if(resultSet1.next()) {
            retrievedPIN =  resultSet1.getInt("account_pin");
        }
        return (pin == retrievedPIN);
    }

    public void transferMoney(Long account_number) throws SQLException, InterruptedException{
        connection.setAutoCommit(false);
        String getDetailsQuery = "SELECT full_name FROM accounts WHERE account_number = ?;";
        PreparedStatement preparedStatement1 = connection.prepareStatement(getDetailsQuery);
        System.out.println("\n*** Money Transfer ***");
        System.out.println("From A/c: " + account_number);
        System.out.print("To A/c: ");
        long ac_no = sc.nextLong();
        if(!accountNumberExist(ac_no)){
            System.out.println("Account Does Not Exist!");
            return;
        }
        preparedStatement1.setLong(1, ac_no);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        String name = "";
        if(resultSet1.next()){
            name = resultSet1.getString("full_name");
        }
        System.out.print("Fetching Receivers Name");
        loader();
        System.out.println("\nReceivers Name: " + name);
        System.out.println();
        System.out.print("Amount: ");
        int amount = sc.nextInt();
        System.out.print("Account PIN: ");
        int pin = sc.nextInt();
        if(verifyPIN(account_number, pin)){
            System.out.print("Transaction Initiated");
            loader();
            int debitSender = subtractMoney(account_number, amount);
            int creditReceiver = addMoney(ac_no, amount);
            if(debitSender > 0 && creditReceiver > 0){
                System.out.println("\nTransfer Successful!");
                connection.commit();
            } else{
                System.out.println("\nTransfer Failed!");
                connection.rollback();
            }
        } else {
            System.out.println("Invalid Password!");
        }
    }

    public void showBalance(Long account_number) throws SQLException, InterruptedException{
        String retrieve_acc_details = "SELECT account_pin, balance FROM accounts WHERE account_number = ?;";
        PreparedStatement preparedStatement1 = connection.prepareStatement(retrieve_acc_details);
        preparedStatement1.setLong(1, account_number);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        int retrieved_pin = 0;
        double balance = 0;
        if(resultSet1.next()) {
            retrieved_pin = resultSet1.getInt("account_pin");
            balance = resultSet1.getDouble("balance");
        }
        System.out.println("*** Check Balance ***");
        System.out.println("A/c Number: " + account_number);
        System.out.print("PIN: ");
        int pin = sc.nextInt();
        if(pin == retrieved_pin) {
            System.out.print("Transaction Initiated");
            loader();
            System.out.println("\nAvailable Balance: " + balance);
        } else {
            System.out.println("Invalid PIN!");
        }
    }

    public boolean accountNumberExist(Long account_number) throws SQLException {
        String isAccountAvailable = "SELECT *FROM accounts WHERE account_number = ?;";
        PreparedStatement check = connection.prepareStatement(isAccountAvailable);
        check.setLong(1, account_number);
        ResultSet resultSet = check.executeQuery();
        return resultSet.next();
    }

}
