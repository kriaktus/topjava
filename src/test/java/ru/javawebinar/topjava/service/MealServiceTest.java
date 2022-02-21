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

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
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
        assertMealsById(service.get(100003, USER_ID), userMeal10003);
    }

    @Test
    public void delete() {
        Meal mealForDel = service.get(100003, USER_ID);
        assertThat(mealForDel).isNotNull();
        service.delete(100003, USER_ID);
        assertThatNotFoundException(() -> service.get(100003, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actualFilteredList = service.getBetweenInclusive(
                LocalDate.of(2022, Month.FEBRUARY, 12),
                LocalDate.of(2022, Month.FEBRUARY, 13), USER_ID);
        assertMealsById(actualFilteredList, USER_FILTERED_FROM_12_TO_13_MEAL_LIST);
    }

    @Test
    public void getAll() {
        assertMealsById(service.getAll(USER_ID), USER_MEAL_LIST);
    }

    @Test
    public void update() {
        Meal expectedMeal100005 = getUpdated10005();
        service.update(expectedMeal100005, USER_ID);
        assertMealsById(service.get(100005, USER_ID), expectedMeal100005);
    }

    @Test
    public void create() {
        Meal createdMeal = service.create(getNew(), USER_ID);
        Integer newId = createdMeal.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMealsById(createdMeal, newMeal);
    }

    @Test
    public void deletedNotFound() {
        assertThatNotFoundException(() -> service.delete(NOT_THIS_USER_MEAL_ID, USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThatNotFoundException(() -> service.get(NOT_THIS_USER_MEAL_ID, USER_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated10005();
        updated.setId(NOT_THIS_USER_MEAL_ID);
        assertThatNotFoundException(() -> service.update(updated, USER_ID));
    }

    @Test
    public void updateDuplicateDateTimeCreate() {
        Meal updated10005 = getUpdated10005();
        updated10005.setDateTime(userMeal10007.getDateTime());
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> service.update(updated10005, USER_ID));
    }
}