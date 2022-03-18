package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public abstract class AbstractMealController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    protected Meal create(Meal meal) {
        log.info("create meal {}", meal);
        return service.create(meal, authUserId());
    }

    protected Meal get(int id) {
        log.info("get meal with id {}", id);
        return service.get(id, authUserId());
    }

    protected void update(Meal meal) {
        log.info("update meal {}", meal);
        service.update(meal, authUserId());
    }

    protected void delete(int id) {
        log.info("delete meal with id {}", id);
        service.delete(id, authUserId());
    }

    protected List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    protected List<MealTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getBetween dates({} - {}) time({} - {})", startDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate, endDate, authUserId()), authUserCaloriesPerDay(), startTime, endTime);
    }
}