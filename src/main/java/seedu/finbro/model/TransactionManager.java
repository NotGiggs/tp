package seedu.finbro.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Manages financial transactions such as incomes and expenses.
 * Provides functionality to add, list, filter, and clear transactions,
 * calculate total income, total expenses, and current balance.
 */
public class TransactionManager {
    private final List<Transaction> transactions;

    /**
     * Constructs a TransactionManager with an empty list of transactions.
     */
    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a transaction to the manager.
     *
     * @param transaction The transaction to add
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Lists all transactions in reverse chronological order.
     *
     * @return List of all transactions in reverse chronological order
     */
    public List<Transaction> listTransactions() {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        Collections.sort(sortedTransactions, (t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        return sortedTransactions;
    }

    /**
     * Filters transactions made between the specified start date and end date
     *
     * @param startDate the start date specified by the user for filtering
     * @param endDate   the end date specified by the user for filtering
     * @return a list of filtered transactions
     */
    public ArrayList<Transaction> getFilteredTransactions(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> (t.getDate().isEqual(startDate) || t.getDate().isAfter(startDate)) &&
                        (t.getDate().isEqual(endDate) || t.getDate().isBefore(endDate)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calculates the current balance based on all transactions.
     *
     * @return The current balance
     */
    public double getBalance() {
        double balance = 0;
        for (Transaction transaction : transactions) {
            if (transaction instanceof Income) {
                balance += transaction.getAmount();
            } else if (transaction instanceof Expense) {
                balance -= transaction.getAmount();
            }
        }
        return balance;
    }

    /**
     * Calculates the total income from all transactions.
     *
     * @return The total income
     */
    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t instanceof Income)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculates the total expenses from all transactions.
     *
     * @return The total expenses
     */
    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Clears all transactions.
     */
    public void clearTransactions() {
        transactions.clear();
    }

    /**
     * Returns the number of transactions.
     *
     * @return The number of transactions
     */
    public int getTransactionCount() {
        return transactions.size();
    }
}
