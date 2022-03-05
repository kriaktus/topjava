package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final EntityManager entityManager;

    public DataJpaMealRepository(CrudMealRepository crudRepository, EntityManager entityManager) {
        this.crudRepository = crudRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(entityManager.getReference(User.class, userId));
        return meal.isNew() || get(meal.id(), userId) != null ? crudRepository.save(meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllByUserIdAndDateTimeIsGreaterThanEqualAndDateTimeIsLessThanOrderByDateTimeDesc(userId, startDateTime, endDateTime);
    }
}
