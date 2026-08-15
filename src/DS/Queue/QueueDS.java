package Ds.Queue;

import Ds.Comparator.TaskPriorityComparator;
import Java.Model.Task;
import Java.UI.Colors;
import Java.UI.UIHelper;

import java.util.PriorityQueue;

/**
 * ---------------------------------------------------------------------
 * Class Name : QueueDS
 * <p>
 * Purpose:
 * Implements a custom prioritization engine using native Java structures.
 * <p>
 * Responsibilities:
 * - Intake tasks and sort them autonomously based on priority weight.
 * - Render an ordered execution path for users.
 * ---------------------------------------------------------------------
 */

public class QueueDS {
    private PriorityQueue<Task> taskQueue = new PriorityQueue<>(new TaskPriorityComparator());
    public void addTask(Task task) { taskQueue.add(task); }
    public boolean isEmpty() { return taskQueue.isEmpty(); }

    /**
     * Drains the queue and generates an optimized layout view.
     *
     * Purpose:
     * Provides the end user with a sequential, priority-based itinerary.
     *
     * Parameters: None
     * Returns: Void
     */
    public void displayTasksInPriority() {
        if (taskQueue.isEmpty()) {
            UIHelper.printSuccess("No pending tasks!.");
            return;
        }
        UIHelper.printHeader("MY TASKS (PRIORITY WISE)");

        System.out.println(Colors.CYAN + "====================================================================================");
        System.out.println(" " + Colors.YELLOW + UIHelper.pad("ID", 4) + "   " + UIHelper.pad("TITLE", 20) + "   " + UIHelper.pad("PRIORITY", 13) + "   " + UIHelper.pad("DEADLINE", 21) + "   " + UIHelper.pad("STATUS", 15) + Colors.CYAN);
        System.out.println("------------------------------------------------------------------------------------" + Colors.RESET);

        while (!taskQueue.isEmpty()) {
            Task t = taskQueue.poll();
            String pColor = t.getPriority().equalsIgnoreCase("High") || t.getStatus().equalsIgnoreCase("Overdue") ? Colors.RED : Colors.RESET;

            System.out.println(" " + pColor + UIHelper.pad(String.valueOf(t.getTaskId()), 4) + "   " + UIHelper.pad(t.getTitle(), 20) + "   " + UIHelper.pad(UIHelper.formatPriority(t.getPriority()), 13) + "   " + UIHelper.pad(t.getDeadline().toString(), 21) + "   " + UIHelper.pad(UIHelper.formatStatus(t.getStatus()), 15) + Colors.RESET);
        }
        System.out.println(Colors.CYAN + "====================================================================================" + Colors.RESET);
    }
}

