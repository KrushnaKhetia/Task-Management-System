package App;/*
 * =====================================================================
 * Project      : Time-Bound Task Management System
 * File         : MainApp.java
 *
 * Description:
 * This file contains the comprehensive implementation of the Time-Bound
 * Task Management System. It includes the user interface utilities,
 * data structures, database connectivity models, background daemons,
 * core business logic services, and the main execution workflow for
 * both administrators and users.
 * =====================================================================
 */

import Dbms.Connection.DBConnection;
import Ds.Queue.QueueDS;
import Java.Model.Task;
import Java.Model.User;
import Java.Service.TaskService;
import Java.Threading.DeadlineThread;
import Java.UI.Colors;
import Java.UI.UIHelper;
import Java.Validation.Validator;
import java.sql.*;
import java.util.Scanner;



/**
 * ---------------------------------------------------------------------
 * Class Name : MainApp
 *
 * Purpose:
 * Evaluates core gateway interactions and structural loops integrating logic arrays over UI endpoints.
 *
 * Responsibilities:
 * - Boot application daemons.
 * - Establish primary interactive interface mapping structures.
 * - Delegate user routes based upon valid input coordinates.
 * ---------------------------------------------------------------------
 */
public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static TaskService taskService = new TaskService();

    /**
     * Initializes structural configuration nodes dictating execution flow.
     *
     * Purpose: Entry point for the application JVM routines.
     *
     * Parameters:
     * args - Command line arguments passed during execution.
     *
     * Returns: Void
     */
    public static void main(String[] args) {

        UIHelper.startupLoadingScreen();

        DeadlineThread alertThread = new DeadlineThread();
        alertThread.start();

        boolean running = true;
        while (running) {
            UIHelper.clearScreen();
            UIHelper.printBanner();

            System.out.println(Colors.BLUE + "\n========================================");
            System.out.println("              🏠 MAIN MENU              ");
            System.out.println("----------------------------------------");
            System.out.println("  1. 👨‍💼 Admin Login                     ");
            System.out.println("  2. 👨‍💻 User Login                      ");
            System.out.println("  3. 🚪 Exit                            ");
            System.out.println("========================================\n" + Colors.RESET);

            int choice = Validator.getValidInt(scanner, "➤ Select Mode: ");

            switch (choice) {
                case 1:
                    adminFlow();
                    break;
                case 2:
                    userFlow();
                    break;
                case 3:
                    alertThread.stopThread();
                    running = false;
                    UIHelper.exitAnimation();
                    break;
                default:
                    UIHelper.printError("Invalid Choice! Please select 1, 2, or 3.");
                    UIHelper.pause(scanner);
            }
        }
    }

    /**
     * Controls the overall execution flow for administrative functions.
     *
     * Purpose: Provides access to secure system manipulation tasks and reports.
     *
     * Parameters: None
     * Returns: Void
     */
    private static void adminFlow() {
        UIHelper.clearScreen();
        UIHelper.printHeader("ADMIN LOGIN");

        // Validate user input
        String user = Validator.getValidString(scanner, "👤 Username : ");
        String pass = Validator.getValidString(scanner, "🔒 Password : ");

        UIHelper.spinner("Authenticating");
        // User a1=new User();
        User admin = taskService.loginUser(user, pass);

        if (admin != null && admin.getRole().equalsIgnoreCase("Admin")) {
            UIHelper.printLoginSuccess(admin.getName());

            boolean adminRunning = true;
            while (adminRunning) {
                UIHelper.clearScreen();
                printAdminDashboard();

                System.out.println(Colors.BLUE + "\n========================================");
                System.out.println("         ⚙️ ADMIN CONTROL PANEL          ");
                System.out.println("----------------------------------------");
                System.out.println("  1. 👤 Manage Users                    ");
                System.out.println("  2. 📂 Add Category                    ");
                System.out.println("  3. 📝 Assign Task                     ");
                System.out.println("  4. 📋 View Tasks Menu                 ");
                System.out.println("  5. 🚪 Logout                          ");
                System.out.println("========================================\n" + Colors.RESET);

                int choice = Validator.getValidInt(scanner, "➤ Select Option: ");

                switch (choice) {
                    // -----------------------------------------------------
                    // Section: Manage External Users
                    // -----------------------------------------------------
                    case 1:
                        UIHelper.printHeader("MANAGE USERS");
                        int subUser = Validator.getValidInt(scanner, "1. Add User | 2. Export Users | 3. Delete User: ");
                        if (subUser == 1) {
                            String n = Validator.getValidString(scanner, "Enter Name (Will be used as Username): ");
                            String m = Validator.getValidMobile(scanner, "Enter Mobile (10 Digits): ");
                            String p = Validator.getValidString(scanner, "Set Password: ");
                            taskService.addUser(n, m, n, p, "User");
                        } else if (subUser == 2) {
                            taskService.exportUsersToFile();
                        } else if (subUser == 3) {
                            taskService.displayAllUsers();
                            int delId = Validator.getValidInt(scanner, "Enter User ID to delete: ");
                            taskService.deleteUser(delId);
                        }
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Category Administration
                    // -----------------------------------------------------
                    case 2:
                        UIHelper.printHeader("ADD CATEGORY");
                        String catName = Validator.getValidString(scanner, "New Category Name: ");
                        taskService.addCategory(catName);
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Task Generation
                    // -----------------------------------------------------
                    case 3:
                        UIHelper.printHeader("ASSIGN NEW TASK");
                        String t = Validator.getValidString(scanner, "Title: ");
                        String d = Validator.getValidString(scanner, "Description: ");
                        String pr = Validator.getValidPriority(scanner, "Priority (High/Med/Low): ");

                        int eh = Validator.getValidInt(scanner, "Estimated Hours: ");
                        int em = Validator.getValidInt(scanner, "Estimated Minutes: ");

                        String dl = Validator.getValidDeadline(scanner, "Deadline (YYYY-MM-DD HH:MM:SS): ", eh, em);

                        taskService.displayAllUsers();
                        int uid = Validator.getValidInt(scanner, "Assign to User ID: ");

                        taskService.displayAllCategories();
                        int cid = Validator.getValidInt(scanner, "Category ID: ");

                        taskService.addTask(t, d, pr, eh, em, dl, uid, cid);
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: System Reporting Views
                    // -----------------------------------------------------
                    case 4:
                        UIHelper.printHeader("VIEW TASKS");
                        int viewOpt = Validator.getValidInt(scanner, "1. View All System Tasks | 2. Search Task by User ID: ");
                        if (viewOpt == 1) {
                            taskService.displayAllTasks();
                        } else if (viewOpt == 2) {
                            taskService.displayAllUsers();
                            int searchUid = Validator.getValidInt(scanner, "Enter User ID: ");
                            taskService.displayTasksByUserId(searchUid);
                            printUserEfficiency(searchUid);
                        }
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Session Termination
                    // -----------------------------------------------------
                    case 5:
                        adminRunning = false;
                        break;
                    default:
                        UIHelper.printError("Invalid Option!");
                        UIHelper.pause(scanner);
                }
            }
        } else {
            UIHelper.printError("Invalid Username or Password.");
            UIHelper.pause(scanner);
        }
    }

    /**
     * Controls the overall execution flow for non-administrative user functions.
     *
     * Purpose: Provides access for users to update assigned tasks and personal details.
     *
     * Parameters: None
     * Returns: Void
     */
    private static void userFlow() {
        UIHelper.clearScreen();
        UIHelper.printHeader("USER LOGIN");

        // Validate user input
        int loginId = Validator.getValidInt(scanner, "👤 User ID : ");
        String loginPass = Validator.getValidString(scanner, "🔒 Password : ");

        UIHelper.spinner("Authenticating");
        User loggedInUser = taskService.loginUserById(loginId, loginPass);

        if (loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("User")) {
            UIHelper.printLoginSuccess(loggedInUser.getName());

            boolean userRunning = true;
            while (userRunning) {
                UIHelper.clearScreen();
                System.out.println(Colors.CYAN + UIHelper.getCurrentTime() + Colors.RESET);

                System.out.println(Colors.BLUE + "\n========================================");
                System.out.println("           👨‍💻 USER DASHBOARD            ");
                System.out.println("----------------------------------------");
                System.out.println("  1. 📋 My Tasks (Queue)                ");
                System.out.println("  2. ⏱ Update Progress                 ");
                System.out.println("  3. 📈 Efficiency Report               ");
                System.out.println("  4. ✏ Edit Profile                    ");
                System.out.println("  5. 🚪 Logout                          ");
                System.out.println("========================================\n" + Colors.RESET);

                UIHelper.printRandomTip();
                int choice = Validator.getValidInt(scanner, "➤ Select Option: ");

                switch (choice) {
                    // -----------------------------------------------------
                    // Section: Retrieve Current Assigments
                    // -----------------------------------------------------
                    case 1:
                        viewUserTasksInQueue(loginId);
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Manage Deliverable State
                    // -----------------------------------------------------
                    case 2:
                        UIHelper.printHeader("UPDATE PROGRESS");
                        int tid = Validator.getValidInt(scanner, "Enter Task ID: ");
                        int hrs = Validator.getValidInt(scanner, "Hours Spent Today: ");
                        int mins = Validator.getValidInt(scanner, "Minutes Spent Today: ");
                        taskService.updateTaskProgress(tid, hrs, mins, loginId);
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Retrieve Statistical Completion Data
                    // -----------------------------------------------------
                    case 3:
                        printUserEfficiency(loginId);
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Account Configuration
                    // -----------------------------------------------------
                    case 4:
                        UIHelper.printHeader("EDIT PROFILE");
                        String newName = Validator.getValidString(scanner, "Enter New Name: ");
                        String newMobile = Validator.getValidMobile(scanner, "Enter New Mobile (10 Digits): ");
                        taskService.updateUserProfile(loginId, newName, newMobile);
                        UIHelper.pause(scanner);
                        break;

                    // -----------------------------------------------------
                    // Section: Session Termination
                    // -----------------------------------------------------
                    case 5:
                        userRunning = false;
                        break;
                    default:
                        UIHelper.printError("Invalid Option!");
                        UIHelper.pause(scanner);
                }
            }
        } else {
            UIHelper.printError("Invalid User ID or Password.");
            UIHelper.pause(scanner);
        }
    }

    /**
     * Gathers aggregate metrics spanning all registered users and assigned tasks.
     *
     * Purpose: Produces a high-level executive dashboard summarizing the global system state.
     *
     * Parameters: None
     * Returns: Void
     */
    private static void printAdminDashboard() {
        String queryStatuses = "SELECT ts.status_name as status, COUNT(t.task_id) as count FROM Task_Status ts LEFT JOIN Tasks t ON ts.status_id = t.status_id GROUP BY ts.status_id";
        String queryUsers = "SELECT COUNT(*) as count FROM Users WHERE role = 'User'";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Process query result for Task Status distribution
            ResultSet rsStatuses = stmt.executeQuery(queryStatuses);
            int pending = 0, inProgress = 0, completed = 0, overdue = 0, totalTasks = 0;
            while (rsStatuses.next()) {
                String status = rsStatuses.getString("status");
                int count = rsStatuses.getInt("count");
                totalTasks += count;
                if (status.equals("Pending")) pending = count;
                else if (status.equals("In-Progress")) inProgress = count;
                else if (status.equals("Completed")) completed = count;
                else if (status.equals("Overdue")) overdue = count;
            }

            // Process query result for User distribution
            ResultSet rsUsers = stmt.executeQuery(queryUsers);
            int totalUsers = 0;
            if (rsUsers.next()) totalUsers = rsUsers.getInt("count");

            // Generate formatted table for Administrator Overview
            System.out.println(Colors.CYAN + UIHelper.getCurrentTime() + Colors.RESET);
            System.out.println(Colors.PURPLE + "=======================================================");
            System.out.println("                 📊 SYSTEM DASHBOARD");
            System.out.println("=======================================================");
            System.out.println("   👥 Total Users : " + UIHelper.pad(String.valueOf(totalUsers), 4) + "   📋 Total Tasks : " + UIHelper.pad(String.valueOf(totalTasks), 4));
            System.out.println("-------------------------------------------------------");
            System.out.println("   🟢 Pending     : " + UIHelper.pad(String.valueOf(pending), 4) + "   🟡 In-Progress : " + UIHelper.pad(String.valueOf(inProgress), 4));
            System.out.println("   🔵 Completed   : " + UIHelper.pad(String.valueOf(completed), 4) + "   🔴 Overdue     : " + UIHelper.pad(String.valueOf(overdue), 4));
            System.out.println("=======================================================" + Colors.RESET);
            UIHelper.printRandomTip();

        } catch (SQLException e) { }
    }

    /**
     * Extracts active tasks belonging to a specified user and injects them into the priority queue.
     *
     * Purpose: Forces users to view their workload sorted securely by organizational priority parameters.
     *
     * Parameters:
     * userId - Target coordinate filtering tasks designated specifically to the interacting entity.
     *
     * Returns: Void
     */
    private static void viewUserTasksInQueue(int userId) {
        QueueDS queue = new QueueDS();
        String query = "SELECT t.*, ts.status_name as status FROM Tasks t JOIN Task_Status ts ON t.status_id = ts.status_id WHERE t.user_id = ? AND ts.status_id IN (1, 2, 4)";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);

            // Execute SQL query
            ResultSet rs = pstmt.executeQuery();

            // Process query result adding components directly to custom Queue collection
            while (rs.next()) {
                queue.addTask(new Task(rs.getInt("task_id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("priority"), rs.getString("status"), rs.getInt("est_hours"), rs.getInt("est_minutes"),
                        0, rs.getTimestamp("deadline"), userId, rs.getInt("cat_id")));
            }
            queue.displayTasksInPriority();
        } catch (SQLException e) { UIHelper.printError("Error: " + e.getMessage()); }
    }

    /**
     * Executes internal mathematical calculation measuring effort submitted against base estimates.
     *
     * Purpose: Affords operational self-auditing parameters directly inside the user dashboard.
     *
     * Parameters:
     * userId - Filter parameter evaluating tasks related only to this node.
     *
     * Returns: Void
     */
    private static void printUserEfficiency(int userId) {
        String query = "SELECT t.title, t.est_hours as estimated_hours, t.est_minutes, IFNULL(SUM(p.hours_spent), 0) as th, IFNULL(SUM(p.minutes_spent), 0) as tm " +
                "FROM Tasks t LEFT JOIN Task_Progress p ON t.task_id = p.task_id " +
                "WHERE t.user_id = ? GROUP BY t.task_id";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);

            // Execute SQL query
            ResultSet rs = pstmt.executeQuery();

            UIHelper.printHeader("EFFICIENCY REPORT");

            boolean found = false;

            // Process query result
            while (rs.next()) {
                found = true;

                // Calculate task completion percentage
                int estMins = (rs.getInt("estimated_hours") * 60) + rs.getInt("est_minutes");
                int spentMins = (rs.getInt("th") * 60) + rs.getInt("tm");

                double progress = (estMins > 0) ? Math.min(100.0, ((double) spentMins / estMins) * 100) : 0;

                // Output metrics to User Interface logic
                UIHelper.printProgressBar(progress, rs.getString("title"),spentMins);
            }
            if (!found) UIHelper.printSuccess("No tasks to analyze efficiency for.");
        } catch (SQLException e) { }
    }
}