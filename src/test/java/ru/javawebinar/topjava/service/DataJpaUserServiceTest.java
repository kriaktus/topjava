package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void getByIdWithMeals() {
        User actual = service.getByIdWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, user);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    public void getByIdWithMealsWhereUserHaveNoMeals() {
        User actual = service.getByIdWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(actual, guest);
        MEAL_MATCHER.assertMatch(actual.getMeals(), List.of());
    }

    @Test
    public void getByIdWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithMeals(NOT_FOUND));
    }
}
