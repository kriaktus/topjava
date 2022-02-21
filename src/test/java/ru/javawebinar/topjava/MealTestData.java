package ru.javawebinar.topjava;

import org.assertj.core.api.ThrowableAssert;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final List<Meal> USER_MEAL_LIST;
    public static final List<Meal> USER_FILTERED_FROM_12_TO_13_MEAL_LIST;
    public static final Meal userMeal10003;
    public static final Meal userMeal10004;
    public static final Meal userMeal10005;
    public static final Meal userMeal10006;
    public static final Meal userMeal10007;
    public static final Meal userMeal10008;
    public static final Meal userMeal10009;
    public static final int NOT_THIS_USER_MEAL_ID;

    static {
        userMeal10005 = new Meal(100005, LocalDateTime.of(2022, Month.FEBRUARY, 14, 20, 0), "Ужин (User100000)", 500);
        userMeal10004 = new Meal(100004, LocalDateTime.of(2022, Month.FEBRUARY, 14, 13, 0), "Обед (User100000)", 1000);
        userMeal10003 = new Meal(100003, LocalDateTime.of(2022, Month.FEBRUARY, 14, 8, 0), "Завтрак (User100000)", 500);
        userMeal10009 = new Meal(100009, LocalDateTime.of(2022, Month.FEBRUARY, 13, 23, 0), "Ужин2 (User100000)", 100);
        userMeal10008 = new Meal(100008, LocalDateTime.of(2022, Month.FEBRUARY, 13, 20, 0), "Ужин (User100000)", 500);
        userMeal10007 = new Meal(100007, LocalDateTime.of(2022, Month.FEBRUARY, 12, 13, 0), "Обед (User100000)", 1000);
        userMeal10006 = new Meal(100006, LocalDateTime.of(2022, Month.FEBRUARY, 13, 8, 0), "Завтрак (User100000)", 500);
        NOT_THIS_USER_MEAL_ID = 666;
        USER_MEAL_LIST = Arrays.asList(userMeal10005, userMeal10004, userMeal10003, userMeal10009, userMeal10008, userMeal10006, userMeal10007);
        USER_FILTERED_FROM_12_TO_13_MEAL_LIST = Arrays.asList(userMeal10009, userMeal10008, userMeal10006, userMeal10007);
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.FEBRUARY, 18, 18, 0), "some text", 666);
    }

    public static Meal getUpdated10005() {
        Meal updatedMeal10005 = new Meal(userMeal10005);
        updatedMeal10005.setDateTime(LocalDateTime.of(2170, Month.MARCH, 10, 15, 0));
        updatedMeal10005.setDescription("updated description");
        updatedMeal10005.setCalories(328);
        return updatedMeal10005;
    }

    public static void assertMealsById(Meal actual, Meal expected) {
        assertThat(actual)
                .usingComparator(Comparator.comparing(AbstractBaseEntity::getId))
                .isEqualTo(expected);
    }

    public static void assertMealsById(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual)
                .usingElementComparator(Comparator.comparing(AbstractBaseEntity::getId))
                .isEqualTo(expected);
    }

    public static void assertThatNotFoundException(ThrowableAssert.ThrowingCallable throwingCallable) {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(throwingCallable);
    }
}
