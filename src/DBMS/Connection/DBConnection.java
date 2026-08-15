package Dbms.Connection;


import Java.UI.UIHelper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * ---------------------------------------------------------------------
 * Class Name : DBConnection
 * <p>
 * Purpose:
 * Acts as the centralized connection factory for persistence operations.
 * <p>
 * Responsibilities:
 * - Establish and return verified active links to the MySQL engine.
 * ---------------------------------------------------------------------
 */

public class DBConnection {

    /**
     * Initializes a JDBC connection to the target database.
     *
     * Purpose:
     * Obtains a secure session required for all CRUD transactions.
     *
     * Parameters: None
     * Returns: Active Connection instance or null if the host resolves unfavorably.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/task_management_v3", "root", "");
        } catch (Exception e) {
            UIHelper.printError("DB Error: " + e.getMessage());
            return null;
        }
    }
}

