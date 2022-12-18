package linen_app.message_classes;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Contains all information regarding the beginning of an order, along with comparators for sorting.
 * @author Austin Franklin
 */
public record OrderInfo(int orderID, String customerID, long dateShippedIn, int weight, boolean active) implements MessageSerializeInterface {

    /**
     * For sorting by order ID
     */
    public static Comparator<OrderInfo> compareOrderID() {
        return (OrderInfo a, OrderInfo b) -> {
            return Integer.compare(a.orderID(), b.orderID());
        };
    }

    /**
     * For sorting by customer ID
     */
    public static Comparator<OrderInfo> compareCustomerID() {
        return (OrderInfo a, OrderInfo b) -> {
            return a.customerID().compareTo(b.customerID());
        };
    }

    /**
     * For sorting by the date shipped in
     */
    public static Comparator<OrderInfo> compareDateShippedIn() {
        return (OrderInfo a, OrderInfo b) -> {
            return Long.compare(a.dateShippedIn(), b.dateShippedIn());
        };
    }

    /**
     * For sorting by weight
     */
    public static Comparator<OrderInfo> compareWeight() {
        return (OrderInfo a, OrderInfo b) -> {
            return Integer.compare(a.weight(), b.weight());
        };
    }

    /**
     * For sorting by order completion
     */
    public static Comparator<OrderInfo> compareActive() {
        return (OrderInfo a, OrderInfo b) -> {
            return Boolean.compare(a.active(), b.active());
        };
    }

    public Vector<String> toStringVec() {
        Vector<String> strVec = new Vector<>();
        strVec.add(Integer.toString(orderID));
        strVec.add(customerID);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        strVec.add(format.format(new Date(dateShippedIn)));
        strVec.add(Integer.toString(weight));
        strVec.add(active ? "Active" : "Inactive");
        return strVec;
    }
}