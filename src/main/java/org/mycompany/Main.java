package org.mycompany;

import static org.mycompany.Size.SMALL;
import static org.mycompany.Workload.INCREASED;

/**
 * Домашнее задание для урока "Основы тестирования на Java (JUnit)".
 * Задача: Написать функцию расчёта стоимости доставки.
 * ---
 * Создадим класс DeliveryCostCalculationUtils с конструктором для расчёта доставки.
 */
public class Main {

    public static void main(String[] args) {
        // Проверка
        double deliveryCost1 = DeliveryCostCalculationUtils.calculateDeliveryCost(
                1,
                SMALL,
                false,
                INCREASED);
        System.out.println("deliveryCost1 = " + deliveryCost1);
    }

}