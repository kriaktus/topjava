package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class JdbcValidator {
    private static final Logger log = LoggerFactory.getLogger(JdbcValidator.class);
    private static Validator validator;
    private static JdbcValidator instance;

    private JdbcValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static JdbcValidator getInstance() {
        return instance != null ? instance : new JdbcValidator();
    }

    public <T> T validate(T object) {
        if (object == null) return null;
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) log.warn("{} : {}", object, violation.getMessage());
            throw new ValidationException(violations.toString());
        }
        return object;
    }

    public <T> List<T> validate(List<T> objects) {
        for (T object : objects) {
            validate(object);
        }
        return objects;
    }
}