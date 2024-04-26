package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static final Injector injectorInstance = Guice.createInjector(new AppInjector());

    public static void main(String[] args) {

        WebServer webServer = WebServer.getInstance();
        webServer.start();
    }
}