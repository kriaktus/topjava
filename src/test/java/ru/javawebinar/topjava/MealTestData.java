package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int TEST_USER_MEAL_ID = START_SEQ + 5;
    public static final int ANOTHER_USER_MEAL_ID = START_SEQ + 10;
    public static final Meal testUserMeal = new Meal(START_SEQ + 5, LocalDateTime.of(2022, Month.FEBRUARY, 14, 20, 0), "Ужин (User100000)", 500);
    public static final Meal testUserAnotherMeal = new Meal(START_SEQ + 4, LocalDateTime.of(2022, Month.FEBRUARY, 14, 13, 0), "Обед (User100000)", 1000);
    public static final Meal anotherUserMeal = new Meal(START_SEQ + 10, LocalDateTime.of(2022, Month.FEBRUARY, 13, 13, 0), "Обед (User100001)", 1100);

    private static final Meal testUserMeal3 = new Meal(START_SEQ + 3, LocalDateTime.of(2022, Month.FEBRUARY, 14, 8, 0), "Завтрак (User100000)", 500);
    private static final Meal testUserMeal9 = new Meal(START_SEQ + 9, LocalDateTime.of(2022, Month.FEBRUARY, 13, 23, 0), "Ужин2 (User100000)", 100);
    private static final Meal testUserMeal8 = new Meal(START_SEQ + 8, LocalDateTime.of(2022, Month.FEBRUARY, 13, 20, 0), "Ужин (User100000)", 500);
    private static final Meal testUserMeal6 = new Meal(START_SEQ + 6, LocalDateTime.of(2022, Month.FEBRUARY, 13, 8, 0), "Завтрак (User100000)", 500);
    private static final Meal testUserMeal7 = new Meal(START_SEQ + 7, LocalDateTime.of(2022, Month.FEBRUARY, 12, 13, 0), "Обед (User100000)", 1000);

    public static final List<Meal> testUserMealList = Arrays.asList(testUserMeal, testUserAnotherMeal, testUserMeal3, testUserMeal9, testUserMeal8, testUserMeal6, testUserMeal7);
    public static final List<Meal> testUserMealListFrom12To13 = Arrays.asList(testUserMeal9, testUserMeal8, testUserMeal6, testUserMeal7);

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