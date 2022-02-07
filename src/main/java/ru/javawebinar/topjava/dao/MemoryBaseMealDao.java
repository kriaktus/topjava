package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryBaseMealDao implements MealDao {
    private static Integer idCounter = 0;
    private final Map<Integer, Meal> memoryBase = new ConcurrentHashMap<>();

    {
        List<Meal> list = Arrays.asList(
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 29, 00, 00), "Завтрак", 1),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 29, 12, 0), "Обед", 1000),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 29, 18, 0), "Ужин1", 800),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 29, 23, 59), "Ужин2", 200),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        for (Meal meal : list) {
            add(meal);
        }
    }

    @Override
    public void add(Meal meal) {
        if (meal.getId() == 0) {
            synchronized (MemoryBaseMealDao.class) {
                meal.setId(++idCounter);
            }
        }
        memoryBase.put(meal.getId(), meal);
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
    public void update(Meal meal) {
        memoryBase.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        memoryBase.remove(id);
    }
}
