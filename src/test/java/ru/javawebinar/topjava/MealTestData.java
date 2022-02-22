package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static final Meal userMeal1 = new Meal(START_SEQ + 3, LocalDateTime.of(2022, Month.FEBRUARY, 14, 8, 0), "Завтрак (User100000)", 500);
    private static final Meal userMeal2 = new Meal(START_SEQ + 4, LocalDateTime.of(2022, Month.FEBRUARY, 14, 13, 0), "Обед (User100000)", 1000);
    private static final Meal userMeal3 = new Meal(START_SEQ + 5, LocalDateTime.of(2022, Month.FEBRUARY, 14, 20, 0), "Ужин (User100000)", 500);
    private static final Meal userMeal4 = new Meal(START_SEQ + 6, LocalDateTime.of(2022, Month.FEBRUARY, 13, 8, 0), "Завтрак (User100000)", 500);
    private static final Meal userMeal5 = new Meal(START_SEQ + 7, LocalDateTime.of(2022, Month.FEBRUARY, 12, 13, 0), "Обед (User100000)", 1000);
    private static final Meal userMeal6 = new Meal(START_SEQ + 8, LocalDateTime.of(2022, Month.FEBRUARY, 13, 20, 0), "Ужин (User100000)", 500);
    private static final Meal userMeal7 = new Meal(START_SEQ + 9, LocalDateTime.of(2022, Month.FEBRUARY, 13, 23, 0), "Ужин2 (User100000)", 100);

    public static final int USER_MEAL_ID = START_SEQ + 5;
    public static final Meal userMeal = userMeal3;
    public static final Meal userAnotherMeal = userMeal2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 10;
    public static final Meal adminMeal = new Meal(START_SEQ + 10, LocalDateTime.of(2022, Month.FEBRUARY, 13, 13, 0), "Обед (User100001)", 1100);
    public static final int NON_EXIST_MEAL_ID = Integer.MAX_VALUE;

    public static final List<Meal> userMealList = Arrays.asList(userMeal3, userMeal2, userMeal1, userMeal7, userMeal6, userMeal4, userMeal5);
    public static final List<Meal> userMealListFrom12To13 = Arrays.asList(userMeal7, userMeal6, userMeal4, userMeal5);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.FEBRUARY, 18, 18, 0), "some text", 666);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(userMeal);
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