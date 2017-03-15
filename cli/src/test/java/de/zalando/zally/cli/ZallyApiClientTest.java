package de.zalando.zally.cli;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;



public class ZallyApiClientTest {
    private final String token = "1956eeee-ffff-eeee-ffff-abcdeff767325";
    private final String requestBody = "{\"hello\":\"world\"}";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        initJadler();
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test
    public void validateReturnsOutputFromZallyServer() throws Exception {
        final String responseBody = "{\"violations\":[], \"violations_count\":{}}";

        mockServer(200, responseBody);

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        ZallyApiResponse response = client.validate(requestBody);

        assertEquals(0, response.getViolations().size());
    }

    @Test
    public void validateRaisesCliException() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("A JSONObject text must begin with '{' at 1 [character 2 line 1]");

        mockServer(200, "");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateRaisesCliExceptionWhen400IsReturned() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("API: An error occurred while querying Zally server");


        mockServer(400, "");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateRaisesCliExceptionWhen400WithDetailsIsReturned() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage(
                "API: An error occurred while querying Zally server\n\n"
                + "Could not read document: Unexpected end-of-input"
        );

        mockServer(400, "{\"detail\":\"Could not read document: Unexpected end-of-input\"}");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateRaisesCliExceptionWhenServerUnaccessible() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("API: An error occurred while querying Zally server");

        ZallyApiClient client = new ZallyApiClient("http://localhost:65534/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    private void mockServer(int status, String body) {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo("/")
                .havingHeaderEqualTo("Authorization", "Bearer " + token)
                .havingHeaderEqualTo("Content-Type", "application/json")
                .havingBodyEqualTo(requestBody)
                .respond()
                .withStatus(status)
                .withBody(body);
    }
}
