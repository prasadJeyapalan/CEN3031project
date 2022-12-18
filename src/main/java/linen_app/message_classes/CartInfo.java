package linen_app.message_classes;

import java.util.Comparator;
import java.util.Vector;

/**
 * Contains the unique information for a cart at its present state.
 * @author Austin Franklin
 */
public record CartInfo(int cartID, String location, int tareWeight, CartState state) implements MessageSerializeInterface {

    /**
     * For sorting by cart ID
     */
    public static Comparator<CartInfo> compareID() {
        return (CartInfo a, CartInfo b) -> {
            return Integer.compare(a.cartID(), b.cartID());
        };
    }

    /**
     * For sorting by location
     */
    public static Comparator<CartInfo> compareLocation() {
        return (CartInfo a, CartInfo b) -> {
            return a.location().compareTo(b.location());
        };
    }

    /**
     * For sorting by tare weight
     */
    public static Comparator<CartInfo> compareTareWeight() {
        return (CartInfo a, CartInfo b) -> {
            return Integer.compare(a.tareWeight(), b.tareWeight());
        };
    }

    /**
     * For sorting by cart state
     */
    public static Comparator<CartInfo> compareState() {
        return (CartInfo a, CartInfo b) -> {
            return a.state().toString().compareTo(
                b.state().toString()
            );
        };
    }

    public Vector<String> toStringVec() {
        Vector<String> strVec = new Vector<>();
        strVec.add(Integer.toString(cartID));
        strVec.add(location);
        strVec.add(Integer.toString(tareWeight));
        strVec.add(state.toString());
        return strVec;
    }
}