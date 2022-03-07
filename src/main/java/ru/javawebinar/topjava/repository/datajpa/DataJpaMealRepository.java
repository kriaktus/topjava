package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudMealRepository = crudRepository;
        this.crudUserRepository = userRepository;
    }

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(crudUserRepository.getById(userId));
        return meal.isNew() || get(meal.id(), userId) != null ? crudMealRepository.save(meal) : null;
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public Meal getByIdWithUser(int id, int userId) {
        return crudMealRepository.getByIdWithUser(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.findAllByUserIdAndDateTimeIsGreaterThanEqualAndDateTimeIsLessThanOrderByDateTimeDesc(userId, startDateTime, endDateTime);
    }
}
