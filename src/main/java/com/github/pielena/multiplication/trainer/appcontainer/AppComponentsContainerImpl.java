package com.github.pielena.multiplication.trainer.appcontainer;

import com.github.pielena.multiplication.trainer.appcontainer.api.AppComponent;
import com.github.pielena.multiplication.trainer.appcontainer.api.AppComponentsContainer;
import com.github.pielena.multiplication.trainer.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> beanMethods = stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        try {
            Object configClassInstance = configClass.getDeclaredConstructor().newInstance();

            for (Method method : beanMethods) {
                if (appComponentsByName.containsKey(method.getAnnotation(AppComponent.class).name())) {
                    throw new RuntimeException(format("There are two methods with the same name: %s", method.getAnnotation(AppComponent.class).name()));
                }

                Object[] args = stream(method.getParameterTypes())
                        .map(this::getAppComponent)
                        .toArray();

                Object bean = method.invoke(configClassInstance, args);
                String beanName = method.getAnnotation(AppComponent.class).name();
                appComponentsByName.put(beanName, bean);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> beans = appComponentsByName.values().stream()
                .filter(bean -> componentClass.isAssignableFrom(bean.getClass()))
                .toList();

        if (beans.size() != 1) {
            throw new RuntimeException(format("Failed to determine bean in %s", componentClass.getName()));
        }
        return (C) beans.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return ofNullable((C) appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException(format("Failed to determine bean in %s", componentName)));
    }
}
