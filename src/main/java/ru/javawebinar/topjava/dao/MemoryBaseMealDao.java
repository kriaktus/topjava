package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryBaseMealDao implements MealDao {
    private AtomicInteger idCounter = new AtomicInteger(0);
    private final Map<Integer, Meal> memoryBase = new ConcurrentHashMap<>();

    {
        List<Meal> list = Arrays.asList(
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 29, 0, 0), "Завтрак", 1),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 29, 12, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 29, 18, 0), "Ужин1", 800),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 29, 23, 59), "Ужин2", 200),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        for (Meal meal : list) {
            save(meal);
        }
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(idCounter.incrementAndGet());
        } else if (!memoryBase.containsKey(meal.getId())) {
            return null;
        }
        memoryBase.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(memoryBase.values());
    }

    @Override
    public Meal getById(int id) {
        return memoryBase.get(id);
    }

    @Override
    public void delete(int id) {
        memoryBase.remove(id);
    }
}
