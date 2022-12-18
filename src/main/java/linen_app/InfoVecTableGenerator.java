package linen_app;

import java.util.Comparator;
import java.util.Vector;
import java.util.function.Predicate;
import javax.swing.*;

import linen_app.message_classes.MessageSerializeInterface;

/**
 * @author Austin Franklin<p>
 * Used for turning a vector of rows from a database into a JTable to be displayed.
 * Holds onto a original copy of the information, a copy for filtering, the names of the fields, and the table itself.
 * If you want to turn the headers into buttons, that must be done outside this class
 */

public class InfoVecTableGenerator<T extends MessageSerializeInterface> {
    private JTable table;
    private final Vector<T> infoVecSource;
    private Vector<T> infoVecFilter;
    private final Object[] objectFieldNames;


    /**
     * Constructor that generates the table
     * @param infoVec Vector of table rows to be displayed
     * @param dummyObject A workaround, just have some dummy object like new LinenInfo("", 0). Used for generating table column names
     */
    public InfoVecTableGenerator(Vector<T> infoVec, T dummyObject) {
    	this.infoVecSource = infoVec;
        objectFieldNames = Reflection.getClassVariableNames(dummyObject).toArray();
        resetFilter();
        buildTable();
    }

    /**
     * Builds the table using the filtered vector
     * Is called every time something is sorted or filtered
     * Make sure to call getTable() to refresh the table
     */
    private void buildTable() {
    	
        Object[][] data = new Object[infoVecFilter.size()][];
        for (int i = 0; i < infoVecFilter.size(); ++i) {
            data[i] = infoVecFilter.get(i).toStringVec().toArray();
        }
        table = new JTable(data, objectFieldNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateRowSorter(true);
    }

    /**
     * Returns the built table, use it every time something has been done to the table
     * @return
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Sorts the filtered copy
     * May be slow since java's String.compareTo(String) uses pcmpestri, which is overkill and slow
     * @param c Comparator found within T, (LinenType.sortType())
     */
    public void sortBy(Comparator<? super T> c) {
        infoVecFilter.sort(c);
        buildTable();
    }

    /**
     * Filters the filtered copy, filters out what is true
     * @param p A predicate such as a -> a.linenType().compareTo(str) != 0 (removes anything that isnt str)
     */
    public void filterBy(Predicate<? super T> p) {
        infoVecFilter = new Vector<>(infoVecFilter.stream().filter(p).toList());
        buildTable();
    }

    /**
     * Resets the filtered information by simply cloning the original
     */
    public void resetFilter() {
        infoVecFilter = new Vector<>(infoVecSource);
    }
}
