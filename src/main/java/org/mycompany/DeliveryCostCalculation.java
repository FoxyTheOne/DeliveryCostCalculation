package org.mycompany;

import org.mycompany.exception.FragileFarDeliveryException;
import org.mycompany.exception.NoDistanceException;

/**
 * Класс для расчёта стоимости доставки.
 * Задача: написать функцию расчёта стоимости доставки. На входе функция получает:
 * - расстояние до пункта назначения,
 * - габариты,
 * - информацию о хрупкости,
 * - загруженность службы на данный момент.
 * На выходе пользователь получает стоимость доставки. Если сумма меньше минимальной стоимости доставки, выводится минимальная сумма.
 * Хрупкие грузы нельзя возить на расстояние более 30 км.
 */
public class DeliveryCostCalculation {
    // Минимальная сумма. Если сумма доставки меньше минимальной, выводится минимальная.
    // double потому что при дальнейших изменениях в сумме могут быть и копейки
    public static final double MIN_DELIVERY_SUM = 400;

    public static double calculateDeliveryCost(double distanceKm, Size size, Fragility fragility, Workload workload) {
        // Статический метод потому, что по заданию мы должны вводить данные в метод и получать стоимость доставки, без создания объекта.
        // Расстояние до пункта назначения и стоимость за расстояние считаю в double потому что расстояние может быть и 25,5 км.

        // Переменная, которую метод будет возвращать
        double deliveryCost = 0;

        // Посчитаем стоимость за расстояние до пункта назначения
        double distanceCostRub = 0;
        distanceCostRub = calculateDistanceCostRub(distanceKm);

        if (distanceCostRub <= 0) {
            // Проверить, чтобы расстояние было больше нуля
            System.out.println("Расстояние меньше либо равно 0, расчёт стоимости не проводился");
            throw new NoDistanceException("Расстояние меньше либо равно 0, расчёт стоимости не проводился");
        } else if (fragility == Fragility.FRAGILE && distanceKm > 30) {
            // Хрупкие грузы нельзя возить на расстояние более 30 км.
            System.out.println("Хрупкие грузы нельзя возить на расстояние более 30 км, расчёт стоимости не проводился");
            throw new FragileFarDeliveryException("Хрупкие грузы нельзя возить на расстояние более 30 км, расчёт стоимости не проводился");
        } else {

            // Расчёт стоимости
            deliveryCost = (distanceCostRub + size.getSizeCostRub() + fragility.getFragilityCostRub()) * workload.getCoefficient();

            System.out.println("Calculating: distanceCostRub = " + distanceCostRub
                    + ", sizeCostRub = " + size.getSizeCostRub()
                    + ", fragilityCostRub = " + fragility.getFragilityCostRub()
                    + ", workloadCoefficient = " + workload.getCoefficient() + ".");

            if (deliveryCost < MIN_DELIVERY_SUM) {
                // Если сумма меньше минимальной стоимости доставки, выводится минимальная сумма.
                System.out.println("Our deliveryCost = " + deliveryCost + " and it is < MIN_DELIVERY_SUM so it will be = " + MIN_DELIVERY_SUM);
                deliveryCost = MIN_DELIVERY_SUM;
            }

        }

//        // Это не нужно - Condition 'deliveryCost == 0' is always 'false'
//        if (deliveryCost == 0){
//            throw new NoDistanceException("Расчёт стоимости не проводился, deliveryCost = 0");
//        }

        return deliveryCost;
    }

    // Загруженность службы доставки может быть только четырёх видов. Стоимость умножается на коэффициент доставки:
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

    // Груз может быть хрупкий и не хрупкий
    public enum Fragility {
        FRAGILE(300),
        NOT_FRAGILE(0);

        private final double fragilityCostRub;

        Fragility(double fragilityCost) {
            this.fragilityCostRub = fragilityCost;
        }

        public double getFragilityCostRub() {
            return fragilityCostRub;
        }
    }

    // Габариты могут быть только большие или маленькие
    public enum Size {
        SMALL(100),
        LARGE(200);

        private final double sizeCostRub;

        Size(double sizeCost) {
            this.sizeCostRub = sizeCost;
        }

        public double getSizeCostRub() {
            return sizeCostRub;
        }
    }

    private static double calculateDistanceCostRub(double cargoDistanceKm) {
        double cargoDistanceCostRub = 0;

        // Запишем в переменную стоимость за расстояние
        if (cargoDistanceKm > 0 && cargoDistanceKm <= 2) {
            // Стоимость при расстоянии до 2 км включительно (но больше 0, потому что это доставка)
            cargoDistanceCostRub = 50;
        }
        if (cargoDistanceKm > 2 && cargoDistanceKm <= 10) {
            // Стоимость при расстоянии до 10 км включительно (но больше 2)
            cargoDistanceCostRub = 100;
        }
        if (cargoDistanceKm > 10 && cargoDistanceKm <= 30) {
            // Стоимость при расстоянии до 30 км включительно (но больше 10)
            cargoDistanceCostRub = 200;
        }
        if (cargoDistanceKm > 30) {
            // Стоимость при расстоянии более 30 км
            cargoDistanceCostRub = 300;
        }

        return cargoDistanceCostRub;
    }
}
