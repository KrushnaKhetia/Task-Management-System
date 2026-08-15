package Java.Validation;
import Java.UI.Colors;
import Java.UI.UIHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * ---------------------------------------------------------------------
 * Class Name : Validator
 *
 * Purpose:
 * Contains centralized sanitation functions for standardizing client input.
 *
 * Responsibilities:
 * - Prevent invalid formats, null variables, and chronological errors.
 * ---------------------------------------------------------------------
 */
public class Validator {

    /**
     * Traps the user until a legitimate integer is supplied.
     *
     * Purpose: Prevents domain logic faults from invalid mathematical input.
     */
    public static int getValidInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(Colors.YELLOW + prompt + Colors.RESET);
            if (scanner.hasNextInt()) {
                int val = scanner.nextInt(); scanner.nextLine();
                if(val >= 0) return val;
                UIHelper.printError("Value cannot be negative!");
            } else {
                UIHelper.printError("Invalid Input! Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Enforces the existence of non-empty string variables.
     *
     * Purpose: Protects the application against blank operational parameters.
     */
    public static String getValidString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(Colors.YELLOW + prompt + Colors.RESET);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            UIHelper.printError("Input cannot be empty! Please type again.");
        }
    }

    /**
     * Evaluates telephone numbers against formatting constraints using RegEx.
     *
     * Purpose: Disallows malformed contact arrays in the Users architecture.
     */
    public static String getValidMobile(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(Colors.YELLOW + prompt + Colors.RESET);
            String input = scanner.nextLine().trim();
            if (input.matches("^[6789]\\d{9}$")) return input;
            UIHelper.printError("Invalid Mobile Number! Must be 10 digits & start with 6, 7, 8, or 9.");
        }
    }

    /**
     * Confines string declarations to acceptable systemic priority states.
     *
     * Purpose: Normalizes the critical priority metric required for Queue sorting.
     */
    public static String getValidPriority(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(Colors.YELLOW + prompt + Colors.RESET);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("High") || input.equalsIgnoreCase("Med") || input.equalsIgnoreCase("Low")) return input;
            UIHelper.printError("Invalid Priority! Enter High, Med, or Low.");
        }
    }

    /**
     * Validates chronological compliance of assigned task deadlines.
     *
     * Purpose: Restricts users from assigning task limits that expire prior to the
     * fundamental estimated labor parameters required to execute them.
     */
    public static String getValidDeadline(Scanner scanner, String prompt, int estHrs, int estMins) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        while (true) {
            System.out.print(Colors.YELLOW + prompt + Colors.RESET);
            String input = scanner.nextLine().trim();
            try {
                Date deadlineDate = sdf.parse(input);
                long diffInMillis = deadlineDate.getTime() - System.currentTimeMillis();
                long reqInMillis = ((estHrs * 60L) + estMins) * 60000L;

                if (diffInMillis < reqInMillis) {
                    UIHelper.printError("Deadline invalid! Minimum gap of " + estHrs + " hrs and " + estMins + " mins is required.");
                } else {
                    return input;
                }
            } catch (Exception e) {
                UIHelper.printError("Invalid Format! Use exactly YYYY-MM-DD HH:MM:SS");
            }
        }
    }
}

