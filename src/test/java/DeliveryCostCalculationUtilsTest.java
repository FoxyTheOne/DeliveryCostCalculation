import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mycompany.DeliveryCostCalculationUtils;
import org.mycompany.Size;
import org.mycompany.Workload;
import org.mycompany.exception.ExceptionMessages;
import org.mycompany.exception.FragileFarDeliveryException;
import org.mycompany.exception.NoDistanceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mycompany.DeliveryCostCalculationUtils.MIN_DELIVERY_SUM;
import static org.mycompany.Size.LARGE;
import static org.mycompany.Size.SMALL;
import static org.mycompany.Workload.*;

/**
 * Задача: покрыть тестами класс DeliveryCostCalculationUtils.
 * Использовать аннотации @Test, @Tag, @ParametrizedTest, @DisplayName.
 * Применить методы assertEquals и любой другой assert.
 */
class DeliveryCostCalculationUtilsTest {

    @Test
    @Tag("Smoke")
    @DisplayName("Использование положительных значений, не хрупкий груз, стоимость доставки больше минимальной")
    void simpleDeliveryCostCalculationTest() {
        double actualDeliveryCost = DeliveryCostCalculationUtils.calculateDeliveryCost(
                25.5,
                LARGE,
                false,
                VERY_HIGH);
        double expectedDeliveryCost = (200 + 200 + 0) * 1.6;

        assertEquals(expectedDeliveryCost, actualDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @Test
    @Tag("Smoke")
    @DisplayName("Использование положительных значений, не хрупкий груз, стоимость доставки МЕНЬШЕ минимальной")
    void simpleTooCheapDeliveryTest() {
        double actualDeliveryCost = DeliveryCostCalculationUtils.calculateDeliveryCost(
                1,
                SMALL,
                false,
                OTHER);

        assertEquals(MIN_DELIVERY_SUM, actualDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @ParameterizedTest
    @Tag("Smoke")
    @DisplayName("Проверка подсчёта доставки ХРУПКОГО груза при расстоянии до 30км")
    @CsvSource({"1,50", "8,100", "25,200"})
    void fragileDeliveryCostCalculationTest(int actualDistanceKm, int expectedDistanceCostRub) {
        double actualDeliveryCost = DeliveryCostCalculationUtils.calculateDeliveryCost(
                actualDistanceKm,
                LARGE,
                true,
                VERY_HIGH);
        double calculatedDeliveryCost = (expectedDistanceCostRub + 200 + 300) * 1.6;
        // Math.max() returns the greater of two double values
        double expectedDeliveryCost = Math.max(calculatedDeliveryCost, MIN_DELIVERY_SUM);

        assertEquals(expectedDeliveryCost, actualDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @ParameterizedTest
    @Tag("Negative")
    @DisplayName("Запрос доставки ХРУПКОГО груза на расстояние БОЛЕЕ 30км")
    @ValueSource(doubles = {30.001, 31, 46, 999999999})
    void fragileFarDeliveryTest(double distance) {
        Exception thrown = assertThrows(FragileFarDeliveryException.class, () -> DeliveryCostCalculationUtils.calculateDeliveryCost(
                        distance,
                        LARGE,
                        true,
                        HIGH),
                "Должно быть выброшено исключение FragileFarDeliveryException");

        // Можем проверить текст нашего исключения:
        Assertions.assertEquals(ExceptionMessages.FRAGILE_FAR_DELIVERY_EXCEPTION.getExceptionMessage(), thrown.getMessage());
    }

    @ParameterizedTest
    @Tag("Negative")
    @DisplayName("Запрос доставки на расстояние меньше нуля")
    @ValueSource(ints = {-10, -1})
    void denyDeliveryTest(int distanceKm) {
        Exception thrown = assertThrows(NoDistanceException.class, () -> DeliveryCostCalculationUtils.calculateDeliveryCost(
                        distanceKm,
                        LARGE,
                        true,
                        HIGH),
                "Должно быть выброшено исключение NoDistanceException");

        Assertions.assertEquals(ExceptionMessages.NEGATIVE_NUMBER_DISTANCE_EXCEPTION.getExceptionMessage(), thrown.getMessage());
    }

    @Test
    @Tag("Negative")
    @DisplayName("Запрос доставки на расстояние 0")
    void denyDeliveryTest2() {
        Exception thrown = assertThrows(NoDistanceException.class, () -> DeliveryCostCalculationUtils.calculateDeliveryCost(
                        0,
                        LARGE,
                        true,
                        HIGH),
                "Должно быть выброшено исключение NoDistanceException");

        Assertions.assertEquals(ExceptionMessages.ZERO_NO_DISTANCE_EXCEPTION.getExceptionMessage(), thrown.getMessage());
    }

    @Disabled // Error converting parameter at index 1: Failed to convert String "null" to type org.mycompany.Size
    @ParameterizedTest
    @Tag("Negative")
    @DisplayName("Запрос доставки, где size = null или workload = null")
    @CsvSource({
            "1, null, true, VERY_HIGH",
            "1, LARGE, true, null"})
    void denyDeliveryTest3(double distanceKm, Size size, boolean isFragile, Workload workload) {
        Exception thrown = assertThrows(ArithmeticException.class, () -> DeliveryCostCalculationUtils.calculateDeliveryCost(
                        distanceKm,
                        size,
                        isFragile,
                        workload),
                "Должно быть выброшено исключение ArithmeticException");

        Assertions.assertEquals(ExceptionMessages.ARITHMETIC_EXCEPTION_EMPTY_FIELD.getExceptionMessage(), thrown.getMessage());
    }

    @ParameterizedTest
    @Tag("Parewise")
    @DisplayName("Проверка всех возможных комбинаций и граничных значений")
    @CsvSource({"999999999999, LARGE, false, VERY_HIGH, 800",
            "30.01, SMALL, false, HIGH, 560",
            "35, LARGE, false, INCREASED, 600",
            "40, SMALL, false, OTHER, 400",
            "10.001, SMALL, true, OTHER, 600",
            "15, LARGE, false, VERY_HIGH, 640",
            "29.99, SMALL, true, HIGH, 840",
            "30, LARGE, false, INCREASED, 480",
            "2.001, LARGE, true, INCREASED, 720",
            "5, SMALL, false, OTHER, 200",
            "9.99, LARGE, true, VERY_HIGH, 960",
            "10, SMALL, false, HIGH, 280",
            "0.001,	SMALL, true, HIGH, 630",
            "1, LARGE, false, INCREASED, 300",
            "1.99, SMALL, true, OTHER, 450",
            "2, LARGE, false, VERY_HIGH, 400"})
    void parewiseCombinationsAndBoundaryTest(double distanceKm, Size size, boolean isFragile, Workload workload, double expectedSum) {
        assertEquals(Math.max(expectedSum, MIN_DELIVERY_SUM),
                DeliveryCostCalculationUtils.calculateDeliveryCost(distanceKm, size, isFragile, workload),
                "Суммы должны быть одинаковыми");
    }

//    @Test
//    void intOverflow() {
//        int max = Integer.MAX_VALUE;
//        System.out.println(max);
//        System.out.println(max + 1);
//
//        int min = Integer.MIN_VALUE;
//        System.out.println(min);
//        System.out.println(min - 1);
//    }

}
