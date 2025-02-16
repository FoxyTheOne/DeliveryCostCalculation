import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mycompany.DeliveryCostCalculation;
import org.mycompany.exception.FragileFarDeliveryException;
import org.mycompany.exception.NoDistanceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Задача: покрыть тестами класс DeliveryCostCalculation.
 * Использовать аннотации @Test, @Tag, @ParametrizedTest, @DisplayName.
 * Применить методы assertEquals и любой другой assert.
 */

public class DeliveryCostCalculationTest {

    @Test
    @Tag("Smoke")
    @DisplayName("Использование положительных значений, не хрупкий груз, стоимость доставки больше минимальной")
    void simpleDeliveryCostCalculationTest() {
        double distanceKm = 25.5;
        DeliveryCostCalculation.Size size = DeliveryCostCalculation.Size.LARGE;
        DeliveryCostCalculation.Fragility fragility = DeliveryCostCalculation.Fragility.NOT_FRAGILE;
        DeliveryCostCalculation.Workload workload = DeliveryCostCalculation.Workload.VERY_HIGH;


        double actualDeliveryCost = DeliveryCostCalculation.calculateDeliveryCost(
                distanceKm,
                size,
                fragility,
                workload);
        double expectedDeliveryCost = (200 + 200 + 0) * 1.6;

        assertEquals(expectedDeliveryCost, actualDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @Test
    @Tag("Smoke")
    @DisplayName("Использование положительных значений, не хрупкий груз, стоимость доставки МЕНЬШЕ минимальной")
    void simpleTooCheapDeliveryTest() {
        double distanceKm = 1;
        DeliveryCostCalculation.Size size = DeliveryCostCalculation.Size.SMALL;
        DeliveryCostCalculation.Fragility fragility = DeliveryCostCalculation.Fragility.NOT_FRAGILE;
        DeliveryCostCalculation.Workload workload = DeliveryCostCalculation.Workload.OTHER;


        double actualDeliveryCost = DeliveryCostCalculation.calculateDeliveryCost(
                distanceKm,
                size,
                fragility,
                workload);

        double expectedDeliveryCost = (50 + 100 + 0) * 1;
        if (expectedDeliveryCost < DeliveryCostCalculation.MIN_DELIVERY_SUM) {
            expectedDeliveryCost = DeliveryCostCalculation.MIN_DELIVERY_SUM;
        }

        assertEquals(expectedDeliveryCost, actualDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @ParameterizedTest
    @Tag("Smoke")
    @DisplayName("Проверка подсчёта доставки ХРУПКОГО груза при расстоянии до 30км")
    @CsvSource({"1,50", "8,100", "25,200"})
    void fragileDeliveryCostCalculationTest(int actualDistanceKm, int expextedDistanceCostRub) {
        DeliveryCostCalculation.Size size = DeliveryCostCalculation.Size.LARGE;
        DeliveryCostCalculation.Fragility fragility = DeliveryCostCalculation.Fragility.FRAGILE;
        DeliveryCostCalculation.Workload workload = DeliveryCostCalculation.Workload.VERY_HIGH;


        double actualDeliveryCost = DeliveryCostCalculation.calculateDeliveryCost(
                actualDistanceKm,
                size,
                fragility,
                workload);

        double expectedDeliveryCost = (expextedDistanceCostRub + 200 + 300) * 1.6;
        if (expectedDeliveryCost < DeliveryCostCalculation.MIN_DELIVERY_SUM) {
            expectedDeliveryCost = DeliveryCostCalculation.MIN_DELIVERY_SUM;
        }

        assertEquals(expectedDeliveryCost, actualDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @Test
    @Tag("Negative")
    @DisplayName("Запрос доставки ХРУПКОГО груза на расстояние БОЛЕЕ 30км")
    void fragileFarDeliveryTest() {
        assertThrows(FragileFarDeliveryException.class, () -> DeliveryCostCalculation.calculateDeliveryCost(
                        35,
                        DeliveryCostCalculation.Size.LARGE,
                        DeliveryCostCalculation.Fragility.FRAGILE,
                        DeliveryCostCalculation.Workload.HIGH),
                "Должно быть выброшено исключение FragileFarDeliveryException");
    }

    @ParameterizedTest
    @Tag("Negative")
    @DisplayName("Запрос доставки на расстояние меньше нуля или 0")
    @ValueSource(ints = {-1, 0})
    void denyDeliveryTest(int distanceKm) {
        Exception thrown = assertThrows(NoDistanceException.class, () -> DeliveryCostCalculation.calculateDeliveryCost(
                        distanceKm,
                        DeliveryCostCalculation.Size.LARGE,
                        DeliveryCostCalculation.Fragility.FRAGILE,
                        DeliveryCostCalculation.Workload.HIGH),
                "Должно быть выброшено исключение NoDistanceException");

        // Можем проверить текст нашего исключения:
        Assertions.assertEquals("Расстояние меньше либо равно 0, расчёт стоимости не проводился", thrown.getMessage());
    }

}
