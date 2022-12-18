package linen_app.message_classes;

import java.util.Comparator;
import java.util.Vector;

/**
 * Contains infomation unique to a customer (not number of orders or anything like that), as well as comparators for sorting.
 * @author Austin Franklin
 */
public record CustomerInfo(String customerID, String customerName, String address, double costPerPound) implements MessageSerializeInterface {

    /**
     * For sorting by customer ID
     */
    public static Comparator<CustomerInfo> compareID() {
        return (CustomerInfo a, CustomerInfo b) -> {
            return a.customerID().compareTo(b.customerID());
        };
    }

    /**
     * For sorting by customer name
     */
    public static Comparator<CustomerInfo> compareName() {
        return (CustomerInfo a, CustomerInfo b) -> {
            return a.customerName().compareTo(b.customerName());
        };
    }

    /**
     * For sorting by customer address, this isnt a particularly smart comparator
     */
    public static Comparator<CustomerInfo> compareAddress() {
        return (CustomerInfo a, CustomerInfo b) -> {
            return a.address().compareTo(b.address());
        };
    }

    /**
     * For sorting by cost per pound
     */
    public static Comparator<CustomerInfo> compareCost() {
        return (CustomerInfo a, CustomerInfo b) -> {
            return Double.compare(a.costPerPound(), b.costPerPound());
        };
    }

    public Vector<String> toStringVec() {
        Vector<String> strVec = new Vector<>();
        strVec.add(customerID);
        strVec.add(customerName);
        strVec.add(address);
        strVec.add(Double.toString(costPerPound));
        return strVec;
    }
}