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

🔑 config.properties

(Not included for security reasons)

  Format:
  
  DB_URL=jdbc:mysql://localhost:3306/bankapp
  DB_USER=root
  DB_PASS=yourpassword

  config.properties is added to .gitignore.

Anyone running the project needs to create their own file.

▶️ How to Run

  Open the project in IntelliJ
  
  Run Login.java
  
  Add MySQL Connector/J to Project Libraries

📸 Screenshots (Check images folder...)

🚀 Future Improvements

  Admin panel, 
  Proper field validations, 
  Export passbook as PDF, 
  Dark mode UI


🤝 Feel Free to Use This

  If you're learning Java Swing or JDBC, you can clone this repo and use it however you like!
