package seedu.finbro.logic.command;
import seedu.finbro.model.TransactionManager;
import seedu.finbro.storage.Storage;
import seedu.finbro.ui.Ui;
import java.util.logging.Logger;


public class TrackBudgetCommand implements Command {
    private static final double DEFAULT_BUDGET = -1.0;
    private static final int MONTH_OFFSET = 1;
    private final int month;
    private final int year;

    /**
     * Constructs a TrackBudgetCommand with the specified month and year.
     *
     * @param month The month for which to track the budget
     * @param year  The year for which to track the budget
     */
    public TrackBudgetCommand(int month, int year) {
        this.month = month;
        this.year = year;
    }

    /**
     * Executes the command to track a budget for a specific category.
     *
     * @param transactionManager The transaction manager to execute the command on
     * @param ui                The UI to interact with the user
     * @param storage           The storage to save data
     * @return The string representation of the result of executing the command
     */
    @Override
    public String execute(TransactionManager transactionManager, Ui ui, Storage storage) {
        assert transactionManager != null : "TransactionManager cannot be null";
        assert ui != null : "UI cannot be null";
        assert storage != null : "Storage cannot be null";

        try {
            double budget = transactionManager.getBudget(month, year);
            double totalExpense = transactionManager.getMonthlyTotalExpense(month, year);
            double totalIncome = transactionManager.getMonthlyTotalIncome(month, year);
            double remainingBudget = budget - (totalIncome - totalExpense);
            String monthString = new java.text.DateFormatSymbols().getMonths()[month - MONTH_OFFSET];


            if (budget == DEFAULT_BUDGET) {         //do I need to make this a happy case?
                return String.format("No budget set for %s %d.\n", month, year);
            } else {
                return String.format("Budget for %s %d: $%.2f\n", monthString, year,
                        transactionManager.getBudget(month, year)) + "\n" +
                        String.format("Your remaining budget for %s %d: $%.2f\n", monthString, year,
                                remainingBudget);
            }
        } catch (Exception e) {
            return "Error retrieving budget: " + e.getMessage();
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
