package Java.Threading;


import Dbms.Connection.DBConnection;
import Java.UI.Colors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * ---------------------------------------------------------------------
 * Class Name : DeadlineThread
 * <p>
 * Purpose:
 * Operates a daemon processing background verifications independent of explicit user interactions.
 * <p>
 * Responsibilities:
 * - Implement routine systemic scanning protocols triggering temporal violations.
 * ---------------------------------------------------------------------
 */

public class DeadlineThread extends Thread {
    private volatile boolean running = true;

    /**
     * Halts the background thread safely.
     *
     * Purpose: Prevents infinite loop execution when application terminates.
     *
     * Parameters: None
     * Returns: Void
     */
    public void stopThread() { running = false; }

    /**
     * Primary execution block for the thread daemon.
     *
     * Purpose: Continually verifies task deadlines against the current timestamp to mark overdue objects.
     *
     * Parameters: None
     * Returns: Void
     */
    @Override
    public void run() {
        while (running) {
            // Establish database connection
            try {
                Connection conn = DBConnection.getConnection();
                if (conn != null) {
                    String query = "UPDATE Tasks SET status_id = 4 WHERE status_id IN (1, 2) AND deadline <= ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

                    // Execute SQL query
                    int rows = pstmt.executeUpdate();

                    if (rows > 0) {
                        System.out.println("\n" + Colors.RED + "======================================================");
                        System.out.println(" 🔔 [ALERT] " + rows + " Task(s) just went OVERDUE! 🔔");
                        System.out.println("======================================================" + Colors.RESET + "\n> ");

                        // Insert activity log
                        PreparedStatement logPstmt = conn.prepareStatement("INSERT INTO System_Logs (action) VALUES ('System auto-updated " + rows + " task(s) to OVERDUE status')");
                        logPstmt.executeUpdate();
                    }
                    // Close resources automatically via loop termination block
                    pstmt.close();
                    conn.close();
                }
                Thread.sleep(10000);
            } catch (Exception e) {
                try { Thread.sleep(10000); } catch (InterruptedException ie) { running = false; }
            }
        }
    }
}

