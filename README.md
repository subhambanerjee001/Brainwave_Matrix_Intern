# 💳 NeoATM — A Simple Java Swing ATM Interface

NeoATM is a fully functional **ATM simulation system** written in pure Java using **Swing GUI**, designed for students and learners. It allows users to register with a secure PIN, log in, perform transactions like deposit/withdraw, view history, and logout — all with **local file-based storage** (`users.db`).

---

## 🔧 Features

- ✅ **User Registration & Login**
- 💰 **Deposit & Withdraw Money**
- 📊 **Balance Inquiry**
- 📜 **Transaction History Tracking**
- 📝 **Local Storage in `users.db`**
- 🚪 **Smooth Logout Functionality**
- 🎯 **No JSON, No SQL, No Dependencies**

---

## 🧠 How It Works

- Users register or log in using a **4-digit PIN**
- Transactions (Deposit/Withdraw) are stored in memory and written to `users.db`
- User data is automatically **loaded at startup** and **saved on every transaction**

---

## 🚀 How to Run the Project

### 🛠️ Prerequisites:
- Java JDK 8 or higher installed

### 🔄 Compile:

```bash
javac NeoATM.java
