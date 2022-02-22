package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    static {
        SLF4JBridgeHandler.install();
    }

    @Test
    public void get() {
        assertMatch(service.get(TEST_USER_MEAL_ID, USER_ID), testUserMeal);
    }

    @Test
    public void delete() {
        assertThat(service.get(TEST_USER_MEAL_ID, USER_ID)).isNotNull();
        service.delete(TEST_USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(TEST_USER_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actualFilteredList = service.getBetweenInclusive(
                LocalDate.of(2022, Month.FEBRUARY, 12),
                LocalDate.of(2022, Month.FEBRUARY, 13), USER_ID);
        assertMatch(actualFilteredList, testUserMealListFrom12To13);
    }

    @Test
    public void getBetweenWithoutRange() {
        List<Meal> actualFilteredList = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(actualFilteredList, testUserMealList);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), testUserMealList);
    }

    @Test
    public void update() {
        service.update(getUpdated(), USER_ID);
        assertMatch(service.get(TEST_USER_MEAL_ID, USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal expectedMeal = getNew();
        Meal actualMeal = service.create(getNew(), USER_ID);
        Integer actualId = actualMeal.getId();
        expectedMeal.setId(actualId);
        assertMatch(actualMeal, expectedMeal);
        assertMatch(service.get(actualId, USER_ID), expectedMeal);
    }

    @Test
    public void deletedMealByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ANOTHER_USER_MEAL_ID, USER_ID));
    }

    @Test
    public void getMealByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(ANOTHER_USER_MEAL_ID, USER_ID));
    }

    @Test
    public void updateMealByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(anotherUserMeal, USER_ID));
    }

    @Test
    public void createDuplicateDateTimeMeal() {
        Meal duplicateTimeMeal = getNew();
        duplicateTimeMeal.setDateTime(testUserAnotherMeal.getDateTime());
        assertThrows(DataAccessException.class, () -> service.create(duplicateTimeMeal, USER_ID));
    }

    @Test
    public void updateDuplicateDateTimeMeal() {
        Meal duplicateTimeMeal = getUpdated();
        duplicateTimeMeal.setDateTime(testUserAnotherMeal.getDateTime());
        assertThrows(DataAccessException.class, () -> service.update(duplicateTimeMeal, USER_ID));
    }
}