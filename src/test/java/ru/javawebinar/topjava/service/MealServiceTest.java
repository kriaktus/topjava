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
        assertMatch(service.get(TEST_USER_MEAL_ID, TEST_USER_ID), testUserMeal);
    }

    @Test
    public void delete() {
        Meal mealForDel = service.get(TEST_USER_MEAL_ID, TEST_USER_ID);
        assertThat(mealForDel).isNotNull();
        service.delete(TEST_USER_MEAL_ID, TEST_USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(TEST_USER_MEAL_ID, TEST_USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actualFilteredList = service.getBetweenInclusive(
                LocalDate.of(2022, Month.FEBRUARY, 12),
                LocalDate.of(2022, Month.FEBRUARY, 13), TEST_USER_ID);
        assertMatch(actualFilteredList, testUserFilteredFrom12To13MealList);
    }

    @Test
    public void getBetweenWithoutRange() {
        List<Meal> actualFilteredList = service.getBetweenInclusive(null, null, TEST_USER_ID);
        assertMatch(actualFilteredList, testUserMealList);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(TEST_USER_ID), testUserMealList);
    }

    @Test
    public void update() {
        service.update(getUpdated(), TEST_USER_ID);
        assertMatch(service.get(TEST_USER_MEAL_ID, TEST_USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal createdMeal = service.create(getNew(), TEST_USER_ID);
        Integer newId = createdMeal.getId();
        Meal newMeal = service.get(newId, TEST_USER_ID);
        assertMatch(createdMeal, newMeal);
    }

    @Test
    public void deletedMealByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ANOTHER_USER_MEAL_ID, TEST_USER_ID));
    }

    @Test
    public void getMealByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(ANOTHER_USER_MEAL_ID, TEST_USER_ID));
    }

    @Test
    public void updateMealByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(anotherUserMeal, TEST_USER_ID));
    }

    @Test
    public void createDuplicateDateTimeMeal() {
        Meal duplicateTimeMeal = getNew();
        duplicateTimeMeal.setDateTime(testUserAnotherMeal.getDateTime());
        assertThrows(DataAccessException.class, () -> service.create(duplicateTimeMeal, TEST_USER_ID));
    }

    @Test
    public void updateDuplicateDateTimeMeal() {
        Meal duplicateTimeMeal = getUpdated();
        duplicateTimeMeal.setDateTime(testUserAnotherMeal.getDateTime());
        assertThrows(DataAccessException.class, () -> service.update(duplicateTimeMeal, TEST_USER_ID));
    }
}