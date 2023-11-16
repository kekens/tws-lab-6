package ru.kekens;


import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import ru.kekens.resources.AccountResource;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс для запуска REST-сервиса
 */
@ApplicationPath("/api")
public class App extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(AccountResource.class );
        return set;
    }

}