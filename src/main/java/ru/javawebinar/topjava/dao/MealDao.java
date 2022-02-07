package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal save(Meal meal);
    List<Meal> getAll();
    Meal getById(int id);
    void delete(int id);
}
