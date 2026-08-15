package Java.Model;

import java.sql.Timestamp;

/**
 * ---------------------------------------------------------------------
 * Class Name : Task
 *
 * Purpose:
 * Represents the primary operational data unit within the system.
 *
 * Responsibilities:
 * - Maintain relational IDs, temporal estimates, and status strings.
 * ---------------------------------------------------------------------
 */
public class Task {
    private int taskId, estimatedHours, estMinutes, timeSpent, userId, catId;
    private String title, description, priority, status;
    private Timestamp deadline;

    public Task(int taskId, String title, String description, String priority, String status,
                int estimatedHours, int estMinutes, int timeSpent, Timestamp deadline, int userId, int catId) {
        this.taskId = taskId; this.title = title; this.description = description;
        this.priority = priority; this.status = status; this.estimatedHours = estimatedHours;
        this.estMinutes = estMinutes; this.timeSpent = timeSpent;
        this.deadline = deadline; this.userId = userId; this.catId = catId;
    }
    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public Timestamp getDeadline() { return deadline; }
    public String getDescription() { return description; }
}
