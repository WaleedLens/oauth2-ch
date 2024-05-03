package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppInjector());

        WebServer webServer = injector.getInstance(WebServer.class);
        webServer.start();
    }

}