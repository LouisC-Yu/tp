package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import seedu.address.logic.commands.CommandResult;

public class CommandBoxTest {

    @BeforeAll
    public static void initJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException e) {
            // JavaFX already started
        }
    }

    @Test
    public void insertCommand_emptyExisting_replacesText() throws Exception {
        CommandBox box = createCommandBox();
        TextField field = getCommandTextField(box);

        runFxAndWait(() -> field.setText(""));
        runFxAndWait(() -> box.insertCommand("open"));

        assertEquals("open", runFxAndGet(field::getText));
    }

    @Test
    public void insertCommand_lessThanSevenWords_replacesText() throws Exception {
        CommandBox box = createCommandBox();
        TextField field = getCommandTextField(box);

        runFxAndWait(() -> field.setText("tag 3 t/veg")); // 3 words
        runFxAndWait(() -> box.insertCommand("adds n/Ah Seng p/91234567"));

        assertEquals("adds n/Ah Seng p/91234567", runFxAndGet(field::getText));
    }

    @Test
    public void insertCommand_sevenWords_appendsText() throws Exception {
        CommandBox box = createCommandBox();
        TextField field = getCommandTextField(box);

        // exactly 7 words
        runFxAndWait(() -> field.setText("a b c d e f g"));
        runFxAndWait(() -> box.insertCommand(" X"));

        assertEquals("a b c d e f g X", runFxAndGet(field::getText));
    }

    @Test
    public void insertCommand_trimsBeforeCountingWords_replacesText() throws Exception {
        CommandBox box = createCommandBox();
        TextField field = getCommandTextField(box);

        runFxAndWait(() -> field.setText("   a    b   c   ")); // 3 words after trim/split
        runFxAndWait(() -> box.insertCommand("tag 1 t/vegetable"));

        assertEquals("tag 1 t/vegetable", runFxAndGet(field::getText));
    }

    private static CommandBox createCommandBox() throws Exception {
        AtomicReference<CommandBox> ref = new AtomicReference<>();
        runFxAndWait(() -> ref.set(new CommandBox(commandText -> new CommandResult("ok"))));
        return ref.get();
    }

    private static TextField getCommandTextField(CommandBox box) throws Exception {
        Field f = CommandBox.class.getDeclaredField("commandTextField");
        f.setAccessible(true);
        return (TextField) f.get(box);
    }

    private static void runFxAndWait(Runnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable t) {
                thrown.set(t);
            } finally {
                latch.countDown();
            }
        });

        boolean ok = latch.await(5, TimeUnit.SECONDS);
        if (!ok) {
            throw new AssertionError("Timed out waiting for JavaFX thread.");
        }
        if (thrown.get() != null) {
            throw new RuntimeException(thrown.get());
        }
    }

    private static <T> T runFxAndGet(java.util.concurrent.Callable<T> action) throws Exception {
        AtomicReference<T> result = new AtomicReference<>();
        runFxAndWait(() -> {
            try {
                result.set(action.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return result.get();
    }
}
