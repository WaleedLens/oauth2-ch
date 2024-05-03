package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.example.database.Connector;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        // Other configuration here...
    }

    @Provides
    public Connector provideConnector() {
        return Connector.getInstance();
    }
}