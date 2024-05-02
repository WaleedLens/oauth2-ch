package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.client.ClientController;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();

        WebServer webServer = injector.getInstance(WebServer.class);
        webServer.start();
    }

}