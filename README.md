# Task Management System

A console-based Task Management System developed using Java and MySQL, designed to manage users, tasks, categories, priorities, deadlines, and task status through a structured database-driven application.

## 🚀 Features

### 👨‍💼 Admin Management
- Admin login and authentication
- User management
- Add and manage task categories
- Assign tasks to users
- View and manage system tasks
- Monitor task status
- System activity logging

### 👤 User Management
- User login
- View assigned tasks
- Create and manage tasks
- Update task status
- Track task priorities and deadlines

### 📋 Task Management
- Create, update, and delete tasks
- Task categorization
- Priority-based task organization
- Deadline management
- Task status tracking
- Automatic overdue task detection

### ⚡ Priority Queue
The project uses Java's `PriorityQueue` along with a custom `Comparator` to organize tasks according to their priority.

Priority levels:
- High
- Medium
- Low

### 🧵 Multithreading
A dedicated background thread continuously checks task deadlines and automatically updates overdue tasks in the database.

### 🗄️ Database Integration
- MySQL database
- JDBC connectivity
- CRUD operations
- Prepared statements
- Database transactions
- System activity logs

### ✅ Input Validation
The system validates:
- User input
- Numeric values
- Mobile numbers
- Task priorities
- Task deadlines
- Empty input fields

## 🛠️ Technologies Used

- Java
- Object-Oriented Programming
- MySQL
- JDBC
- Data Structures
- PriorityQueue
- Comparator
- Multithreading
- Exception Handling
- SQL
- Git & GitHub

## 📚 Data Structures & Concepts

This project demonstrates practical implementation of:

- Priority Queue
- Comparator
- Collections
- Object-Oriented Programming
- Encapsulation
- Inheritance / Java OOP concepts
- Multithreading
- Exception Handling
- Database Transactions

## 📁 Project Structure

```text
Task-Management-System/
│
├── src/
│   ├── App/
│   │   └── MainApp.java
│   │
│   ├── Dbms/
│   │   └── Connection/
│   │       └── DBConnection.java
│   │
│   ├── Ds/
│   │   ├── Comparator/
│   │   │   └── TaskPriorityComparator.java
│   │   │
│   │   └── Queue/
│   │       └── QueueDS.java
│   │
│   └── Java/
│       ├── Model/
│       │   ├── Category.java
│       │   ├── Task.java
│       │   └── User.java
│       │
│       ├── Service/
│       │   └── TaskService.java
│       │
│       ├── Threading/
│       │   └── DeadlineThread.java
│       │
│       ├── UI/
│       │   ├── Colors.java
│       │   └── UIHelper.java
│       │
│       └── Validation/
│           └── Validator.java
│
├── database/
│   └── task_management_v3.sql
│
├── docs/
│   ├── ER_Diagram.png
│   └── Task_Management_System.key
│
├── README.md
└── .gitignore
```
## ▶️ How to Run

### 1. Requirements

Make sure the following are installed:

- Java JDK
- MySQL
- XAMPP
- IntelliJ IDEA or any Java-compatible IDE

### 2. Start MySQL

1. Open XAMPP.
2. Start the **MySQL** service.
3. Make sure MySQL is running on port `3306`.

### 3. Setup the Database

1. Open phpMyAdmin or MySQL.
2. Create the required database.
3. Import the SQL file from the `database` folder:

`database/task_management_v3.sql`

4. Make sure the database name matches the configuration in `DBConnection.java`.

### 4. Configure Database Connection

Open:

`src/Dbms/Connection/DBConnection.java`

The project is configured for a local MySQL/XAMPP setup.

If your MySQL configuration is different, update the database URL, username, or password accordingly.

### 5. Run the Application

Open:

`src/App/MainApp.java`

Run `MainApp.java` using IntelliJ IDEA.

Follow the instructions displayed in the console to use the Task Management System.

## 📊 Documentation

The `docs` folder contains:

- **ER Diagram** — provides an overview of the database entities and their relationships.
- **Project Presentation** — contains the academic presentation of the project.

## 👥 Team Members

- **Krushna Khetia**
- **Riya Mistry**
- **Krish Sureliya**

## 🎓 Academic Project

This project was developed as part of our academic work at **LJ University**.

## 📖 Learning Outcomes

Through this project, we gained practical experience in:

- Java application development
- Object-Oriented Programming
- Data Structures
- MySQL database design
- JDBC connectivity
- SQL and CRUD operations
- Priority Queue and Comparator
- Multithreading
- Input validation
- Exception handling
- Database transactions
- Team-based software development

## 🔮 Future Improvements

- Develop a graphical user interface
- Add email or notification support
- Add advanced task filtering and searching
- Implement user roles and permissions
- Add detailed task analytics and reports

---

⭐ **Thanks for visiting this repository!**
