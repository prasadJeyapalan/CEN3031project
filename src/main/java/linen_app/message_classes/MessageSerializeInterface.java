package linen_app.message_classes;

import java.util.Vector;

/**
 * An interface for a "<T extends MessageSerializable>" class to use .toStringVec()<p>
 * Used in generic JTable creation
 * @author Austin Franklin
 */

public interface MessageSerializeInterface {
    /**
     * Interface function for message classes
     * @return A vector of strings of the fields in the class
     */
    public Vector<String> toStringVec();
}
