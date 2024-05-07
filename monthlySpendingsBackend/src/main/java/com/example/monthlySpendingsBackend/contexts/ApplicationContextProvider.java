package com.example.monthlySpendingsBackend.contexts;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        ApplicationContextProvider.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
