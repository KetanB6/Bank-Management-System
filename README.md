# 🏦 Banking Management System – Java & JDBC Project

A **console-based Banking Management System** developed in **Java using JDBC and MySQL**.  
This project simulates real-world banking operations with **secure account handling, PIN-based authentication, and transaction management**.

---

## ✨ Features
- 👤 **User Management**
  - Register new users with full name, email, and password
  - Login with credentials before accessing account services  

- 🏦 **Account Management**
  - Open new bank accounts with auto-generated **account number** & secure **PIN**
  - Change PIN securely  
  - Retrieve account number  

- 💰 **Banking Transactions**
  - Deposit money with PIN verification  
  - Withdraw money securely  
  - Transfer money between accounts with rollback support if a transaction fails  
  - Show balance with PIN authentication  

- 🔐 **Security**
  - Uses `PreparedStatement` to prevent SQL injection  
  - PIN verification for every sensitive transaction  
  - JDBC transaction management for consistency  

---

## 🛠 Technologies Used
- **Java (Core + JDBC)**
- **MySQL Database**
- **Console-based UI**

---

## 📂 Project Structure
- `BankingApp` → Main entry point, handles menu navigation  
- `User` → Handles registration and login  
- `Accounts` → Manages account creation, PIN updates, and retrieval  
- `AccountManager` → Handles deposits, withdrawals, transfers, and balance checks  

---

##📸 Demo (Console Preview)

*** Welcome To Banking System ***

1. Register.
2. Login.
3. Exit.

*** Account Services ***
1. Get Account Number.
2. Change Account PIN.
3. Deposit Money.
4. Withdraw Money.
5. Transfer Money(Within Bank).
6. Check Balance.
7. Logout.
