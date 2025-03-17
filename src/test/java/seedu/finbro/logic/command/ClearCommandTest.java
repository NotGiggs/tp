package seedu.finbro.logic.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.finbro.model.TransactionManager;
import seedu.finbro.storage.Storage;
import seedu.finbro.ui.Ui;
import seedu.finbro.model.Transaction;
import seedu.finbro.model.Expense;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for ClearCommand.
 */
class ClearCommandTest {
    private TransactionManager transactionManager;
    private Ui ui;
    private Storage storage;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        transactionManager = new TransactionManager();
        ui = new Ui();
        storage = new Storage();
    }

    /**
     * Tests that the execute method returns the confirmation message when not confirmed.
     */
    @Test
    void execute_notConfirmed_returnsConfirmationMessage() {
        ClearCommand command = new ClearCommand(false);

        String result = command.execute(transactionManager, ui, storage);

        assertEquals("Are you sure you want to clear all data? This action cannot be undone.\n" +
                "Type 'clear confirm' to proceed, or simply type 'y' to confirm, 'n' to cancel.", result);
    }

    /**
     * Tests that the execute method clears transactions and returns success message when confirmed.
     */
    @Test
    void execute_confirmed_clearsTransactionsAndReturnsSuccessMessage() {
        // Create a proper Transaction object using correct parameters
        List<String> tags = new ArrayList<>();
        tags.add("Test");
        Transaction testTransaction = new Expense(100.0, "Test expense", Expense.Category.fromString(""), tags);
        transactionManager.addTransaction(testTransaction);

        ClearCommand command = new ClearCommand(true);

        String result = command.execute(transactionManager, ui, storage);

        assertEquals("All data has been cleared.", result);

        // Verify transactions were cleared
        assertEquals(0, transactionManager.getTotalExpenses());
    }

    /**
     * Tests that isExit returns false.
     */
    @Test
    void isExit_returnsFalse() {
        ClearCommand command = new ClearCommand(true);
        assertFalse(command.isExit());
    }
}
