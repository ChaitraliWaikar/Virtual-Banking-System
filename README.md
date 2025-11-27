💳Virtual Banking System(Desktop)

This is a small Java + MySQL banking system I built using Java Swing and IntelliJ.
It has login, signup, deposit, withdraw, profile settings, and transaction history — basically a mini banking UI.

I made this mainly to practice Java Swing + JDBC and to understand how desktop apps talk to databases.

✨ What this app can do

Create account (Signup)

Login with username/password

Choose an account type

Deposit / Withdraw money

Check balance

View all transactions

Update your profile

Stores everything in MySQL

🗂 Project Files (Important ones)

Login.java — login screen

Register.java — create new user

Dashboard.java — home screen after login

Deposit.java / Withdraw.java

Passbook.java — transaction history

DBConfig.java — loads DB details from config.properties

🗄 Database Setup

Create a database in MySQL:

CREATE DATABASE bankapp;
USE bankapp;


Inside the repo, I’ve added a database.sql file.
Just import it — it has the users, accounts, and transactions tables.

🔑 config.properties

I’m not pushing my actual DB password to GitHub (obviously).
So I added this format:

DB_URL=jdbc:mysql://localhost:3306/bankapp
DB_USER=root
DB_PASS=yourpassword


And I added config.properties to .gitignore.

If someone wants to run it, they just create their own copy.

▶️ How to run

Open the project in IntelliJ → run Login.java.

You need MySQL Connector/J added to the project libraries.

📸 Screenshots

🚀 What I want to add in future

Admin panel

Proper validations

Export passbook as PDF

Dark mode UI

OTP login maybe

🤝 Feel free to use this

If you're learning Java Swing or JDBC, you can clone this repo and use it however you like.
