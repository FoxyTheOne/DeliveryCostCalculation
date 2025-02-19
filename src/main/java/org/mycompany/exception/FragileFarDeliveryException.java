package org.mycompany.exception;

public class FragileFarDeliveryException extends RuntimeException {

    public FragileFarDeliveryException(ExceptionMessages message) {
        super(message.getExceptionMessage());
    }

    @Override
    public String toString() {
        return "FragileFarDeliveryException {"
                + "message: " + getMessage()
                + " } ";
    }

}
