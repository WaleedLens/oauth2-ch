package org.example;

import com.google.inject.AbstractModule;
import org.example.authorization.AuthenticationController;
import org.example.authorization.AuthenticationService;
import org.example.client.ClientController;
import org.example.client.ClientService;
import org.example.token.TokenController;
import org.example.token.TokenService;
import org.example.validation.TokenValidator;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(TokenValidator.class).to(TokenValidator.class);
        bind(TokenController.class).to(TokenController.class);
        bind(ClientController.class).to(ClientController.class);
        bind(ClientService.class).to(ClientService.class);
        bind(AuthenticationController.class).to(AuthenticationController.class);
        bind(AuthenticationService.class).to(AuthenticationService.class);
        bind(TokenService.class).to(TokenService.class);
    }

}