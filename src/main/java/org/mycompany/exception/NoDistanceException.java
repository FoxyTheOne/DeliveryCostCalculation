package org.mycompany.exception;

public class NoDistanceException extends RuntimeException {

    public NoDistanceException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "NoDistanceException {"
                + "message: " + getMessage()
                + " Расстояние должно быть больше 0, т.к. это доставка.} ";
    }

}
