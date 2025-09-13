package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static BankingApplication.BankingApp.loader;

public class User {
    private Connection connection;
    private Scanner sc;

    User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void register() throws SQLException, InterruptedException{
        System.out.println("\n***********LOGIN***********");
        sc.nextLine();
        System.out.print("Full Name: ");
        String full_name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        if(userExist(email)){
            System.out.println("User already exist for this email!");
            return;
        }
        String register_query = "INSERT INTO users VALUES(?, ?, ?);";
        PreparedStatement register = connection.prepareStatement(register_query);
        register.setString(1, full_name);
        register.setString(2, email);
        register.setString(3, password);
        int rows_affected = register.executeUpdate();
        if(rows_affected > 0){
            System.out.print("Registering User");
            loader();
            System.out.println("\nUser registered successfully!\n");
        } else{
            System.out.println("User registration failed!");
        }
        register.close();
    }

    public String login() throws SQLException{
        System.out.println("\n***********LOGIN***********");
        sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String login_query = "SELECT *FROM users WHERE email = ? AND password = ?;";
        PreparedStatement login = connection.prepareStatement(login_query);
        login.setString(1, email);
        login.setString(2, password);
        ResultSet resultSet = login.executeQuery();
        if(resultSet.next()){
            login.close();
            resultSet.close();
            return email;
        }
        login.close();
        return null;
    }

    private boolean userExist(String email) throws SQLException {
        String query = "SELECT *FROM users WHERE email = ?";
        PreparedStatement isAvailable = connection.prepareStatement(query);
        isAvailable.setString(1, email);
        ResultSet resultSet = isAvailable.executeQuery();
        return resultSet.next();
    }
}
