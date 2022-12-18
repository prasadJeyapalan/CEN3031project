package linen_app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Vector;

import org.junit.jupiter.api.Test;

/**
 * @author Austin Franklin
 * Testing reflection API usage for the easy table generator
 */
public class ReflectionTest {
    @Test
    public void parseNameTest() {
        assertEquals("A Field Like This", Reflection.parseName("aFieldLikeThis"));
        assertEquals("Get Class()", Reflection.parseName("getClass()"));
        assertEquals("An ID", Reflection.parseName("anID"));
        assertEquals("An ID Is Not Here", Reflection.parseName("anIDIsNotHere"));
    }

    @Test
    public void checkFieldParsing() {
        Vector<String> linenNames = new Vector<>(Arrays.asList("Linen Type", "Weight"));
        Vector<String> linenNamesTest = Reflection.getClassVariableNames(new linen_app.message_classes.LinenInfo("", 0.0));
        assertEquals(linenNames.size(), linenNamesTest.size());
        for (int i = 0; i < linenNames.size(); ++i) {
            assertEquals(linenNames.get(i), linenNamesTest.get(i));
        }

        Vector<String> customerName = new Vector<>(
            Arrays.asList(
                "Customer ID",
                "Customer Name",
                "Address",
                "Cost Per Pound"
            )
        );
        Vector<String> customerNamesTest = Reflection.getClassVariableNames(
            new linen_app.message_classes.CustomerInfo("", "", "", 0.0)
        );
        assertEquals(customerName.size(), customerNamesTest.size());
        for (int i = 0; i < customerName.size(); ++i) {
            assertEquals(customerName.get(i), customerNamesTest.get(i));
        }
    }
}