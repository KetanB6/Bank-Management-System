package BankingApplication;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static BankingApplication.BankingApp.loader;

public class Accounts extends Thread {
    private Connection connection;
    private Scanner sc;

    Accounts(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void openAccount(String email) throws SQLException, InterruptedException{
        if(accountExist(email)){
            System.out.println("Account already exist for email id!!!");
            return;
        }
        String openAccountQuery = "INSERT INTO accounts VALUES(?, ?, ?, ?, ?);";
        String getNameEmail = "SELECT *FROM users WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(getNameEmail);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        String name = "";
        while(resultSet.next()){
            name = resultSet.getString("full_name");
        }
        System.out.println("*** Account Opening Form ***\n");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Generating Account Number & PIN");
        loader();
        long acc_no = generateAccountNumber();
        System.out.println("\nAccount Number: " + acc_no);
        int pin = generateAccountPIN();
        System.out.println("Account PIN: " + pin);
        System.out.print("Initial Balance: Rs.");
        double balance = sc.nextDouble();
        System.out.print("\nConfirm opening account (y/n): ");
        String confirm = sc.next();
        System.out.println();
        if(confirm.equalsIgnoreCase("y")){
            PreparedStatement fillAccountInfo = connection.prepareStatement(openAccountQuery);
            fillAccountInfo.setLong(1, acc_no);
            fillAccountInfo.setString(2, name);
            fillAccountInfo.setString(3, email);
            fillAccountInfo.setDouble(4, balance);
            fillAccountInfo.setInt(5, pin);
            int affected_rows = fillAccountInfo.executeUpdate();
            if(affected_rows > 0){
                System.out.print("Creating Account");
                loader();
                System.out.println("\nAccount Opened successfully!\n");
            }
        } else{
            System.out.println("Account Opening Cancelled!");
        }
        preparedStatement.close();
        resultSet.close();
    }

    public long getAccountNumber(String email) throws SQLException{
        String accNumberQuery = "SELECT account_number FROM accounts WHERE email = ?;";
        PreparedStatement getAccNo = connection.prepareStatement(accNumberQuery);
        getAccNo.setString(1, email);
        ResultSet resultSet = getAccNo.executeQuery();
        if(resultSet.next()){
            return resultSet.getLong("account_number");
        }
        return 0L;
    }

    private long generateAccountNumber() throws SQLException{
        SecureRandom secureRandom = new SecureRandom();
        String query = "SELECT * FROM accounts WHERE account_number = ?;";
        PreparedStatement check = connection.prepareStatement(query);
        while(true) {
            long acc_number = 1000000000L + (long)(secureRandom.nextDouble() * 9000000000L);
            check.setLong(1, acc_number);
            ResultSet resultSet = check.executeQuery();
            if(!resultSet.next()){
                check.close();
                return acc_number;
            }
        }
    }

    private int generateAccountPIN() throws SQLException{
        SecureRandom secureRandom = new SecureRandom();
        return (1000 + (int)(secureRandom.nextDouble() * 9000));
    }

    protected void changePIN(long account_number) throws SQLException, InterruptedException{
        String changePIN_query = "UPDATE accounts SET account_pin = ? WHERE account_number = ?;";
        System.out.println("\n*** Change PIN ***");
        System.out.print("Enter Current PIN: ");
        int curr_pin = sc.nextInt();
        System.out.print("Enter New PIN: ");
        int new_pin = sc.nextInt();
        String checkPIN_query = "SELECT account_pin FROM accounts WHERE account_number = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(checkPIN_query);
        preparedStatement.setLong(1, account_number);
        ResultSet resultSet1 = preparedStatement.executeQuery();
        if(resultSet1.next()){
            PreparedStatement preparedStatement2 = connection.prepareStatement(changePIN_query);
            preparedStatement2.setInt(1, new_pin);
            preparedStatement2.setLong(2, account_number);
            System.out.print("Changing PIN");
            int i = 3;
            while(i != 0) {
                System.out.print(".");
                Thread.sleep(1000);
                i--;
            }
            int affected_rows = preparedStatement2.executeUpdate();
            if(affected_rows > 0) {
                System.out.println("\nPIN Changed Successfully!!!");
            } else {
                System.out.println("\nPIN updating failed!");
            }
        } else{
            System.out.println("Invalid PIN!");
        }
        preparedStatement.close();
        resultSet1.close();
    }

    public boolean accountExist(String email) throws SQLException {
        String isAccountAvailable = "SELECT account_number FROM accounts WHERE email = ?;";
        PreparedStatement check = connection.prepareStatement(isAccountAvailable);
        check.setString(1, email);
        ResultSet resultSet = check.executeQuery();
        return resultSet.next();
    }

}
