package BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "data@ketan5678";

    public static void loader() throws InterruptedException{
        int i = 3;
        while(i != 0) {
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
    }

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("Class not found: " + e);
            e.getStackTrace();
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(connection, sc);
            Accounts accounts = new Accounts(connection, sc);
            AccountManager accountManager = new AccountManager(connection, sc);

            String email;
            long account_number = 0L;

            while(true){
                System.out.println("*** Welcome To Banking System ***\n");
                System.out.println("1. Register.");
                System.out.println("2. Login.");
                System.out.println("3. Exit.");
                System.out.print("\nEnter your choice: ");
                int choice1 = sc.nextInt();
                switch(choice1){
                    case 1:
                        user.register();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        break;
                    case 2:
                        email = user.login();
                        if(email != null){
                            System.out.print("Logging-in");
                            account_number = accounts.getAccountNumber(email);
                            int i = 3;
                            while(i != 0) {
                                System.out.print(".");
                                Thread.sleep(1000);
                                i--;
                            }
                            System.out.println("\n\n************ Welcome ************");
                            while(true) {
                                if (accounts.accountExist(email)) {
                                    System.out.println("\n*** Account Services ***");
                                    System.out.println("1. Get Account Number.");
                                    System.out.println("2. Change Account PIN.");
                                    System.out.println("3. Deposit Money.");
                                    System.out.println("4. Withdraw Money.");
                                    System.out.println("5. Transfer Money(Within Bank).");
                                    System.out.println("6. Check Balance.");
                                    System.out.println("7. Logout.");
                                } else {
                                    System.out.println("0.Open Account.");
                                }
                                System.out.print("\nEnter Choice: ");
                                int choice2 = sc.nextInt();

                                if (choice2 == 0) {
                                    accounts.openAccount(email);
                                    System.out.println("Thank you, please re-login!");
                                    Thread.sleep(2000);
                                    break;
                                } else if(choice2 == 1){
                                    System.out.println("Your account number: " + accounts.getAccountNumber(email));
                                    System.out.println("Thank you!");
                                    Thread.sleep(2000);
                                } else if(choice2 == 2){
                                    accounts.changePIN(account_number);
                                    System.out.println("Thank you!");
                                    Thread.sleep(2000);
                                } else if(choice2 == 3){
                                    accountManager.depositMoney(account_number);
                                    System.out.println("Thank you!");
                                    Thread.sleep(2000);
                                } else if (choice2 == 4){
                                    accountManager.withdrawMoney(account_number);
                                    System.out.println("Thank you!");
                                    Thread.sleep(2000);
                                } else if(choice2 == 5){
                                    accountManager.transferMoney(account_number);
                                    System.out.println("Thank you!");
                                    Thread.sleep(2000);
                                } else if(choice2 == 6){
                                    accountManager.showBalance(account_number);
                                    System.out.println("Thank you!");
                                    Thread.sleep(2000);
                                } else if(choice2 == 7){
                                    System.out.print("Logging-out");
                                    loader();
                                    System.out.println("\n");
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect email or password!");
                        }
                        break;
                    case 3:
                        System.out.println("Thank you for using banking system!!!");
                        System.out.print("Exiting System");
                        loader();
                        connection.close();
                        sc.close();
                        return;
                }
            }
        } catch(SQLException e){
            System.out.println("Connection error or SQL error: " + e);
            e.getStackTrace();
        } catch(InterruptedException e){
            System.out.println("Error in thread: " + e);
        } catch(Exception e){
            System.out.println("Seems wrong input: " + e);
        }

    }
}
