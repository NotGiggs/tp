package seedu.finbro.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.finbro.logic.parser.Parser;
import seedu.finbro.model.TransactionManager;
import seedu.finbro.storage.Storage;
import seedu.finbro.ui.Ui;

import seedu.finbro.logic.command.ClearCommand;
import seedu.finbro.logic.command.Command;
import seedu.finbro.logic.command.ExitCommand;
import seedu.finbro.logic.command.ExportCommand;
import seedu.finbro.logic.command.FilterCommand;
import seedu.finbro.logic.command.HelpCommand;
import seedu.finbro.logic.command.IncomeCommand;
import seedu.finbro.logic.command.InvalidCommand;
import seedu.finbro.logic.command.ListCommand;
import seedu.finbro.logic.command.UnknownCommand;
import seedu.finbro.logic.command.ExpenseCommand;
import seedu.finbro.logic.command.DeleteCommand;
import seedu.finbro.logic.command.SearchCommand;
import seedu.finbro.logic.command.SummaryCommand;
import seedu.finbro.logic.command.BalanceCommand;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the Parser class.
 */
class ParserTest {
    private Parser parser;
    private TransactionManager dummyManager;
    private Ui dummyUi;
    private Storage dummyStorage;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
        dummyManager = new TransactionManager();
        dummyUi = new Ui();
        dummyStorage = new Storage();
    }

    @Test
    void parseCommand_incomeCommand_success() {
        Command command = parser.parseCommand("income 3000 d/Monthly salary t/work");
        assertTrue(command instanceof IncomeCommand);

        String result = command.execute(dummyManager, dummyUi, dummyStorage);
        assertTrue(result.contains("New income added"));
        assertTrue(result.contains("3000.00"));
        assertTrue(result.contains("Monthly salary"));
        assertTrue(result.contains("work"));
    }

    @Test
    void parseCommand_invalidIncome_returnsInvalidCommand() {
        Command command = parser.parseCommand("income d/Monthly salary t/work");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_expenseCommand_success() {
        Command command = parser.parseCommand("expense 25.50 d/Lunch c/Food t/work");
        assertTrue(command instanceof ExpenseCommand);

        String result = command.execute(dummyManager, dummyUi, dummyStorage);
        assertTrue(result.contains("New expense added"));
        assertTrue(result.contains("25.50"));
        assertTrue(result.contains("Lunch"));
        assertTrue(result.contains("Food"));
        assertTrue(result.contains("work"));
    }

    @Test
    void parseCommand_expenseWithDefaultCategory_usesOthers() {
        Command command = parser.parseCommand("expense 25.50 d/Lunch");
        assertTrue(command instanceof ExpenseCommand);

        String result = command.execute(dummyManager, dummyUi, dummyStorage);
        assertTrue(result.contains("Others"));
    }

    @Test
    void parseCommand_invalidExpense_returnsInvalidCommand() {
        Command command = parser.parseCommand("expense d/Lunch c/Food");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_listCommand_success() {
        Command command = parser.parseCommand("list");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    void parseCommand_listWithLimit_success() {
        Command command = parser.parseCommand("list n/10");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    void parseCommand_listWithDate_success() {
        Command command = parser.parseCommand("list d/2025-02-18");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    void parseCommand_listWithInvalidDate_returnsInvalidCommand() {
        Command command = parser.parseCommand("list d/invalid-date");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_deleteCommand_success() {
        Command command = parser.parseCommand("delete 1");
        assertTrue(command instanceof DeleteCommand);
    }

    @Test
    void parseCommand_invalidDelete_returnsInvalidCommand() {
        Command command = parser.parseCommand("delete");
        assertTrue(command instanceof InvalidCommand);

        command = parser.parseCommand("delete abc");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_searchCommand_success() {
        Command command = parser.parseCommand("search grocery food");
        assertTrue(command instanceof SearchCommand);
    }

    @Test
    void parseCommand_emptySearch_returnsInvalidCommand() {
        Command command = parser.parseCommand("search");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_filterCommand_success() {
        Command command = parser.parseCommand("filter d/2025-02-01 to/2025-02-18");
        assertTrue(command instanceof FilterCommand);
    }

    @Test
    void parseCommand_filterWithStartDateOnly_success() {
        Command command = parser.parseCommand("filter d/2025-02-01");
        assertTrue(command instanceof FilterCommand);
    }

    @Test
    void parseCommand_filterWithInvalidDate_returnsInvalidCommand() {
        Command command = parser.parseCommand("filter d/invalid-date");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_balanceCommand_success() {
        Command command = parser.parseCommand("balance");
        assertTrue(command instanceof BalanceCommand);
    }

    @Test
    void parseCommand_summaryCommand_success() {
        Command command = parser.parseCommand("summary");
        assertTrue(command instanceof SummaryCommand);
    }

    @Test
    void parseCommand_summaryWithMonthAndYear_success() {
        Command command = parser.parseCommand("summary m/2 y/2025");
        assertTrue(command instanceof SummaryCommand);
    }

    @Test
    void parseCommand_summaryWithInvalidMonth_returnsInvalidCommand() {
        Command command = parser.parseCommand("summary m/13");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_exportCommand_success() {
        Command command = parser.parseCommand("export");
        assertTrue(command instanceof ExportCommand);
    }

    @Test
    void parseCommand_exportWithFormat_success() {
        Command command = parser.parseCommand("export f/csv");
        assertTrue(command instanceof ExportCommand);

        command = parser.parseCommand("export f/txt");
        assertTrue(command instanceof ExportCommand);
    }

    @Test
    void parseCommand_exportWithInvalidFormat_returnsInvalidCommand() {
        Command command = parser.parseCommand("export f/pdf");
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    void parseCommand_clearCommand_success() {
        Command command = parser.parseCommand("clear");
        assertTrue(command instanceof ClearCommand);
    }

    @Test
    void parseCommand_clearWithConfirm_success() {
        Command command = parser.parseCommand("clear confirm");
        assertTrue(command instanceof ClearCommand);
    }

    @Test
    void parseCommand_exitCommand_success() {
        Command command = parser.parseCommand("exit");
        assertTrue(command instanceof ExitCommand);
        assertTrue(command.isExit());
    }

    @Test
    void parseCommand_helpCommand_success() {
        Command command = parser.parseCommand("help");
        assertTrue(command instanceof HelpCommand);
    }

    @Test
    void parseCommand_unknownCommand_returnsUnknownCommand() {
        Command command = parser.parseCommand("unknown");
        assertTrue(command instanceof UnknownCommand);
    }

    @Test
    void parseCommand_emptyInput_returnsHelpCommand() {
        Command command = parser.parseCommand("");
        assertTrue(command instanceof HelpCommand);

        command = parser.parseCommand("   ");
        assertTrue(command instanceof HelpCommand);
    }
}
