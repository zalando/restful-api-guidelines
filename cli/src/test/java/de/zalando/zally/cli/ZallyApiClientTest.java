package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.jadler.Jadler.*;
import static org.junit.Assert.*;


public class ZallyApiClientTest {
    final String TOKEN = "1956eeee-ffff-eeee-ffff-abcdeff767325";
    final String REQUEST_BODY = "{\"hello\":\"world\"}";

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
                .havingHeaderEqualTo("Authorization", "Bearer " + TOKEN)
                .havingHeaderEqualTo("Content-Type", "application/json")
                .havingBodyEqualTo(REQUEST_BODY)
        .respond()
                .withStatus(200)
                .withBody(responseBody);

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", TOKEN);
        JsonObject result = client.validate(REQUEST_BODY).asObject();

        assertEquals("ok", result.get("response").asString());
    }

    @Test(expected = ParseException.class)
    public void validateRaisesParseException() throws Exception {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo("/")
                .havingHeaderEqualTo("Authorization", "Bearer " + TOKEN)
                .havingHeaderEqualTo("Content-Type", "application/json")
                .havingBodyEqualTo(REQUEST_BODY)
        .respond()
                .withStatus(200)
                .withBody("");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", TOKEN);
        client.validate(REQUEST_BODY).asObject();
    }
}
