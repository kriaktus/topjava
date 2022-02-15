package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY;
    public static final List<Meal> mealListForUser1;
    public static final List<Meal> mealListForUser2;

    static {
        DEFAULT_CALORIES_PER_DAY = 2000;
        mealListForUser1 = Arrays.asList(
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 14, 8, 0), "Завтрак (User1)", 500),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 14, 13, 0), "Обед (User1)", 1000),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 14, 20, 0), "Ужин (User1)", 500),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 8, 0), "Завтрак (User1)", 500),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 13, 0), "Обед (User1)", 1000),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 20, 0), "Ужин (User1)", 500),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 23, 0), "Ужин2 (User1)", 100)
        );
        mealListForUser2 = Arrays.asList(
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 8, 0), "Завтрак (User2)", 400),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 13, 0), "Обед (User2)", 1100),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 13, 20, 0), "Ужин (User2)", 500),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 12, 0, 0), "Еда на граничное значение (User2)", 100),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 12, 10, 0), "Завтрак (User2)", 1000),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 12, 13, 0), "Обед (User2)", 500),
                new Meal(LocalDateTime.of(2022, Month.FEBRUARY, 12, 20, 0), "Ужин (User2)", 410)
        );
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime, true));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
