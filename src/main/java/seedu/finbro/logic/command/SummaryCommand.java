package seedu.finbro.logic.command;

import seedu.finbro.model.Expense;
import seedu.finbro.model.TransactionManager;
import seedu.finbro.storage.Storage;
import seedu.finbro.ui.Ui;

import java.util.logging.Logger;
import java.text.DateFormatSymbols;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Represents a command to view a financial summary.
 */
public class SummaryCommand implements Command {
    private static final int MAXIMUM_CATEGORIES_TO_DISPLAY = 3;
    private static final double DEFAULT_BUDGET = -1.0;
    private static final int MONTH_OFFSET = 1;
    private static final Logger logger = Logger.getLogger(SummaryCommand.class.getName());
    private final int month;
    private final int year;

    /**
     * Constructs a SummaryCommand with the specified month and end year.
     *
     * @param month the month in which transactions will be used for the summary
     * @param year the year in which transactions will be used for the summary
     */
    public SummaryCommand(int month, int year) {
        this.month = month;
        this.year = year;
    }

    /**
     * Executes the command to display a summary of the transactions in the specified month and year
     * Summary includes total income, total expenses, top 5 expense categories
     *
     * @param transactionManager The transaction manager to execute the command on
     * @param ui                 The UI to interact with the user
     * @param storage            The storage to save data
     * @return The string representation of the summary
     */
    @Override
    public String execute(TransactionManager transactionManager, Ui ui, Storage storage) {
        assert transactionManager != null : "TransactionManager cannot be null";
        assert ui != null : "UI cannot be null";
        assert storage != null : "Storage cannot be null";

        logger.info("Executing summary command");

        String monthString = new DateFormatSymbols().getMonths()[month-MONTH_OFFSET];
        logger.info(String.format("Calculating total income and total expenses for %s %d",
            monthString, year));
        String summaryDisplay = String.format("Financial Summary for %s %d:\n\n",  monthString, year);
        summaryDisplay += String.format("Total Income: $%.2f\n",
            transactionManager.getMonthlyTotalIncome(month, year));
        summaryDisplay += String.format("Total Expenses: $%.2f\n",
            transactionManager.getMonthlyTotalExpense(month, year));

        logger.info(String.format("Calculating total expenses for top categories for %s %d",
                monthString, year));
        Map<Expense.Category, Double> sortedCategorisedExpenses =
            transactionManager.getMonthlyCategorisedExpenses(month, year)
            .entrySet()
            .stream()
            .sorted(Map.Entry.<Expense.Category, Double> comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        if (!sortedCategorisedExpenses.isEmpty()) {
            summaryDisplay += "\nTop Expense Categories:\n";
            int categoryCount = 0;
            for (Map.Entry<Expense.Category, Double> expenseInCategory :
                    sortedCategorisedExpenses.entrySet()) {
                categoryCount++;
                assert expenseInCategory.getKey() != null: "Category cannot be null";
                assert expenseInCategory.getValue() <=
                        transactionManager.getMonthlyTotalExpense(month, year):
                        "Total expenses in one category cannot be greater " +
                                "than total expenses for the month";
                if (expenseInCategory.getValue() == 0) {
                    break;
                }
                summaryDisplay += String.format("%d. %s: $%.2f\n", categoryCount,
                        expenseInCategory.getKey().toString(),
                        expenseInCategory.getValue());
                if (categoryCount >= MAXIMUM_CATEGORIES_TO_DISPLAY) {
                    break;
                }
            }
        }

        logger.info(String.format("Calculating total expenses for each tag for %s %d",
                monthString, year));
        Map<String, Double> sortedTaggedTransactions =
            transactionManager.getMonthlyTaggedTransactions(month, year)
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Double> comparingByValue().reversed())
            .collect(Collectors.toMap(
                 Map.Entry::getKey,
                 Map.Entry::getValue,
                 (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //uncomment this to test set budget

        summaryDisplay += "\n";
        if (transactionManager.getBudget(month, year) == DEFAULT_BUDGET) {
            logger.info(("No budget found in hashmap"));
            summaryDisplay += String.format("No budget set for %s %d\n", monthString, year);
        } else {
            logger.info(String.format("Budget found in hashmap: $%.2f",
                transactionManager.getBudget(month, year)));
            summaryDisplay += String.format("Budget for %s %d: $%.2f\n",
                    monthString, year,
                    transactionManager.getBudget(month, year));
        }


        if (sortedTaggedTransactions.isEmpty()) {
            return summaryDisplay;
        }

        summaryDisplay += "\nTags Summary:\n";
        int tagCount = 0;
        for (Map.Entry<String, Double> expenseInTag : sortedTaggedTransactions.entrySet()) {
            tagCount++;
            assert expenseInTag.getKey() != null: "Tag cannot be null";
            assert expenseInTag.getValue() <=
                transactionManager.getMonthlyTotalExpense(month, year) +
                transactionManager.getMonthlyTotalIncome(month, year):
                "Total transaction amount in one tag cannot be greater " +
                "than total transaction amount for the month";
            if (expenseInTag.getValue() == 0) {
                break;
            }
            summaryDisplay += String.format("%d. %s: $%.2f\n", tagCount,
                expenseInTag.getKey(),
                expenseInTag.getValue());
        }

        return summaryDisplay;
    }

    /**
     * Returns false since this is not an exit command.
     *
     * @return false
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
