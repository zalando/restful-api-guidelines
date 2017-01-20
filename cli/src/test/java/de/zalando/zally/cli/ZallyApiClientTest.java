package de.zalando.zally.cli;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class ZallyApiClientTest {
    private final String token = "1956eeee-ffff-eeee-ffff-abcdeff767325";
    private final String requestBody = "{\"hello\":\"world\"}";

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
        final String responseBody = "{\"response\":\"ok\"}";

        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo("/")
                .havingHeaderEqualTo("Authorization", "Bearer " + token)
                .havingHeaderEqualTo("Content-Type", "application/json")
                .havingBodyEqualTo(requestBody)
        .respond()
                .withStatus(200)
                .withBody(responseBody);

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        JsonObject result = client.validate(requestBody).asObject();

        assertEquals("ok", result.get("response").asString());
    }

    @Test(expected = ParseException.class)
    public void validateRaisesParseException() throws Exception {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo("/")
                .havingHeaderEqualTo("Authorization", "Bearer " + token)
                .havingHeaderEqualTo("Content-Type", "application/json")
                .havingBodyEqualTo(requestBody)
        .respond()
                .withStatus(200)
                .withBody("");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        client.validate(requestBody).asObject();
    }

    @Test(expected = CliException.class)
    public void validateRaisesCliExceptionWhenServerUnaccessible() throws Exception {
        ZallyApiClient client = new ZallyApiClient("http://localhost:65534/", token);
        client.validate(requestBody);
    }
}
