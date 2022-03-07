package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.DataJpaWithActiveDbProfileResolver;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(resolver = DataJpaWithActiveDbProfileResolver.class)
public class UserServiceDataJpaTest extends AbstractServicePopulateDbTimeTest {

    @Autowired
    private UserService service;

    @Test
    public void getByIdWithMeals() {
        User actual = service.getByIdWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, user);
        List<Meal> actualMeals = actual.getMeals();
        List<Meal> expectedMeals = user.getMeals();
        for (int i = 0; i < actualMeals.size(); i++) {
            MEAL_MATCHER.assertMatch(actualMeals.get(i), expectedMeals.get(i));
        }
    }

    @Test
    public void getByIdWithMealsWhereUserHaveNoMeals() {
        User actual = service.getByIdWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(actual, guest);
        List<Meal> actualMeals = actual.getMeals();
        List<Meal> expectedMeals = guest.getMeals();
        for (int i = 0; i < actualMeals.size(); i++) {
            MEAL_MATCHER.assertMatch(actualMeals.get(i), expectedMeals.get(i));
        }
    }

    @Test
    public void getByIdWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithMeals(NOT_FOUND));
    }
}
