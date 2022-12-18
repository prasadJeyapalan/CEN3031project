package linen_app.message_classes;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Used for entering a cart for delivery into the DB.<p>
 * Record class containing information of a SINGLE cart sent in return of a sepcific order containing an amount of a type of linen on a date. With some Comparators for sorting.
 * @author Austin Franklin
 */
public record OutboundInfoOrder(int cartID, int orderID, String linenType, int weightMeasured, long date) implements MessageSerializeInterface {

    /**
     * For sorting by cartID
     */
    public static Comparator<OutboundInfoOrder> compareCartID() {
        return (OutboundInfoOrder a, OutboundInfoOrder b) -> {
            return Integer.compare(a.cartID(), b.cartID());
        };
    }

    /**
     * For sorting by order ID
     */
    public static Comparator<OutboundInfoOrder> compareOrderID() {
        return (OutboundInfoOrder a, OutboundInfoOrder b) -> {
            return Integer.compare(a.orderID(), b.orderID());
        };
    }
    
    /**
     * For sorting by linen type
     */
    public static Comparator<OutboundInfoOrder> compareLinenType() {
        return (OutboundInfoOrder a, OutboundInfoOrder b) -> {
            return a.linenType().compareTo(b.linenType());
        };
    }

    /**
     * For sorting by weight measured
     */
    public static Comparator<OutboundInfoOrder> compareWeightMeasured() {
        return (OutboundInfoOrder a, OutboundInfoOrder b) -> {
            return Integer.compare(a.weightMeasured(), b.weightMeasured());
        };
    }

    /**
     * For sorting by date shipped out
     */
    public static Comparator<OutboundInfoOrder> compareDate() {
        return (OutboundInfoOrder a, OutboundInfoOrder b) -> {
            return Long.compare(a.date(), b.date());
        };
    }

    public Vector<String> toStringVec() {
        Vector<String> strVec = new Vector<>();
        strVec.add(Integer.toString(cartID));
        strVec.add(Integer.toString(orderID));
        strVec.add(linenType);
        strVec.add(Integer.toString(weightMeasured));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        strVec.add(format.format(new Date(date)));
        return strVec;
    }
}