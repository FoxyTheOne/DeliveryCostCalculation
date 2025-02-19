package org.mycompany.exception;

/**
 * Список сообщений для кастомных исключений
 */
public enum ExceptionMessages {
    FRAGILE_FAR_DELIVERY_EXCEPTION("Хрупкие грузы нельзя возить на расстояние более 30 км, расчёт стоимости не проводился."),
    ZERO_NO_DISTANCE_EXCEPTION("Расстояние равно 0, расчёт стоимости не проводился. Пожалуйста, оформите самовывоз."),
    NEGATIVE_NUMBER_DISTANCE_EXCEPTION("Расстояние меньше нуля, расчёт стоимости не проводился."),
    ARITHMETIC_EXCEPTION_EMPTY_FIELD("Поля не могут быть пустыми, расчёт стоимости не проводился."),
    ARITHMETIC_EXCEPTION_DELIVERY_COST_OVERFLOW("Переполнение при вычислении deliveryCost!");

    private final String exceptionMessage;

    ExceptionMessages(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
