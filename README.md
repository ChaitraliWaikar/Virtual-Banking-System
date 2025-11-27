# 💳 Virtual Banking System (Desktop)

### A lightweight Java + MySQL banking system built with Java Swing and IntelliJ.  
It includes login, signup, deposit, withdraw, profile settings, and transaction history — basically a simple desktop banking UI.

I created this to practice **Java Swing + JDBC** and understand how desktop apps interact with databases.

---

## ✨ Features

- Create account (Signup)  
- Login with username/password  
- Choose an account type  
- Deposit / Withdraw money  
- Check balance  
- View all transactions  
- Update profile  
- Stores everything in **MySQL**

---

## 🗂 Project Files (Important)

- **Login.java** — login screen  
- **Register.java** — create new user  
- **Dashboard.java** — main home screen  
- **Deposit.java / Withdraw.java**  
- **Passbook.java** — transaction history  
- **DBConfig.java** — loads DB info from `config.properties`

---

## 🗄 Database Setup

Create a database in MySQL:

```sql
CREATE DATABASE bankapp;
USE bankapp;
```
Inside the repo, there's a database.sql file.
Just import it — it contains users, accounts, and transactions tables.
