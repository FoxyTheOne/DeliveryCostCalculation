package org.mycompany.exception;

public class FragileFarDeliveryException extends RuntimeException {

    public FragileFarDeliveryException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "FragileFarDeliveryException {"
                + "message: " + getMessage()
                + " } ";
    }

}
