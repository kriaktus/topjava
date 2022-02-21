package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int TEST_USER_ID = START_SEQ;
    public static final List<Meal> testUserMealList;
    public static final List<Meal> testUserFilteredFrom12To13MealList;
    public static final Meal testUserMeal;
    public static final int TEST_USER_MEAL_ID;
    public static final Meal testUserAnotherMeal;
    public static final Meal anotherUserMeal;
    public static final int ANOTHER_USER_MEAL_ID = START_SEQ + 10;

    static {
        testUserMealList = Arrays.asList(
                new Meal(START_SEQ + 5, LocalDateTime.of(2022, Month.FEBRUARY, 14, 20, 0), "Ужин (User100000)", 500),
                new Meal(START_SEQ + 4, LocalDateTime.of(2022, Month.FEBRUARY, 14, 13, 0), "Обед (User100000)", 1000),
                new Meal(START_SEQ + 3, LocalDateTime.of(2022, Month.FEBRUARY, 14, 8, 0), "Завтрак (User100000)", 500),
                new Meal(START_SEQ + 9, LocalDateTime.of(2022, Month.FEBRUARY, 13, 23, 0), "Ужин2 (User100000)", 100),
                new Meal(START_SEQ + 8, LocalDateTime.of(2022, Month.FEBRUARY, 13, 20, 0), "Ужин (User100000)", 500),
                new Meal(START_SEQ + 6, LocalDateTime.of(2022, Month.FEBRUARY, 13, 8, 0), "Завтрак (User100000)", 500),
                new Meal(START_SEQ + 7, LocalDateTime.of(2022, Month.FEBRUARY, 12, 13, 0), "Обед (User100000)", 1000));
        testUserFilteredFrom12To13MealList = new ArrayList<>(testUserMealList).stream()
                .filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(),
                        LocalDateTime.of(2022, Month.FEBRUARY, 12, 0, 0),
                        DateTimeUtil.atStartOfNextDayOrMax(LocalDate.of(2022, Month.FEBRUARY, 13))))
                .collect(Collectors.toList());
        testUserMeal = testUserMealList.get(3);
        TEST_USER_MEAL_ID = testUserMeal.getId();
        testUserAnotherMeal = testUserMealList.get(4);
        anotherUserMeal = new Meal(ANOTHER_USER_MEAL_ID, LocalDateTime.of(2022, Month.FEBRUARY, 13, 13, 0), "Обед (User100001)", 1100);
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.FEBRUARY, 18, 18, 0), "some text", 666);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(testUserMeal);
        updatedMeal.setDateTime(LocalDateTime.of(2170, Month.MARCH, 10, 15, 0));
        updatedMeal.setDescription("updated description");
        updatedMeal.setCalories(328);
        return updatedMeal;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expected);
    }
}
