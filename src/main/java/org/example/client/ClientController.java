package org.example.client;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ClientController extends HttpServlet {
    private final ClientService service;
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientController.class);

    @Inject
    public ClientController(ClientService service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Handle GET request
        // For example, get all clients
        resp.getWriter().write(service.findAll().toString());
    }

    /**
     * Create a new client
     *
     * @param req  request object
     * @param resp response object
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }
        String requestBody = stringBuilder.toString();

        logger.info("Request body: " + requestBody);
        service.save(requestBody);
    }

    /**
     * Update a client
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }
        String requestBody = stringBuilder.toString();

        logger.info("Request body: " + requestBody);
        service.update(requestBody);
    }
    /**
     * Delete a client
     *
     * @param req request object
     * @param resp response object
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }
        String requestBody = stringBuilder.toString();

        logger.info("Request body: " + requestBody);
        service.delete(requestBody);
    }


}
