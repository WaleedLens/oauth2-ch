// ClientController.java
package org.example.client;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.utils.JsonHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClientController extends HttpServlet {
    @Inject
    private ClientService service;
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write(service.findAll().toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = getRequestBody(req);
        ClientDTO clientDTO = service.save(requestBody);
        sendJsonResponse(resp, JsonHandler.toJson(clientDTO));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = getRequestBody(req);
        service.update(requestBody);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = getRequestBody(req);
        service.delete(requestBody);
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private void sendJsonResponse(HttpServletResponse response, String json) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            logger.error("Error sending JSON response", e);
            throw new RuntimeException(e);
        }
    }
}