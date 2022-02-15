package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.mealListForUser1.forEach(meal -> save(meal, 1));
        MealsUtil.mealListForUser2.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}, userId is {}", meal, userId);
        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            return mealMap.compute(meal.getId(), (id, oldMeal) -> meal);
        }
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}, userId is {}", id, userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}, userId is {}", id, userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null ? mealMap.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll, userId is {}", userId);
        return getAllWithFilter(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllWithFilter userId is {}", userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null ? mealMap.values().stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate, false))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()) :
                Collections.emptyList();
    }
}