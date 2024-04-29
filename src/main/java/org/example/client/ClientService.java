package org.example.client;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.clientAuth.ClientAuth;
import org.example.clientAuth.ClientAuthRepository;
import org.example.utils.JsonHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

public class ClientService {
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ClientAuthRepository clientAuthRepository;

    @Inject
    public ClientService(ClientRepository clientRepository, ClientAuthRepository clientAuthRepository) {
        this.clientRepository = clientRepository;
        this.clientAuthRepository = clientAuthRepository;
    }

    public void save(HttpServletResponse response, String json) {
        // Save client
        Client client = (Client) JsonHandler.toObject(json, Client.class);
        long id = clientRepository.save(client);
        String client_secret = generateClientAuth(id);
        logger.info("Client saved: " + client);
        ClientDTO clientDTO = new ClientDTO(client.getName(), client.getWebsite(), client.getLogo(), client.getRedirectUri(), String.valueOf(id),client_secret);
        String json_response = JsonHandler.toJson(clientDTO);
        sendJsonResponse(response, json_response);

    }


    private void sendJsonResponse(HttpServletResponse response, String json) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            logger.error("Error sending JSON response", e);
        }
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


    public String generateClientAuth(long id) {
        logger.info("initiating client auth generation");
        String clientUid = org.example.utils.OAuthUtils.generateClientUid().toString();
        String clientSecret = org.example.utils.OAuthUtils.generateClientSecret();
        logger.info("Client UID: " + clientUid + " Client Secret: " + clientSecret + " for client id: " + id);
        clientAuthRepository.save(new ClientAuth(clientUid, clientSecret, id));
        return clientSecret;

    }

}
