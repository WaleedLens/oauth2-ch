package org.example;

public class Main {

    public static void main(String[] args) {
        WebServer webServer = WebServer.getInstance();
        webServer.start();
    }
}