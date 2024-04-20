package org.example.client;

import org.apache.logging.log4j.Logger;
import org.example.clientAuth.ClientAuth;
import org.example.clientAuth.ClientAuthRepository;
import org.example.utils.JsonHandler;

import java.util.List;
import java.util.UUID;

public class ClientService {
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientService.class);
    private final ClientRepository clientRepository = new ClientRepository();
    private final ClientAuthRepository clientAuthRepository = new ClientAuthRepository();

    public void save(String json) {
        // Save client
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        generateClientAuth(clientRepository.save(client));
        logger.info("Client saved: " + client);
    }

    public void delete(String json) {
        // Delete client
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        clientRepository.delete(client);
        logger.info("Client deleted: " + client);

    }

    public void update(String json) {
        // Update client
        // Save client
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        clientRepository.update(client);
        logger.info("Client updated: " + client);
    }

    public Client find(Long id) {
        // Find client
        return clientRepository.find(id);

    }

    public List<Client> findAll() {
        // Find all clients
        return clientRepository.findAll();
    }


    public void generateClientAuth(long id) {
        logger.info("initiating client auth generation");
        String clientUid = org.example.utils.OAuthUtils.generateClientUid().toString();
        String clientSecret = org.example.utils.OAuthUtils.generateClientSecret();
        logger.info("Client UID: " + clientUid + " Client Secret: " + clientSecret + " for client id: " + id);
        clientAuthRepository.save(new ClientAuth(clientUid, clientSecret, id));

    }

}
