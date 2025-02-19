package org.mycompany;

import org.mycompany.exception.ExceptionMessages;
import org.mycompany.exception.FragileFarDeliveryException;
import org.mycompany.exception.NoDistanceException;

/**
 * Класс для расчёта стоимости доставки исходя из определенных параметров
 */
public final class DeliveryCostCalculationUtils {

    public static final double MIN_DELIVERY_SUM = 400;

    private DeliveryCostCalculationUtils() {
        // Not called
        // If this class is only a utility class, you should make the class final and define a private constructor
        // (Additionally, you can make the class final, so that it can't be extended in subclasses, which is a best practice for utility classes. Since you declared only a private constructor, other classes wouldn't be able to extend it anyway, but it is still a best practice to mark the class as final)
    }

    /**
     * Returns delivery cost or an error, if there are wrong input
     *
     * @return calculated cost or default if calculated is less than 400
     */
    public static double calculateDeliveryCost(double distanceKm, Size size, boolean isFragile, Workload workload) {
        // Статический метод потому, что по заданию мы должны вводить данные в метод и получать стоимость доставки, без создания объекта.

        double deliveryCost;

        if (size == null || workload == null) {
            throw new ArithmeticException(ExceptionMessages.ARITHMETIC_EXCEPTION_EMPTY_FIELD.getExceptionMessage());
        }

        if (isFragile && distanceKm > 30) {
            throw new FragileFarDeliveryException(ExceptionMessages.FRAGILE_FAR_DELIVERY_EXCEPTION);
        }

        double distanceCostRub = calculateDistanceCostRub(distanceKm); // стоимость дистанции
        double sizeCostRub = size.getSizeCostRub(); // стоимость размера
        double fragilityCostRub = getFragilityCostRub(isFragile); // стоимость хрупкости
        double workloadCoefficient = workload.getCoefficient(); // коэффициент нагрузки

        // Из-за отсутствия верхней границы дальности доставки может возникнуть проблема с переполнением double. Для начала просто вручную перемножим максимально возможные числа:
        // Максимально возможное число в нашем методе - 1280 рублей. Это меньше, чем верхняя граница double и всё хорошо. Но в дальнейшем при добавлении условий и изменении цифр это может измениться
        // Поэтому добавим проверку:
        checkMaxDouble(distanceCostRub, sizeCostRub, fragilityCostRub, workloadCoefficient);

        // Расчёт стоимости
        deliveryCost = (distanceCostRub + sizeCostRub + fragilityCostRub) * workloadCoefficient;

        System.out.println("Calculating: distanceCostRub = " + distanceCostRub
                + ", sizeCostRub = " + sizeCostRub
                + ", fragilityCostRub = " + fragilityCostRub
                + ", workloadCoefficient = " + workloadCoefficient + ".");

        if (deliveryCost < MIN_DELIVERY_SUM) {
            System.out.println("Our deliveryCost = " + deliveryCost + " and it is < MIN_DELIVERY_SUM so it will be = " + MIN_DELIVERY_SUM);
            deliveryCost = MIN_DELIVERY_SUM;
        }

        return deliveryCost;
    }

    /**
     * Returns additional cost based on the fragility of the item
     *
     * @param isFragile - is item fragile or not
     * @return calculated cost, zero for a not fragile item
     */
    public static int getFragilityCostRub(boolean isFragile) {
        return isFragile ? 300 : 0;
    }

    /**
     * Метод для расчёта дополнительной стоимости за расстояние до пункта назначения
     *
     * @param cargoDistanceKm - расстояние до пункта назначения
     * @return рассчитанную стоимость за расстояние
     */
    private static double calculateDistanceCostRub(double cargoDistanceKm) {
        // !!! Стоит так же уточнить верхнюю границу доставки, не повезем же мы за 1000 км !!!

        // Пишем в таком порядке, чтобы предыдущий фильтр отсеивался (как if else, но проще для восприятия визуально)
        if (cargoDistanceKm > 30) return 300;
        if (cargoDistanceKm > 10) return 200;
        if (cargoDistanceKm > 2) return 100;
        if (cargoDistanceKm > 0) return 50;

        // Про это момент нужно уточнять у того, кто дал задачу. Но в нашем случае мы не можем, поэтому обработаем так, как поняли.
        // Раз это доставка, то 0 - это самовывоз, а не доставка. Соответственно расчёт стоимости проводить не нужно
        // !!! В зависимости от пояснений о задаче, эту строку можно объединить либо с верхней строкой, либо с нижней !!!
        if (cargoDistanceKm == 0)
            throw new NoDistanceException(ExceptionMessages.ZERO_NO_DISTANCE_EXCEPTION);

        // И на все остальные случаи - исключение
        throw new NoDistanceException(ExceptionMessages.NEGATIVE_NUMBER_DISTANCE_EXCEPTION);
    }

    /**
     * Метод для проверки переполнения double при расчётах
     * Выбрасывает исключение при переполнении
     */
    private static void checkMaxDouble(double distanceCostRub, double sizeCostRub, double fragilityCostRub, double workloadCoefficient) {
        // Проверка на каждую составляющую суммы
        if (distanceCostRub > Double.MAX_VALUE - sizeCostRub - fragilityCostRub) {
            throw new ArithmeticException(ExceptionMessages.ARITHMETIC_EXCEPTION_DELIVERY_COST_OVERFLOW.getExceptionMessage());
        }

        // Проверка перед умножением
        double sum = distanceCostRub + sizeCostRub + fragilityCostRub;
        if (sum > Double.MAX_VALUE / workloadCoefficient) {
            throw new ArithmeticException(ExceptionMessages.ARITHMETIC_EXCEPTION_DELIVERY_COST_OVERFLOW.getExceptionMessage());
        }
    }

}
