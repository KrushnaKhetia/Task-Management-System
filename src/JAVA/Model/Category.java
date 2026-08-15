package Java.Model;
/**
 * ---------------------------------------------------------------------
 * Class Name : Category
 *
 * Purpose:
 * Represents the structured data model for task classification.
 *
 * Responsibilities:
 * - Encapsulate category identifiers and descriptions.
 * ---------------------------------------------------------------------
 */
class Category {
    private int catId;
    private String categoryName;
    public Category(int catId, String categoryName) {
        this.catId = catId; this.categoryName = categoryName;
    }
}