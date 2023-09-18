package com.github.pielena.multiplication.trainer.appcontainer.api;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass);
    <C> C getAppComponent(String componentName);
}
