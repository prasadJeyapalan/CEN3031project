package linen_app.message_classes;

import java.util.Comparator;
import java.util.Vector;

/**
 * Stores the unique type of linen and the weight of it
 * @author Austin Franklin
 */
public record LinenInfo(String linenType, double weight) implements MessageSerializeInterface {

    /**
     * For sorting by linen type
     */
    public static Comparator<LinenInfo> compareType() {        
        return (LinenInfo a, LinenInfo b) -> {
            return a.linenType().compareTo(b.linenType());
        };
    }

    /**
     * For sorting by linen weight
     */
    public static Comparator<LinenInfo> compareWeight() {
        return (LinenInfo a, LinenInfo b) -> {
            return Double.compare(a.weight(), b.weight());
        };
    }

    public Vector<String> toStringVec() {
        Vector<String> strVec = new Vector<>();
        strVec.add(linenType);
        strVec.add(Double.toString(weight));
        return strVec;
    }
}