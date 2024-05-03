// ClientService.java
package org.example.client;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.clientAuth.ClientAuth;
import org.example.clientAuth.ClientAuthRepository;
import org.example.utils.JsonHandler;

import java.util.List;

public class ClientService {
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ClientAuthRepository clientAuthRepository;

    @Inject
    public ClientService(ClientRepository clientRepository, ClientAuthRepository clientAuthRepository) {
        this.clientRepository = clientRepository;
        this.clientAuthRepository = clientAuthRepository;
    }

    public ClientDTO save(String json) {
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        long id = clientRepository.save(client);
        String client_secret = generateClientAuth(id);
        logger.info("Client saved: " + client);
        return new ClientDTO(client.getName(), client.getWebsite(), client.getLogo(), client.getRedirectUri(), String.valueOf(id),client_secret);
    }

    public void delete(String json) {
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        clientRepository.delete(client);
        logger.info("Client deleted: " + client);
    }

    public void update(String json) {
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        clientRepository.update(client);
        logger.info("Client updated: " + client);
    }

    public Client find(Long id) {
        return clientRepository.find(id);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    private String generateClientAuth(long id) {
        String clientUid = org.example.utils.OAuthUtils.generateClientUid().toString();
        String clientSecret = org.example.utils.OAuthUtils.generateClientSecret();
        clientAuthRepository.save(new ClientAuth(clientUid, clientSecret, id));
        return clientSecret;
    }
}