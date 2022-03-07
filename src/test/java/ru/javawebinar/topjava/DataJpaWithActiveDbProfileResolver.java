package ru.javawebinar.topjava;

import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfilesResolver;


public class DataJpaWithActiveDbProfileResolver implements ActiveProfilesResolver {
    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> testClass) {
        return Profiles.getDataJpaWithActiveDbProfileArray();
    }
}
