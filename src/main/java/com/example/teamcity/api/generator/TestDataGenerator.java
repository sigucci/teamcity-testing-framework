package com.example.teamcity.api.generator;

import com.example.teamcity.api.annotations.Exclude;
import com.example.teamcity.api.annotations.Optional;
import com.example.teamcity.api.annotations.Parameterizable;
import com.example.teamcity.api.annotations.Random;
import com.example.teamcity.api.models.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static <T extends BaseModel> T generate(boolean skipExcluded, List<BaseModel> generatedModels, Class<T> generatorClass, Object... parameters) {
        try {
            var instance = generatorClass.getDeclaredConstructor().newInstance();
            for (var field : generatorClass.getDeclaredFields()) {
                field.setAccessible(true);

                if (skipExcluded && field.isAnnotationPresent(Exclude.class)) {
                    field.set(instance, null);
                    field.setAccessible(false);
                    continue;
                }

                if (!field.isAnnotationPresent(Optional.class)) {
                    var generatedClass = generatedModels.stream()
                            .filter(m -> m.getClass().equals(field.getType()))
                            .findFirst();
                    if (field.isAnnotationPresent(Parameterizable.class) && parameters.length > 0) {
                        field.set(instance, parameters[0]);
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
                    } else if (field.isAnnotationPresent(Random.class)) {
                        if (String.class.equals(field.getType())) {
                            field.set(instance, RandomData.getString());
                        }
                    } else if (BaseModel.class.isAssignableFrom(field.getType())) {
                        var finalParameters = parameters;
                        field.set(instance, generatedClass.orElseGet(() -> generate(skipExcluded,
                                generatedModels,
                                field.getType().asSubclass(BaseModel.class),
                                finalParameters)));
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        if (field.getGenericType() instanceof ParameterizedType pt) {
                            var typeClass = (Class<?>) pt.getActualTypeArguments()[0];
                            if (BaseModel.class.isAssignableFrom(typeClass)) {
                                var finalParameters = parameters;
                                field.set(instance, generatedClass.map(List::of)
                                        .orElseGet(() -> List.of(generate(skipExcluded,
                                                generatedModels,
                                                typeClass.asSubclass(BaseModel.class),
                                                finalParameters))));
                            }
                        }
                    }
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new IllegalStateException("Can not generate test data", e);
        }
    }

    public static TestData generateAll() {
        try {
            var instance = TestData.class.getDeclaredConstructor().newInstance();
            var generatedModels = new ArrayList<BaseModel>();
            for (var field : TestData.class.getDeclaredFields()) {
                field.setAccessible(true);
                if (BaseModel.class.isAssignableFrom(field.getType())) {
                    // В generateAll() передаём skipExcluded = false, чтобы не исключать поля
                    var generatedModel = generate(false, generatedModels, field.getType().asSubclass(BaseModel.class));
                    field.set(instance, generatedModel);
                    generatedModels.add(generatedModel);
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Can not generate test data", e);
        }
    }

    public static TestData generateWithoutExcluded() {
        try {
            var instance = TestData.class.getDeclaredConstructor().newInstance();
            var generatedModels = new ArrayList<BaseModel>();
            for (var field : TestData.class.getDeclaredFields()) {
                field.setAccessible(true);
                // Здесь передаём skipExcluded = true, чтобы пропустить поля с @Exclude
                if (BaseModel.class.isAssignableFrom(field.getType())) {
                    var generatedModel = generate(true, generatedModels, field.getType().asSubclass(BaseModel.class));
                    field.set(instance, generatedModel);
                    generatedModels.add(generatedModel);
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Can not generate test data", e);
        }
    }

    public static Project generateProject(boolean full) {
        TestData data = full ? generateAll() : generateWithoutExcluded();
        return data.getProject();
    }
}