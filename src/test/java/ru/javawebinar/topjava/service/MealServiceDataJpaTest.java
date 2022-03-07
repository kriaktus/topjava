package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.DataJpaWithActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(resolver = DataJpaWithActiveDbProfileResolver.class)
public class MealServiceDataJpaTest extends AbstractServicePopulateDbTimeTest {

    @Autowired
    private MealService service;

    @Test
    public void getByIdWithUser() {
        Meal actualMeal = service.getByIdWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(actualMeal, meal1);
        USER_MATCHER.assertMatch(actualMeal.getUser(), user);
    }

    @Test
    public void getByIdWithUserNotFoundMeal() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void getByIdWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(MealTestData.MEAL1_ID, GUEST_ID));
    }
}
