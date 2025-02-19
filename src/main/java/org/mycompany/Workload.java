package org.mycompany;

/**
 * Список для расчёта коэффициента загруженности службы доставки
 */
public enum Workload {
    VERY_HIGH(1.6),
    HIGH(1.4),
    INCREASED(1.2),
    OTHER(1);

    // Введём приватную константу внутри нашего перечисления
    private final double coefficient;

    // Так же необходим конструктор для этой нашей константы
    Workload(double coefficient) {
        this.coefficient = coefficient;
    }

    // Добавим метод, по которому можно будет узнать используемый коэффициент по текущей нагрузке
    public double getCoefficient() {
        return coefficient;
    }
}
