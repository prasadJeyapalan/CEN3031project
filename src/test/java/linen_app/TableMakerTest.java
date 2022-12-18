package linen_app;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Vector;
import javax.swing.JTable;

import linen_app.message_classes.LinenInfo;

/**
 * @author Austin Franklin<p>
 * Tests InfoVecTableGenerator as a singular unit
 */
public class TableMakerTest {
    @Test
    public void tableTest() {
        Vector<LinenInfo> linenVec = new Vector<>();
        linenVec.add(
            new LinenInfo("First", 1.0)
        );
        linenVec.add(
            new LinenInfo("Second", 2.0)
        );
        InfoVecTableGenerator<LinenInfo> tableGenerator = new InfoVecTableGenerator<LinenInfo>(linenVec, new LinenInfo("", 0));
        JTable table = tableGenerator.getTable();
        assertEquals(2, table.getColumnModel().getColumnCount());
        assertEquals("First", table.getValueAt(0, 0));
        assertEquals("2.0", table.getValueAt(1, 1));
        assertEquals("Linen Type", table.getColumnName(0));
    }
}
