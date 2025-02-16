package org.mycompany;

/**
 * Домашнее задание для урока "Основы тестирования на Java (JUnit)".
 * Задача: Написать функцию расчёта стоимости доставки.
 * ---
 * Создадим класс DeliveryCostCalculation с конструктором для расчёта доставки.
 */
public class Main {

    public static void main(String[] args) {
        // Проверка
        double deliveryCost1 = DeliveryCostCalculation.calculateDeliveryCost(
                1,
                DeliveryCostCalculation.Size.SMALL,
                DeliveryCostCalculation.Fragility.NOT_FRAGILE,
                DeliveryCostCalculation.Workload.INCREASED);
        System.out.println("deliveryCost1 = " + deliveryCost1);
    }

}