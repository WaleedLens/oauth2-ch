package endpoints;

import org.example.authorization.Authentication;
import org.example.authorization.AuthenticationController;

import org.example.authorization.ClientAuthenticationStrategy;
import org.example.authorization.ServerAuthenticationStrategy;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AuthenticationControllerTest {


    // Verify that doGet handles ResponseType.CODE correctly by using serverStrategy
    @Test
    public void test_do_get_with_response_type_code() {
        try {
            HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
            HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
            Authentication authentication = new Authentication("CODE", 123, "http://example.com", "state123", "scope");
            Mockito.when(req.getAttribute("Authentication")).thenReturn(authentication);

            ServerAuthenticationStrategy serverStrategy = Mockito.mock(ServerAuthenticationStrategy.class);
            ClientAuthenticationStrategy clientStrategy = Mockito.mock(ClientAuthenticationStrategy.class);
            AuthenticationController controller = new AuthenticationController(clientStrategy,serverStrategy);

            controller.doGet(req, resp);
            Mockito.verify(serverStrategy).processAuthentication(Mockito.any(Authentication.class), Mockito.eq(resp));

        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Test doGet with an invalid responseType and verify SC_BAD_REQUEST is returned
    @Test
    public void test_do_get_with_invalid_response_type() {
        try {
            HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
            HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
            Authentication authentication = new Authentication("INVALID_TYPE", 123, "http://example.com", "state123", "scope");
            Mockito.when(req.getAttribute("Authentication")).thenReturn(authentication);

            ServerAuthenticationStrategy serverStrategy = Mockito.mock(ServerAuthenticationStrategy.class);
            ClientAuthenticationStrategy clientStrategy = Mockito.mock(ClientAuthenticationStrategy.class);
            AuthenticationController controller = new AuthenticationController(clientStrategy,serverStrategy);


            controller.doGet(req, resp);
            Mockito.verify(resp).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid response type");

        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}