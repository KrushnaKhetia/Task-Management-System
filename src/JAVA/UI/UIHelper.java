package Java.UI;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * ---------------------------------------------------------------------
 * Class Name : UIHelper
 * <p>
 * Purpose:
 * Provides utility methods to manage command-line interface presentation.
 * <p>
 * Responsibilities:
 * - Screen clearing and user pauses.
 * - Loading animations and banners.
 * - Data table formatting and padding.
 * - Progress bar generation.
 * ---------------------------------------------------------------------
 */

public class UIHelper {

    /**
     * Formats a string to a fixed width to ensure consistent alignment.
     *
     * Purpose:
     * Prevents UI layout corruption caused by varying string lengths or
     * special characters by enforcing strict width constraints.
     *
     * Parameters:
     * text  - The target string to be formatted.
     * width - The required fixed length of the string.
     *
     * Returns:
     * A formatted string exactly matching the specified width.
     */
    public static String pad(String text, int width) {
        if (text == null) {
            text = "None";
        }
        if (text.length() > width) {
            return text.substring(0, width - 3) + "...";
        }
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Clears the active console screen.
     *
     * Purpose:
     * Refreshes the terminal window to simulate a clean application transition.
     *
     * Parameters: None
     * Returns: Void
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Halts execution until user interaction.
     *
     * Purpose:
     * Allows the user adequate time to read feedback messages before the
     * application clears the screen and returns to the menu.
     *
     * Parameters:
     * scanner - The active Scanner instance for capturing the newline event.
     *
     * Returns: Void
     */
    public static void pause(Scanner scanner) {
        System.out.print(Colors.PURPLE + "\nPress ENTER to continue..." + Colors.RESET);
        scanner.nextLine();
    }

    /**
     * Renders a simulated boot sequence.
     *
     * Purpose:
     * Enhances user experience by visualizing system initialization.
     *
     * Parameters: None
     * Returns: Void
     */
    public static void startupLoadingScreen() {
        clearScreen();
        System.out.println(Colors.CYAN + "\n\n\t🚀 Launching Task Management System...\n" + Colors.RESET);
        System.out.print(Colors.GREEN + "\t");
        for (int i = 0; i <= 100; i += 4) {
            System.out.print("█");
            try { Thread.sleep(60); } catch (InterruptedException e) {}
        }
        System.out.println(" 100%" + Colors.RESET);
        System.out.println(Colors.GREEN + "\t✅ System Loaded Successfully!\n" + Colors.RESET);
        try { Thread.sleep(800); } catch (InterruptedException e) {}
    }

    /**
     * Displays a dynamic waiting indicator.
     *
     * Purpose:
     * Informs the user that a background process is currently executing.
     *
     * Parameters:
     * message - The contextual action being performed.
     *
     * Returns: Void
     */
    public static void spinner(String message) {
        String[] spinnerChars = {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
        System.out.print(Colors.CYAN + "\n" + message + " ");
        for (int i = 0; i < 15; i++) {
            System.out.print(spinnerChars[i % spinnerChars.length] + "\r" + message + " ");
            try { Thread.sleep(80); } catch (InterruptedException e) {}
        }
        System.out.println(Colors.GREEN + "\r" + message + " Done!       " + Colors.RESET);
    }

    /**
     * Prints the primary application header.
     *
     * Purpose:
     * Establishes the visual identity of the system at the top of main views.
     *
     * Parameters: None
     * Returns: Void
     */
    public static void printBanner() {
        System.out.println(Colors.CYAN + "======================================================");
        System.out.println("                                                      ");
        System.out.println("       ⏳TASK MANAGEMENT SYSTEM 📋        ");
        System.out.println("                                                      ");
        System.out.println("======================================================" + Colors.RESET);
    }

    /**
     * Renders a standardized success notification.
     *
     * Purpose:
     * Ensures all positive operational feedback maintains uniform styling.
     *
     * Parameters:
     * message - The specific success details to display.
     *
     * Returns: Void
     */
    public static void printSuccess(String message) {
        System.out.println(Colors.GREEN + "\n============================================");
        System.out.println("✅ SUCCESS");
        System.out.println(message);
        System.out.println("============================================" + Colors.RESET);
    }

    /**
     * Renders a standardized error notification.
     *
     * Purpose:
     * Ensures all exceptions and invalid operations are clearly visible to the user.
     *
     * Parameters:
     * message - The specific failure details to display.
     *
     * Returns: Void
     */
    public static void printError(String message) {
        System.out.println(Colors.RED + "\n============================================");
        System.out.println("❌ ERROR");
        System.out.println(message);
        System.out.println("============================================" + Colors.RESET);
    }

    /**
     * Renders a standardized section title.
     *
     * Purpose:
     * Separates distinct operational blocks within the interface.
     *
     * Parameters:
     * title - The header text.
     *
     * Returns: Void
     */
    public static void printHeader(String title) {
        System.out.println(Colors.CYAN + "\n============================================");
        System.out.println("📋 " + title);
        System.out.println("============================================" + Colors.RESET);
    }

    /**
     * Displays specialized feedback upon successful authentication.
     *
     * Purpose:
     * Welcomes the authenticated individual before transitioning views.
     *
     * Parameters:
     * name - The authenticated user's registered name.
     *
     * Returns: Void
     */
    public static void printLoginSuccess(String name) {
        System.out.println(Colors.GREEN + "\n============================================");
        System.out.println("✅ LOGIN SUCCESSFUL");
        System.out.println("Welcome, " + name + "!");
        System.out.println("============================================" + Colors.RESET);
        try { Thread.sleep(800); } catch (InterruptedException e) {}
    }

    /**
     * Generates an inline visual representation of task completion.
     *
     * Purpose:
     * Translates numeric percentages into an intuitive bar graph format.
     *
     * Parameters:
     * percentage - The calculated completion metric.
     * label      - The associated task or metric title.
     *
     * Returns: Void
     */
    public static void printProgressBar(double percentage, String label,int spendMinutes) {
        int totalBlocks = 20;
        int filledBlocks = (int) ((percentage / 100.0) * totalBlocks);
        StringBuilder bar = new StringBuilder();

        // Construct the visual block structure based on the ratio
        for (int i = 0; i < totalBlocks; i++) {
            if (i < filledBlocks) bar.append("█");
            else bar.append("░");
        }

        int totalHrs=spendMinutes/60;
        int totalMin=spendMinutes%60;

        String statusText;
        String color;
        if (percentage >= 100) { statusText = "Excellent 🟢" + " Total Hrs Spend:"+ totalHrs +" Total Min Spend:"+totalMin; color = Colors.GREEN; }
        else if (percentage >= 75) { statusText = "Good 🟡" + " Total Hrs Spend:"+ totalHrs +" Total Min Spend:"+totalMin; color = Colors.BLUE; }
        else if (percentage >= 50) { statusText = "Average 🟠" + " Total Hrs Spend:"+ totalHrs +" Total Min Spend:"+totalMin; color = Colors.YELLOW; }
        else { statusText = "Needs Improvement 🔴" + " Total Hrs Spend:"+ totalHrs +" Total Min Spend:"+totalMin; color = Colors.RED; }

        System.out.println(pad(label, 20) + "   " + color + bar.toString() + String.format(" %.1f%%", percentage) + "   " + statusText+ Colors.RESET);
    }

    /**
     * Formats task priority levels with appropriate symbols.
     *
     * Purpose:
     * Enhances raw database strings with visual indicators for faster scanning.
     *
     * Parameters:
     * priority - The raw priority text.
     *
     * Returns: The decorated priority string.
     */
    public static String formatPriority(String priority) {
        if (priority.equalsIgnoreCase("High")) return "🔥 High";
        if (priority.equalsIgnoreCase("Med")) return "⚡ Medium";
        return "🍃 Low";
    }

    /**
     * Formats task status states with appropriate color indicators.
     *
     * Purpose:
     * Normalizes the status presentation across all data tables.
     *
     * Parameters:
     * status - The raw status identifier.
     *
     * Returns: The decorated status string.
     */
    public static String formatStatus(String status) {
        if (status.equalsIgnoreCase("Pending")) return "🟢 Pending";
        if (status.equalsIgnoreCase("In-Progress")) return "🟡 In-Progress";
        if (status.equalsIgnoreCase("Completed")) return "🔵 Completed";
        if (status.equalsIgnoreCase("Overdue")) return "🔴 Overdue";
        return status;
    }

    /**
     * Retrieves the current system timestamp in a human-readable layout.
     *
     * Purpose:
     * Provides temporal context for the interactive dashboards.
     *
     * Parameters: None
     * Returns: Formatted timestamp string.
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy | hh:mm a");
        return "🕒 " + sdf.format(new Date());
    }

    /**
     * Selects and outputs a random productivity advisory.
     *
     * Purpose:
     * Promotes best practices for end-users within the main interface.
     *
     * Parameters: None
     * Returns: Void
     */
    public static void printRandomTip() {
        String[] tips = {
                "💡 Complete High Priority Tasks First.",
                "💡 Update Progress Daily.",
                "💡 Finish Overdue Tasks Immediately.",
                "💡 Break large tasks into smaller chunks.",
                "💡 A 5-minute break every hour keeps you sharp."
        };
        System.out.println(Colors.YELLOW + "\n" + tips[new Random().nextInt(tips.length)] + Colors.RESET);
    }

    /**
     * Executes the system shutdown visual sequence.
     *
     * Purpose:
     * Provides a graceful termination experience simulating state persistence.
     *
     * Parameters: None
     * Returns: Void
     */
    public static void exitAnimation() {
        clearScreen();
        System.out.println(Colors.PURPLE + "\nSaving Logs..." + Colors.RESET);
        System.out.print(Colors.CYAN);
        for (int i = 0; i < 22; i++) {
            System.out.print("█");
            try { Thread.sleep(40); } catch (InterruptedException e) {}
        }
        System.out.println(Colors.GREEN + "\n\n✅ System Closed Successfully.");
        System.out.println("👋 Thank You For Using The Application!\n" + Colors.RESET);
    }
}

