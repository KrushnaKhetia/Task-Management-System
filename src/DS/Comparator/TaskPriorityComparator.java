package Ds.Comparator;

import Java.Model.Task;

import java.util.Comparator;

/**
 * ---------------------------------------------------------------------
 * Class Name : TaskPriorityComparator
 *
 * Purpose:
 * Dictates the algorithmic sorting rules for the priority queue structure.
 *
 * Responsibilities:
 * - Assign weightage to textual priority states.
 * ---------------------------------------------------------------------
 */

import Java.Model.Task;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {

    /**
     * Evaluates two Task objects to determine execution order.
     *
     * Purpose:
     * Instructs the Queue data structure on how to position elements.
     *
     * Parameters:
     * t1 - The first task to compare.
     * t2 - The secondary task to compare.
     *
     * Returns:
     * A mathematical integer representing relative weight.
     */
    @Override
    public int compare(Task t1, Task t2) {
        return getPriorityValue(t1.getPriority()) - getPriorityValue(t2.getPriority());
    }

    /**
     * Converts a string priority representation to an absolute integer ranking.
     *
     * Purpose: Enables numerical comparison of qualitative data.
     */
    private int getPriorityValue(String priority) {
        if (priority.equalsIgnoreCase("High")) return 1;
        if (priority.equalsIgnoreCase("Med")) return 2;
        return 3;
    }
}

