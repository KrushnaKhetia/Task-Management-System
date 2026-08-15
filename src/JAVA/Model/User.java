package Java.Model;

/**
 * ---------------------------------------------------------------------
 * Class Name : User
 *
 * Purpose:
 * Represents the structured data model for a system user.
 *
 * Responsibilities:
 * - Encapsulate authentication and profile data.
 * - Provide read-only access to user attributes.
 * ---------------------------------------------------------------------
 */
public class User {
    private int userId;
    private String name, mobileNo, username, password, role;

    public User(int userId, String name, String mobileNo, String username, String password, String role) {
        this.userId = userId; this.name = name; this.mobileNo = mobileNo;
        this.username = username; this.password = password; this.role = role;
    }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getRole() { return role; }
}

