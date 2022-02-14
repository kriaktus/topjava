package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}, userId is {}", meal, userId);
        if (meal.getUserId() != userId) return null;
        if (repository.get(userId) == null) {
            repository.putIfAbsent(userId, new ConcurrentHashMap<>());
            log.info("create repository map for user with id {}", userId);
        }
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        return get(meal.getId(), userId) != null ? mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}, userId is {}", id, userId);
        boolean result = get(id, userId) != null && repository.get(userId).remove(id) != null;
        if (repository.get(userId).isEmpty()) {
            repository.remove(userId);
            log.info("delete repository map of user with id {}", userId);
        }
        return result;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}, userId is {}", id, userId);
        Meal meal = repository.get(userId) != null ? repository.get(userId).get(id) : null;
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll, userId is {}", userId);
        List<Meal> mealList = repository.get(userId) != null ? new ArrayList<>(repository.get(userId).values()) : Collections.emptyList();
        return mealList.stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllWithFilter userId is {}, startDate {}, endDate {}, startTime {}, endTime {}", userId, startDate, endDate, startTime, endTime);
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate, false))
//                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime, true))
                .collect(Collectors.toList());
    }
}

