package Java.Service;


import Dbms.Connection.DBConnection;
import Java.Model.User;
import Java.UI.Colors;
import Java.UI.UIHelper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;

/**
 * ---------------------------------------------------------------------
 * Class Name : TaskService
 * <p>
 * Purpose:
 * The primary orchestrator of business rules and data manipulation.
 * <p>
 * Responsibilities:
 * - Administer user permissions and state verifications.
 * - Route DML transactions to appropriate database modules.
 * - Implement logic rollbacks enforcing ACID operational traits.
 * ---------------------------------------------------------------------
 */

public class TaskService {

    /**
     * Validates administrative and standard credentials based on alias parameters.
     *
     * Purpose: Secures the system gateway against unapproved interactions.
     *
     * Parameters:
     * username - The alias identifier.
     * password - The secret key.
     *
     * Returns: Instantiated User profile or null if rejected.
     */
    public User loginUser(String username, String password) {
        String query = "SELECT user_id, name, phone_no, username, password, role FROM Users WHERE username = ? AND password = ?";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // Process query result
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("phone_no"),
                        rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        } catch (SQLException e) { UIHelper.printError("Login Error: " + e.getMessage()); }
        return null;
    }

    /**
     * Validates credentials utilizing definitive internal keys.
     *
     * Purpose: Facilitates user pathways requiring explicit ID verification.
     *
     * Parameters:
     * userId - The database identifier.
     * password - The secret key.
     *
     * Returns: Instantiated User profile or null if rejected.
     */
    public User loginUserById(int userId, String password) {
        String query = "SELECT user_id, name, phone_no, username, password, role FROM Users WHERE user_id = ? AND password = ?";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // Process query result
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("phone_no"),
                        rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        } catch (SQLException e) { UIHelper.printError("Login Error: " + e.getMessage()); }
        return null;
    }

    /**
     * Provisions new accounts into the environment array.
     *
     * Purpose: Executes atomic commits adding user data and correlating systemic logs.
     *
     * Parameters:
     * name - Formal identity.
     * mobile - Contact array.
     * username - Alias metric.
     * password - Secret key.
     * role - Security boundary class.
     *
     * Returns: Void
     */
    public void addUser(String name, String mobile, String username, String password, String role) {
        Connection conn = DBConnection.getConnection();
        try {
            // Start transaction
            conn.setAutoCommit(false);

            String query1 = "INSERT INTO Users (name, username, password, role, phone_no) VALUES (?, ?, ?, 'User', ?)";
            PreparedStatement pstmt1 = conn.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, name);
            pstmt1.setString(2, username);
            pstmt1.setString(3, password);
            pstmt1.setString(4, mobile);

            // Execute SQL query
            pstmt1.executeUpdate();

            ResultSet rs = pstmt1.getGeneratedKeys();
            int newUserId = 0;
            if (rs.next()) newUserId = rs.getInt(1);

            // Insert activity log
            PreparedStatement logPstmt = conn.prepareStatement("INSERT INTO System_Logs (action) VALUES (?)");
            logPstmt.setString(1, "Admin added a new user: " + name);
            logPstmt.executeUpdate();

            // Commit database transaction
            conn.commit();
            UIHelper.printSuccess("User added Successfully! User ID is: " + newUserId);
        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            try { if(conn != null) conn.rollback(); } catch(SQLException ex) {}
            UIHelper.printError("Error: " + e.getMessage() + " (Transaction Rolled Back)");
        }
    }

    /**
     * Permanently eradicates accounts and related relational artifacts.
     *
     * Purpose: Removes associated tasks prior to destroying the account node to mitigate referential integrity failures.
     *
     * Parameters:
     * userId - Target removal coordinate.
     *
     * Returns: Void
     */
    public void deleteUser(int userId) {
        Connection conn = DBConnection.getConnection();
        try {
            // Start transaction
            conn.setAutoCommit(false);

            PreparedStatement pstmtTask = conn.prepareStatement("DELETE FROM Tasks WHERE user_id = ?");
            pstmtTask.setInt(1, userId);
            pstmtTask.executeUpdate();

            PreparedStatement pstmtUser = conn.prepareStatement("DELETE FROM Users WHERE user_id = ? AND role = 'User'");
            pstmtUser.setInt(1, userId);
            int rows = pstmtUser.executeUpdate();

            if (rows > 0) {
                // Insert activity log
                PreparedStatement logPstmt = conn.prepareStatement("INSERT INTO System_Logs (action) VALUES (?)");
                logPstmt.setString(1, "Admin deleted user ID: " + userId);
                logPstmt.executeUpdate();

                UIHelper.printSuccess("User ID " + userId + " deleted successfully!");
                // Commit database transaction
                conn.commit();
            } else {
                UIHelper.printError("User ID not found or cannot delete Admin.");
                // Rollback transaction if any error occurs
                conn.rollback();
            }
        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            try { if(conn != null) conn.rollback(); } catch(SQLException ex) {}
            UIHelper.printError("Error: " + e.getMessage());
        }
    }

    /**
     * Amends active configuration properties for registered user nodes.
     *
     * Purpose: Maintains data relevancy within the broader structural system.
     *
     * Parameters:
     * userId - Target configuration coordinate.
     * newName - Amended identity.
     * newMobile - Amended contact array.
     *
     * Returns: Void
     */
    public void updateUserProfile(int userId, String newName, String newMobile) {
        Connection conn = DBConnection.getConnection();
        try {
            // Start transaction
            conn.setAutoCommit(false);

            PreparedStatement pstmt1 = conn.prepareStatement("UPDATE Users SET name = ?, username = ?, phone_no = ? WHERE user_id = ?");
            pstmt1.setString(1, newName);
            pstmt1.setString(2, newName);
            pstmt1.setString(3, newMobile);
            pstmt1.setInt(4, userId);

            // Execute SQL query
            pstmt1.executeUpdate();

            // Commit database transaction
            conn.commit();
            UIHelper.printSuccess("Profile Updated Successfully!");
        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            UIHelper.printError("Error: " + e.getMessage());
        }
    }

    /**
     * Dumps the current registered population to an external text node.
     *
     * Purpose: Facilitates offline analytic evaluation of administrative states.
     *
     * Parameters: None
     * Returns: Void
     */
    public void exportUsersToFile() {
        String query = "SELECT user_id, name, role FROM Users";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             BufferedWriter writer = new BufferedWriter(new FileWriter("users_report.txt"))) {

            writer.write("--- SYSTEM USERS REPORT ---\n");

            // Process query result
            while (rs.next()) {
                writer.write("ID: " + rs.getInt("user_id") + " | Name: " + rs.getString("name") + " | Role: " + rs.getString("role") + "\n");
            }
            UIHelper.printSuccess("Users exported to 'users_report.txt' successfully!");
        } catch (Exception e) { UIHelper.printError("Error: " + e.getMessage()); }
    }

    /**
     * Incorporates new classification schemas to the active architecture.
     *
     * Purpose: Broadens the operational metadata structure for distinct task allocations.
     *
     * Parameters:
     * name - Category descriptor.
     *
     * Returns: Void
     */
    public void addCategory(String name) {
        String query = "INSERT INTO Categories (cat_name) VALUES (?)";
        // Establish database connection
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            // Execute SQL query
            pstmt.executeUpdate();
            UIHelper.printSuccess("Category added successfully!");
        } catch (SQLException e) { UIHelper.printError("Error: " + e.getMessage()); }
    }

    /**
     * Retrieves and displays the comprehensive list of non-admin nodes.
     *
     * Purpose: Affords managers perspective prior to issuing direct operational edits.
     *
     * Parameters: None
     * Returns: Void
     */
    public void displayAllUsers() {
        // Establish database connection
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT user_id, name FROM Users WHERE role='User'")) {

            // Generate formatted table
            UIHelper.printHeader("AVAILABLE USERS");
            System.out.println(Colors.CYAN + "=======================================");
            System.out.println(" " + Colors.YELLOW + UIHelper.pad("ID", 4) + "   " + UIHelper.pad("NAME", 20) + Colors.CYAN);
            System.out.println("---------------------------------------" + Colors.RESET);

            // Process query result
            while (rs.next()) {
                System.out.println(" " + Colors.RESET + UIHelper.pad(String.valueOf(rs.getInt("user_id")), 4) + "   " + UIHelper.pad(rs.getString("name"), 20) + Colors.RESET);
            }
            System.out.println(Colors.CYAN + "=======================================" + Colors.RESET);
        } catch (SQLException e) { }
    }

    /**
     * Outputs all recognized system categories.
     *
     * Purpose: Facilitates the correct input parameters while generating new deliverables.
     *
     * Parameters: None
     * Returns: Void
     */
    public void displayAllCategories() {
        // Establish database connection
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Categories")) {

            // Generate formatted table
            UIHelper.printHeader("AVAILABLE CATEGORIES");
            System.out.println(Colors.CYAN + "=======================================");
            System.out.println(" " + Colors.YELLOW + UIHelper.pad("ID", 4) + "   " + UIHelper.pad("CATEGORY NAME", 20) + Colors.CYAN);
            System.out.println("---------------------------------------" + Colors.RESET);

            // Process query result
            while (rs.next()) {
                System.out.println(" " + Colors.RESET + UIHelper.pad(String.valueOf(rs.getInt("cat_id")), 4) + "   " + UIHelper.pad(rs.getString("cat_name"), 20) + Colors.RESET);
            }
            System.out.println(Colors.CYAN + "=======================================" + Colors.RESET);
        } catch (SQLException e) { }
    }

    /**
     * Renders a global overview of every task instance in the active environment.
     *
     * Purpose: Extrapolates detailed metadata via extensive SQL Joins and maps it into a highly structured visual array.
     *
     * Parameters: None
     * Returns: Void
     */
    public void displayAllTasks() {
        String query = "SELECT t.task_id, t.title, t.priority, ts.status_name as status, u.name as user_name, c.cat_name as category_name " +
                "FROM Tasks t " +
                "JOIN Users u ON t.user_id = u.user_id " +
                "LEFT JOIN Categories c ON t.cat_id = c.cat_id " +
                "JOIN Task_Status ts ON t.status_id = ts.status_id";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Generate formatted table
            UIHelper.printHeader("ALL TASKS IN SYSTEM");

            System.out.println(Colors.CYAN + "==================================================================================================");
            System.out.println(" " + Colors.YELLOW + UIHelper.pad("ID", 3) + "   " + UIHelper.pad("TITLE", 18) + "   " + UIHelper.pad("ASSIGNED TO", 13) + "   " + UIHelper.pad("CATEGORY", 13) + "   " + UIHelper.pad("PRIORITY", 12) + "   " + UIHelper.pad("STATUS", 13) + Colors.CYAN);
            System.out.println("--------------------------------------------------------------------------------------------------" + Colors.RESET);

            boolean hasData = false;

            // Process query result
            while (rs.next()) {
                hasData = true;
                String pColor = rs.getString("priority").equalsIgnoreCase("High") ? Colors.RED : Colors.RESET;

                System.out.println(" " + pColor + UIHelper.pad(String.valueOf(rs.getInt("task_id")), 3) + "   " + UIHelper.pad(rs.getString("title"), 18) + "   " + UIHelper.pad(rs.getString("user_name"), 13) + "   " + UIHelper.pad(rs.getString("category_name"), 13) + "   " + UIHelper.pad(UIHelper.formatPriority(rs.getString("priority")), 12) + "   " + UIHelper.pad(UIHelper.formatStatus(rs.getString("status")), 13) + Colors.RESET);
            }
            System.out.println(Colors.CYAN + "==================================================================================================" + Colors.RESET);
            if (!hasData) UIHelper.printSuccess("There is No task in the System Right Now!");
        } catch (SQLException e) { UIHelper.printError("Error: " + e.getMessage()); }
    }

    /**
     * Executes localized target views resolving specific user loads.
     *
     * Purpose: Provides constrained administrative investigation logic identifying individual assignment densities.
     *
     * Parameters:
     * userId - Coordinate mapping the required target data boundary.
     *
     * Returns: Void
     */
    public void displayTasksByUserId(int userId) {
        String query = "SELECT t.task_id, t.title, t.priority, ts.status_name as status, c.cat_name as category_name " +
                "FROM Tasks t " +
                "LEFT JOIN Categories c ON t.cat_id = c.cat_id " +
                "JOIN Task_Status ts ON t.status_id = ts.status_id WHERE t.user_id = ?";

        // Establish database connection
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);

            // Execute SQL query
            ResultSet rs = pstmt.executeQuery();

            // Generate formatted table
            UIHelper.printHeader("TASKS FOR USER ID " + userId);

            System.out.println(Colors.CYAN + "===============================================================================");
            System.out.println(" " + Colors.YELLOW + UIHelper.pad("ID", 3) + "   " + UIHelper.pad("TITLE", 18) + "   " + UIHelper.pad("CATEGORY", 13) + "   " + UIHelper.pad("PRIORITY", 12) + "   " + UIHelper.pad("STATUS", 13) + Colors.CYAN);
            System.out.println("-------------------------------------------------------------------------------" + Colors.RESET);

            boolean hasData = false;

            // Process query result
            while (rs.next()) {
                hasData = true;
                String pColor = rs.getString("priority").equalsIgnoreCase("High") ? Colors.RED : Colors.RESET;

                System.out.println(" " + pColor + UIHelper.pad(String.valueOf(rs.getInt("task_id")), 3) + "   " + UIHelper.pad(rs.getString("title"), 18) + "   " + UIHelper.pad(rs.getString("category_name"), 13) + "   " + UIHelper.pad(UIHelper.formatPriority(rs.getString("priority")), 12) + "   " + UIHelper.pad(UIHelper.formatStatus(rs.getString("status")), 13) + Colors.RESET);
            }
            System.out.println(Colors.CYAN + "===============================================================================" + Colors.RESET);
            if (!hasData) UIHelper.printSuccess("No task assign to this User!");
        } catch (SQLException e) { UIHelper.printError("Error: " + e.getMessage()); }
    }

    /**
     * Constructs and initializes new assignment variables into system.
     *
     * Purpose: Injects primary operational nodes securely while triggering internal tracking and signaling logic routines.
     *
     * Parameters:
     * title - Headline definition.
     * desc - Expanded parameter detail.
     * priority - Importance sorting value.
     * estHours - Allocated margin constraints (Hours).
     * estMins - Allocated margin constraints (Minutes).
     * deadline - Absolute temporal closure variable.
     * userId - Assigned recipient mapping.
     * catId - Broad category descriptor ID.
     *
     * Returns: Void
     */
    public void addTask(String title, String desc, String priority, int estHours, int estMins, String deadline, int userId, int catId) {
        Connection conn = DBConnection.getConnection();
        try {
            // Start transaction
            conn.setAutoCommit(false);

            String query1 = "INSERT INTO Tasks (title, description, priority, est_hours, est_minutes, deadline, cat_id, status_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?)";
            PreparedStatement pstmt1 = conn.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, title);
            pstmt1.setString(2, desc);
            pstmt1.setString(3, priority);
            pstmt1.setInt(4, estHours);
            pstmt1.setInt(5, estMins);
            pstmt1.setString(6, deadline);
            pstmt1.setInt(7, catId);
            pstmt1.setInt(8, userId);

            // Execute SQL query
            pstmt1.executeUpdate();

            // Process query result
            ResultSet rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                int newTaskId = rs.getInt(1);

                // Insert activity log
                PreparedStatement logPstmt = conn.prepareStatement("INSERT INTO System_Logs (action) VALUES (?)");
                logPstmt.setString(1, "Task Assigned: ID " + newTaskId + " to User ID: " + userId);
                logPstmt.executeUpdate();

                PreparedStatement notifPstmt = conn.prepareStatement("INSERT INTO Notifications (user_id, msg) VALUES (?, ?)");
                notifPstmt.setInt(1, userId);
                notifPstmt.setString(2, "New task assigned: " + title);
                notifPstmt.executeUpdate();
            }

            // Commit database transaction
            conn.commit();
            UIHelper.printSuccess("Task assigned to User ID " + userId + " Successfully!");
        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            UIHelper.printError("Error adding task: " + e.getMessage());
        }
    }

    /**
     * Validates and integrates dynamic progression logic into existing nodes.
     *
     * Purpose: Calculates submitted time efforts against base estimates and automatically dictates contextual changes to absolute task statuses.
     *
     * Parameters:
     * taskId - Systemic coordinate to modify.
     * hoursSpent - Temporal increment variable (Hours).
     * minsSpent - Temporal increment variable (Minutes).
     * loggedInUserId - Identifier to prohibit cross-account modifications.
     *
     * Returns: Void
     */
    public void updateTaskProgress(int taskId, int hoursSpent, int minsSpent, int loggedInUserId) {
        Connection conn = DBConnection.getConnection();
        try {
            // -----------------------------------------------------
            // Step 1 : Validate User Ownership
            // -----------------------------------------------------
            PreparedStatement checkOwnership = conn.prepareStatement("SELECT * FROM Tasks WHERE task_id = ? AND user_id = ?");
            checkOwnership.setInt(1, taskId);
            checkOwnership.setInt(2, loggedInUserId);
            if (!checkOwnership.executeQuery().next()) {
                UIHelper.printError("This is not your task! Or task id is wrong!.");
                return;
            }

            // Start transaction
            conn.setAutoCommit(false);

            // -----------------------------------------------------
            // Step 2 : Insert Progress Log
            // -----------------------------------------------------
            PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO Task_Progress (task_id, hours_spent, minutes_spent) VALUES (?, ?, ?)");
            pstmt1.setInt(1, taskId);
            pstmt1.setInt(2, hoursSpent);
            pstmt1.setInt(3, minsSpent);
            pstmt1.executeUpdate();

            // -----------------------------------------------------
            // Step 3 : Calculate Task Completion Percentage
            // -----------------------------------------------------
            PreparedStatement pstmt2 = conn.prepareStatement(
                    "SELECT t.est_hours, t.est_minutes, IFNULL(SUM(p.hours_spent),0) as total_h, IFNULL(SUM(p.minutes_spent),0) as total_m FROM Tasks t LEFT JOIN Task_Progress p ON t.task_id = p.task_id WHERE t.task_id = ? GROUP BY t.task_id"
            );
            pstmt2.setInt(1, taskId);
            ResultSet rs = pstmt2.executeQuery();

            // Process query result
            if(rs.next()) {
                int estTotalMins = (rs.getInt("est_hours") * 60) + rs.getInt("est_minutes");
                int spentTotalMins = (rs.getInt("total_h") * 60) + rs.getInt("total_m");

                int displayHrs = spentTotalMins / 60;
                int displayMins = spentTotalMins % 60;

                // -----------------------------------------------------
                // Step 4 : Update Task Status
                // -----------------------------------------------------
                if (spentTotalMins >= estTotalMins) {
                    PreparedStatement pstmt3 = conn.prepareStatement("UPDATE Tasks SET status_id = 3 WHERE task_id = ?");
                    pstmt3.setInt(1, taskId);
                    pstmt3.executeUpdate();
                    UIHelper.printSuccess("🎉 Task " + taskId + " has reached estimated time and is marked as Completed!\nTotal time spent: " + displayHrs + " hrs " + displayMins + " mins");

                } else {
                    conn.prepareStatement("UPDATE Tasks SET status_id = 2 WHERE task_id = " + taskId + " AND status_id IN (1, 4)").executeUpdate();
                    UIHelper.printSuccess("Progress updated! Total time spent: " + displayHrs + " hrs " + displayMins + " mins");
                }
            }

            // Commit database transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            UIHelper.printError("Error: " + e.getMessage());
        }
    }
}

