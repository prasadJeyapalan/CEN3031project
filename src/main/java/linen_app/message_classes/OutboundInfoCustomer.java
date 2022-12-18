package linen_app.message_classes;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Used for retrieving info given a customerID (searching if a linen type was sent recently)<p>
 * Record class containing information of a SINGLE cart sent to a customer containing an amount of a type of linen on a date. With some Comparators for sorting.
 * @author Austin Franklin
 */
public record OutboundInfoCustomer(String customerID, int cartID, String linenType, int weight, long date) implements MessageSerializeInterface {

    /**
     * For sorting by customer ID
     */
    public static Comparator<OutboundInfoCustomer> compareCustomerID() {
        return (OutboundInfoCustomer a, OutboundInfoCustomer b) -> {
            return a.customerID().compareTo(b.customerID());
        };
    }

    /**
     * For sorting by cartID
     */
    public static Comparator<OutboundInfoCustomer> compareCartID() {
        return (OutboundInfoCustomer a, OutboundInfoCustomer b) -> {
            return Integer.compare(a.cartID(), b.cartID());
        };
    }
    
    /**
     * For sorting by linen type
     */
    public static Comparator<OutboundInfoCustomer> compareLinenType() {
        return (OutboundInfoCustomer a, OutboundInfoCustomer b) -> {
            return a.linenType().compareTo(b.linenType());
        };
    }

    /**
     * For sorting by weight
     */
    public static Comparator<OutboundInfoCustomer> compareWeight() {
        return (OutboundInfoCustomer a, OutboundInfoCustomer b) -> {
            return Integer.compare(a.weight(), b.weight());
        };
    }

    /**
     * For sorting by date shipped out
     */
    public static Comparator<OutboundInfoCustomer> compareDate() {
        return (OutboundInfoCustomer a, OutboundInfoCustomer b) -> {
            return Long.compare(a.date(), b.date());
        };
    }

    public Vector<String> toStringVec() {
        Vector<String> strVec = new Vector<>();
        strVec.add(customerID);
        strVec.add(Integer.toString(cartID));
        strVec.add(linenType);
        strVec.add(Integer.toString(weight));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        strVec.add(format.format(new Date(date)));
        return strVec;
    }
}